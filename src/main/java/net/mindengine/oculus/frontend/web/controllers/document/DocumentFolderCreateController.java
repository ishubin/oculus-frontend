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

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

public class DocumentFolderCreateController extends SecureSimpleViewController {
	Log logger = LogFactory.getLog(getClass());
	private FolderDAO folderDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			verifyPermissions(request);
			logger.info("Creating folder");
			Session s = Session.create(request);
			User user = s.getAuthorizedUser();

			String folderName = request.getParameter("name");
			String projectId = request.getParameter("projectId");
			String parentId = request.getParameter("parentId");

			Folder folder = new Folder();
			folder.setName(folderName);
			folder.setUserId(user.getId());

			folder.setProjectId(Long.parseLong(projectId));
			folder.setParentId(Long.parseLong(parentId));
			

			// Creating a folder in DB
			Long id = folderDAO.createFolder(folder);

			// Returning ok result
			OutputStream os = response.getOutputStream();
			OutputStreamWriter w = new OutputStreamWriter(os);
			response.setContentType("text/html");

			w.write("{result:\"added\", " + "id:\"" + id + "\", " + "projectId:\"" + folder.getProjectId() + "\", " + "parentId:\"" + folder.getParentId() + "\", " + "name:\"" + StringEscapeUtils.escapeJavaScript(folder.getName()) + "\"}");

			w.flush();
			os.flush();
			os.close();
		}
		catch (Exception e) {
			OutputStream os = response.getOutputStream();
			OutputStreamWriter w = new OutputStreamWriter(os);
			response.setContentType("text/html");

			w.write("{result:\"error\", " + "type: \"" + StringEscapeUtils.escapeJavaScript(e.getClass().getName()) + "\", " + "text:\"" + StringEscapeUtils.escapeJavaScript(e.getLocalizedMessage()) + "\"}");

			w.flush();
			os.flush();
			os.close();
		}

		return null;
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
	}

}
