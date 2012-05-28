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

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DocumentFolderDeleteController extends SimpleAjaxController {
	Log logger = LogFactory.getLog(getClass());
	private FolderDAO folderDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		verifyPermissions(request);
		User user = getUser(request);
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
