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
package net.mindengine.oculus.frontend.web.controllers.trm.customize;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.trm.TrmUploadProject;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

import org.apache.commons.io.FileUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

public class UploadProjectController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private Config config;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		TrmUploadProject uploadProject = (TrmUploadProject) command;
		if ("Current Version".equals(uploadProject.getVersion())) {
			uploadProject.setVersion("current");
		}

		if (uploadProject.getZippedFile() == null || uploadProject.getZippedFile().length == 0) {
			errors.reject("trm.uploadproject.error.file.empty");
			Map model = errors.getModel();
			model.put("uploadProject", uploadProject);
			return new ModelAndView(getFormView(), model);
		}

		Project project = projectDAO.getProject(Long.parseLong(request.getParameter("projectId")));
		if (project == null)
			throw new UnexistentResource("Project with id " + request.getParameter("projectId") + " doesn't exist");
		ClientServerRemoteInterface server = config.getGridServer();

		/*
		 * Uploading project to server
		 */
		User user = getUser(request);
		String fileName = UUID.randomUUID().toString().replace("-", "");
        File tmpFile = File.createTempFile(fileName, ".tmp");
        FileUtils.writeByteArrayToFile(tmpFile, uploadProject.getZippedFile());
		server.uploadProject(project.getPath(), uploadProject.getVersion(), tmpFile, user.getName());

		return new ModelAndView(getSuccessView());
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TrmUploadProject command = new TrmUploadProject();
		command.setVersion("Current Version");
		return command;
	}

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap<String, Object>();
		String strProjectId = request.getParameter("projectId");
		if (strProjectId != null) {
			Long projectId = Long.parseLong(strProjectId);
			map.put("project", projectDAO.getProject(projectId));
		}
		return map;
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
}
