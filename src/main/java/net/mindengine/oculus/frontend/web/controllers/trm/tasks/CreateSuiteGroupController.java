package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class CreateSuiteGroupController extends SecureSimpleFormController {

	private TrmDAO trmDAO;
	private ProjectDAO projectDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TrmSuiteGroup group = new TrmSuiteGroup();

		Long taskId = Long.parseLong(request.getParameter("taskId"));
		if (taskId > 0) {
			group.setTaskId(taskId);
			return group;
		}
		else
			throw new InvalidRequest();
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap<String, Object>();
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		TrmTask task = trmDAO.getTask(taskId);

		if (task == null)
			throw new UnexistentResource("The task with id " + taskId + " is not present");
		map.put("task", task);
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		TrmSuiteGroup group = (TrmSuiteGroup) command;
		group.setEnabled(true);
		Long groupId = trmDAO.createSuiteGroup(group);

		return new ModelAndView("redirect:../test-run-manager/edit-task?id=" + group.getTaskId() + "&groupId=" + groupId);
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
