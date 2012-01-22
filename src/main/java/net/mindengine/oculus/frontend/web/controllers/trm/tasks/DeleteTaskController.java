package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DeleteTaskController extends SecureSimpleViewController {
	private TrmDAO trmDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		trmDAO.deleteTask(taskId, user.getId());
		session.setTemporaryMessage("Task was removed successfully");

		return null;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}
}
