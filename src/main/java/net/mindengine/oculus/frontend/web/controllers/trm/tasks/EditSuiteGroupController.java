package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

public class EditSuiteGroupController extends SecureSimpleFormController {

	private TrmDAO trmDAO;
	private ProjectDAO projectDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TrmSuiteGroup group = trmDAO.getSuiteGroup(groupId);
		if (group == null)
			throw new UnexistentResource("This group doesn't exist");
		return group;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map map = new HashMap<String, Object>();

		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TrmSuiteGroup group = trmDAO.getSuiteGroup(groupId);

		map.put("task", trmDAO.getTask(group.getTaskId()));
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		verifyPermissions(request);

		TrmSuiteGroup group = (TrmSuiteGroup) command;
		trmDAO.updateSuiteGroup(group);

		return new ModelAndView("redirect:../test-run-manager/edit-suite-group?groupId=" + group.getId());
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
