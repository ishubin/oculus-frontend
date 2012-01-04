package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class SharedTaskController extends SecureSimpleViewController {

	private TrmDAO trmDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long taskId = Long.parseLong(request.getParameter("id"));
		TrmTask task = trmDAO.getTask(taskId);

		if (task == null || !task.getShared())
			throw new UnexistentResource("The task with id " + taskId + " doesn't exist");

		List<TrmSuite> suites = trmDAO.getTaskSuites(taskId);
		map.put("task", task);
		map.put("suites", suites);
		return map;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

}
