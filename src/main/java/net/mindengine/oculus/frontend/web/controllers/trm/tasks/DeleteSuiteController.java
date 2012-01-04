package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Used for removing suite from the task. Uses "id" post parameter for fetching
 * suite Redirects to my tasks page.
 * 
 * @author Ivan Shubin
 * 
 */
public class DeleteSuiteController extends SecureSimpleViewController {
	private TrmDAO trmDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();
		Long suiteId = Long.parseLong(request.getParameter("id"));
		TrmSuite suite = trmDAO.getSuite(suiteId);
		if (suite == null)
			throw new UnexistentResource("The suite doesn't exist");

		TrmTask task = trmDAO.getTask(suite.getTaskId());

		/*
		 * Verifying that the task which contain current suite belongs to logged
		 * user
		 */
		if (!task.getUserId().equals(user.getId())) {
			throw new NotAuthorizedException("You are not authorized to edit tasks of other users");
		}

		trmDAO.deleteSuite(suiteId);
		return new ModelAndView(new RedirectView("../test-run-manager/edit-task?id=" + task.getId()));
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
