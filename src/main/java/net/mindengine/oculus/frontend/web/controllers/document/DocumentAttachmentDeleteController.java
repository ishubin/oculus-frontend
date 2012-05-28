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
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.document.DocumentAttachment;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentAttachmentDeleteController  extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		User user = getUser(request);
		if (user == null) {
			throw new NotAuthorizedException();
		}

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
