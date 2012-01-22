package net.mindengine.oculus.frontend.web.controllers.project;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class ProjectDeleteController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		String sid = request.getParameter("id");
		Long id = new Long(sid);

		Project project = projectDAO.getProject(id);
		if (project.getParentId() == 0) {

			/*
			 * Removing all subprojects
			 */
			List<Project> subprojects = projectDAO.getSubprojects(id);
			for (Project subproject : subprojects) {
				projectDAO.deleteProject(subproject.getId());

				/*
				 * Removing all unit customization values
				 */
				List<Customization> customizations = customizationDAO.getCustomizations(project.getId(), Customization.UNIT_PROJECT);
				customizationDAO.removeAllUnitCustomizationValues(subproject.getId(), customizations);
			}
		}
		else if (project.getParentId() > 0) {
			/*
			 * Removing all unit customization values
			 */
			Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
			List<Customization> customizations = customizationDAO.getCustomizations(rootId, Customization.UNIT_PROJECT);
			customizationDAO.removeAllUnitCustomizationValues(project.getId(), customizations);
		}

		projectDAO.deleteProject(id);

		Session session = Session.create(request);
		session.setTemporaryMessage("Project \""+project.getName()+"\" was removed successfully");
		return null;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

}
