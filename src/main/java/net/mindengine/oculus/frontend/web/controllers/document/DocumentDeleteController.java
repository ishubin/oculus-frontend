package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentDeleteController extends SimpleAjaxController {
	private DocumentDAO documentDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		verifyPermissions(request);
		Session s = Session.create(request);
		User user = s.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));

		Document document = documentDAO.getDocument(id);

		documentDAO.deleteDocument(id, document.getFolderId());

		model.setResult("deleted-" + document.getType());

		// Decreasing amount of data
		document.setDescription(null);
		document.setContent(null);
		model.setObject(document);

		return model;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

}
