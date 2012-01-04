package net.mindengine.oculus.frontend.web.controllers.trm.customize;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import net.mindengine.jeremy.bin.RemoteFile;
import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.trm.TrmUploadProject;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

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
		ClientServerRemoteInterface server = config.getTRMServer();

		/*
		 * Uploading project to server
		 */
		User user = getUser(request);
		RemoteFile remoteFile = new RemoteFile();
		remoteFile.setBytes(uploadProject.getZippedFile());
		server.uploadProject(project.getPath(), uploadProject.getVersion(), remoteFile, user.getName());

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
