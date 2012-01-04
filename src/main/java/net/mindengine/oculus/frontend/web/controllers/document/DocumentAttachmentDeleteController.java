package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.File;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentAttachmentDeleteController  extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Session s = Session.create(request);
		User user = s.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		verifyPermissions(request);
		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));

		DocumentAttachment attachment = documentDAO.getAttachment(id);
		if(attachment == null) throw new UnexistentResource("Attachment", id);
		
		documentDAO.deleteAtachment(id);

		model.setResult("deleted-attachment");

		// Deleting file from file system
		String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(attachment.getDate()) + File.separator + "attachment"+attachment.getId();

		File file = new File(path);
		file.delete();

		//Fetching all document attachments as they should be refreshed in the list
		Collection<DocumentAttachment> attachments = documentDAO.getDocumentAttachments(attachment.getDocumentId());
		model.setObject(attachments);

		return model;
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
