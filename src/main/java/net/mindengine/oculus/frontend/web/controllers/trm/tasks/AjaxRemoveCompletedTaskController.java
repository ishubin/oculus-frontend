package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

public class AjaxRemoveCompletedTaskController extends SimpleAjaxController {
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Session session = Session.create(request);
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		ClientServerRemoteInterface server = config.getTRMServer();
		TaskInformation task = server.getTask(taskId);

		if (user.getId().equals(task.getTaskUser().getId())) {
			AjaxModel model = new AjaxModel();
			server.removeCompletedTask(taskId);
			model.setObject(taskId);
			model.setResult("removed");
			return model;
		}
		else
			throw new NotAuthorizedException();
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
