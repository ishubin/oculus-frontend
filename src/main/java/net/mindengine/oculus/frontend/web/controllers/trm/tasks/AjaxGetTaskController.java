package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * Fetches all suites which are used in task
 * 
 * @author Ivan Shubin
 * 
 */
public class AjaxGetTaskController extends SimpleAjaxController {
	private Config config;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		AjaxModel model = new AjaxModel();

		ClientServerRemoteInterface server = config.getTRMServer();

		TaskInformation serverTask = server.getTask(taskId);
		
		Map<String, Object> task = new HashMap<String, Object>();
		task.put("name", serverTask.getTaskName());
		task.put("id", serverTask.getTaskId());
		task.put("status", serverTask.getTaskStatus());
		task.put("createdDate", serverTask.getCreatedDate());
		task.put("startedDate", serverTask.getStartedDate());
		task.put("completedDate", serverTask.getCompletedDate());

		if (serverTask.getTaskStatus().getSuiteInformation()==null) {
			task.put("type", "MultiTask");
			List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
			
			TaskInformation[] serverChildTasks = server.getTasks(taskId);
			
			for (TaskInformation serverChildTask : serverChildTasks) {
				Map<String, Object> childTask = new HashMap<String, Object>();
				childTask.put("name", serverChildTask.getTaskName());
				childTask.put("id", serverChildTask.getTaskId());
				childTask.put("status", serverChildTask.getTaskStatus());

				childTask.put("type", "SuiteTask");
				children.add(childTask);
			}

			task.put("children", children);
		}

		model.setObject(task);
		model.setResult("done");
		return model;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

}
