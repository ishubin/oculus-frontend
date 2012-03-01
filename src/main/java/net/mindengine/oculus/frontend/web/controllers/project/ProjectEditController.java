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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.experior.utils.FileUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ProjectEditController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private CustomizationDAO customizationDAO;
	private Config config;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long id = new Long(request.getParameter("id"));
		Project project = projectDAO.getProject(id);

		if (project == null)
			throw new UnexistentResource("project");
		return project;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();

		Long projectId = new Long(request.getParameter("id"));
		Long rootId = projectDAO.getProjectRootId(projectId, 5);
		map.put("dataFolder", config.getDataFolder());

		/*
		 * Checking whether this project is root or not The following code is
		 * only for sub-projects
		 */
		if (!projectId.equals(rootId)) {
			map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, projectId, Customization.UNIT_PROJECT));
		}
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		verifyPermissions(request);

		User user = getUser(request);

		Project project = (Project) command;
		Long id = new Long(request.getParameter("id"));
		if (project.getParentId() != null && project.getParentId() > 0) {
			user.verifyPermission("subproject_managment");
		}
		else
			user.verifyPermission("project_managment");

		// Handling the icon uploading

		MultipartFile multipartFile = project.getIconFile();
		if (multipartFile != null) {
			if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".png")) {
				Project oldProject = projectDAO.getProject(id);
				// Deleting old icon
				if (oldProject.getIcon() != null && !oldProject.getIcon().isEmpty()) {
					String path = config.getDataFolder() + File.separator + "projects" + File.separator + id + File.separator + "icon_" + oldProject.getIcon() + ".png";
					File file = new File(path);
					file.delete();
				}

				Date date = new Date();
				String path = config.getDataFolder() + File.separator + "projects" + File.separator + id;
				FileUtils.mkdirs(path);
				File file = new File(path + File.separator + "icon_" + date.getTime() + ".png");
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(multipartFile.getBytes());
				fos.close();
				project.setIcon(Long.toString(date.getTime()));
			}
		}

		projectDAO.updateProject(id, project);
		updateProjectCustomizationValues(request, project);

		return new ModelAndView(new RedirectView("../project/edit?id=" + id));
	}

	public void updateProjectCustomizationValues(HttpServletRequest request, Project project) throws Exception {
		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, project.getId(), Customization.UNIT_PROJECT, customizationDAO, request);
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
}
