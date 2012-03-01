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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.document;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.experior.utils.XmlUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is called on Ajax method of folder managment The response of
 * this controller is an xml data of folders for specified project
 * 
 * @author ishubin
 */
public class DocumentFoldersDisplayController extends SecureSimpleViewController {
	private FolderDAO folderDAO;
	private ProjectDAO projectDAO;
	private DocumentDAO documentDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Project project = null;
		
		boolean onlyFolders = request.getParameter("onlyFolders")!=null;
		
		String strProjectId = request.getParameter("projectId");
		String strParentId = request.getParameter("parentId");
		String id = request.getParameter("id");
		if (id != null) {
			if (id.charAt(0) == 'p') {
				strProjectId = id.substring(1);
			}
			else if (id.charAt(0) == 'f') {
				strParentId = id.substring(1);
			}
		}
		else {
			if (strProjectId != null) {
				id = "0";
			}
			else if (strParentId != null) {
				id = "f" + strParentId;
			}
		}

		List<Folder> folders = null;
		List<Document> documents = null;
		List<Project> subProjects = null;
		if (strProjectId != null && !strProjectId.isEmpty()) {
			// Taking only root folders of the project
			folders = folderDAO.getFolders(Long.parseLong(strProjectId), 0L);

			/*
			 * The following check is added in case if the requested node is not
			 * a root element of a tree
			 */
			if (request.getParameter("projectId") != null) {
				project = projectDAO.getProject(Long.parseLong(strProjectId));
			}
			if(!onlyFolders){
			    documents = documentDAO.getDocuments(Long.parseLong(strProjectId), 0L);
			}
			subProjects = projectDAO.getSubprojects(Long.parseLong(strProjectId));

		}
		else if (strParentId != null && !strParentId.isEmpty()) {
			folders = folderDAO.getFoldersByParent(Long.parseLong(strParentId));
			if(!onlyFolders){
			    documents = documentDAO.getDocumentsByFolderId(Long.parseLong(strParentId));
			}
		}

		if (folders == null)
			throw new UnexistentResource("");

		OutputStream os = response.getOutputStream();
		response.setContentType("text/xml");

		OutputStreamWriter w = new OutputStreamWriter(os);

		w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		w.write("<tree id=\"" + id + "\" radio=\"1\">");

		if (project != null) {
			w.write("<item text=\"" + XmlUtils.escapeXml(project.getName()) + "\" " + "id=\"p" + project.getId() + "\" im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" nocheckbox=\"1\" >");
		}
		if (subProjects != null) {
			for (Project subProject : subProjects) {
				w.write("<item text=\"" + XmlUtils.escapeXml(subProject.getName()) + "\" " + "id=\"p" + subProject.getId() + "\" im0=\"tombs.gif\" im1=\"tombs_open.gif\" im2=\"tombs.gif\" child=\"1\" nocheckbox=\"1\" >");
				w.write("</item>");
			}
		}
		for (Folder folder : folders) {
			w.write("<item ");
			w.write("text=\"" + XmlUtils.escapeXml(folder.getName()) + "\" ");
			w.write("id=\"f" + folder.getId() + "\" ");
			w.write("im0=\"folderClosed.gif\" im1=\"folderOpen.gif\" im2=\"folderClosed.gif\" nocheckbox=\"1\" ");
			
			if ((folder.getChildren() != null && folder.getChildren() > 0) || ((folder.getDocuments() != null && folder.getDocuments() > 0)&&!onlyFolders)) {
				w.write("child=\"1\" ");
			}
			w.write(">");
			w.write("</item>");
		}
		if(!onlyFolders){
    		for (Document document : documents) {
    			w.write("<item ");
    			String prefix = "d";
    			if (Document._TYPE_FILE.equals(document.getType())) {
    				String name = XmlUtils.escapeXml(document.getName());
    				if (document.getTypeExtended() != null && !document.getTypeExtended().isEmpty()) {
    					name += "." + document.getTypeExtended();
    				}
    				w.write("text=\"" + name + "\" ");
    
    				prefix += "fl";
    				w.write("im0=\"leaf.gif\" im1=\"leaf.gif\" im2=\"leaf.gif\" ");
    			}
    
    			w.write("id=\"" + prefix + document.getId() + "\" ");
    
    			w.write(">");
    			w.write("</item>");
    		}
		}
		if (project != null) {
			w.write("</item>");
		}
		w.write("</tree>");
		w.flush();
		os.flush();
		os.close();
		return null;
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}
}
