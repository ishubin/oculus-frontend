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

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentFileDeleteController extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
	    User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();

		verifyPermissions(request);
		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));

		// Fetching document from DB
		Document document = documentDAO.getDocument(id);

		if (document == null || !Document._TYPE_FILE.equals(document.getType())) {
			throw new UnexistentResource(Document.class, id);
		}

		// Deleting document from DB
		documentDAO.deleteDocument(id, document.getFolderId());

		model.setResult("deleted-file");

		// Deleting file from file system
		String path = config.getDocumentsFolder() + File.separator + FileUtils.generatePath(document.getDate()) + File.separator + document.getId();

		File file = new File(path);
		file.delete();

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

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

}
