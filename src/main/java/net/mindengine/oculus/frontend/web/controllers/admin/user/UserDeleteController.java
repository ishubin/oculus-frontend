package net.mindengine.oculus.frontend.web.controllers.admin.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class UserDeleteController extends SecureSimpleViewController {
	private UserDAO userDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		String sid = request.getParameter("id");
		Long id = new Long(sid);

		userDAO.deleteUser(id);

		Session session = Session.create(request);
		session.setTemporaryMessageId("user.delete.successful");

		return super.handleController(request);
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
