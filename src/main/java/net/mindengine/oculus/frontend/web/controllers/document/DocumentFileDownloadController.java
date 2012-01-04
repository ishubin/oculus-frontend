package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.experior.utils.FileUtils;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DocumentFileDownloadController implements Controller {
	private DocumentDAO documentDAO;
	private Config config;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strId = request.getParameter("id");
		Long id = Long.parseLong(strId);

		Document document = documentDAO.getDocument(id);
		if (document == null)
			throw new UnexistentResource(Document.class, id);
		if (Document._TYPE_FILE.equals(document.getType())) {
			String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(document.getDate()) + File.separator + document.getId();

			File file = new File(path);
			response.setBufferSize((int) file.length());
			String fileName = document.getName();
			if (document.getTypeExtended() != null && !document.getTypeExtended().isEmpty()) {
				fileName += "." + document.getTypeExtended();
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentType("application/download");
			response.setContentLength((int) file.length());

			byte[] bytes = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();

			FileCopyUtils.copy(bytes, response.getOutputStream());

			return null;
		}
		else
			throw new UnexistentResource(Document.class, id);
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

}
