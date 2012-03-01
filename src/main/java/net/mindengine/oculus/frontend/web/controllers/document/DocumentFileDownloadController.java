/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
