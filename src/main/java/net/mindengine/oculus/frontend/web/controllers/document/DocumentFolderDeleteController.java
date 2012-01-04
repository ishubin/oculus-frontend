package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentFolderDeleteController extends SimpleAjaxController {
	Log logger = LogFactory.getLog(getClass());
	private FolderDAO folderDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		verifyPermissions(request);
		Session s = Session.create(request);
		User user = s.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));

		Folder folder = folderDAO.getFolder(id);
		if (folder == null)
			throw new UnexistentResource("Folder with id '" + id + "' doesn't exist");

		folderDAO.deleteFolder(id, folder.getParentId());

		model.setResult("deleted");

		model.setObject(folder);
		return model;
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
	}
}
