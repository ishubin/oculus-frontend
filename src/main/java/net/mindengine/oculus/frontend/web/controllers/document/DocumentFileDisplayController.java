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
