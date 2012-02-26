package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class DocumentProjectController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getPathInfo().substring(9);
		Project project = projectDAO.getProjectByPath(path);
		String strFileId = request.getParameter("fileId");
		String openDocumentFunction = "";
		if (strFileId != null) {
			Long documentId = Long.parseLong(strFileId);
			openDocumentFunction = "loadFileDocument(" + documentId + ");";
		}

		if (project == null)
			throw new UnexistentResource("Project with name '" + path + "' doesn't exist");

		ModelAndView mav = new ModelAndView(getView());
		mav.addObject("project", project);
		mav.addObject("title", getTitle() + " - " + project.getName());
		mav.addObject("bodyOnLoad", "loadFolderTree(" + project.getId() + ");" + openDocumentFunction);
		return mav;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}
}
