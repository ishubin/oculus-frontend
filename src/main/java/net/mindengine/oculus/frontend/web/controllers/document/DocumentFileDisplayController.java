package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentFileDisplayController extends SimpleAjaxController {
	private DocumentDAO documentDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		String strId = request.getParameter("id");
		Long id = Long.parseLong(strId);

		// Loading document from DB
		Document document = documentDAO.getDocument(id);
		if (document == null)
			throw new UnexistentResource("File with id = '" + id + "' doesn't exist");
		if (Document._TYPE_FILE.equals(document.getType())) {
			AjaxModel model = new AjaxModel();
			model.setResult("file");
			model.setObject(document);
			return model;
		}
		else
			throw new UnexistentResource("File with id = '" + id + "' doesn't exist");
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

}
