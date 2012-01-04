package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentFolderRenameController extends SimpleAjaxController {
	private FolderDAO folderDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Session s = Session.create(request);
		User user = s.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();
		// TODO Implement permission verification for this controller

		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");

		if (id == null || name == null)
			throw new InvalidRequest("id and name should not be null");
		folderDAO.renameFolder(id, name);

		Folder folder = new Folder();
		folder.setId(id);
		folder.setName(name);
		model.setResult("renamed");
		model.setObject(folder);

		return model;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

}
