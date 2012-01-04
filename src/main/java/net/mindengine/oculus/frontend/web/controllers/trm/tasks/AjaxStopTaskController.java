package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.domain.task.TaskUser;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * Stops the specified task.
 * 
 * @author Ivan Shubin
 */
public class AjaxStopTaskController extends SimpleAjaxController {
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		User user = Session.create(request).getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		Long taskId = Long.parseLong(request.getParameter("taskId"));
		AjaxModel model = new AjaxModel();
		ClientServerRemoteInterface server = config.getTRMServer();
		/*
		 * Checking that the task was run by this user.
		 */
		TaskInformation task = server.getTask(taskId);
		TaskUser taskUser = task.getTaskUser();
		if (user.getId().equals(taskUser.getId())) {
			server.stopTask(taskId);
		}
		else
			throw new NotAuthorizedException("You are not allowed to stop tasks of other users");

		model.setObject(taskId);
		return model;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

}
