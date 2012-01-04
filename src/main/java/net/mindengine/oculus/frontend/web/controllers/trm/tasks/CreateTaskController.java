package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class CreateTaskController extends SecureSimpleFormController {
	private TrmDAO trmDAO;
	private ProjectDAO projectDAO;
	
	@SuppressWarnings("unchecked")
    @Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
	    Map map = new HashMap();
	    map.put("projects", projectDAO.getRootProjects());
	    return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		TrmTask task = (TrmTask) command;
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();
		task.setUserId(user.getId());

		long taskId = trmDAO.saveTask(task);
		return new ModelAndView(new RedirectView("../test-run-manager/edit-task?id=" + taskId));
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
