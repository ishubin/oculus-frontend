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
