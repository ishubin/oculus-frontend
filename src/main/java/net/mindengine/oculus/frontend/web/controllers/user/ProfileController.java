package net.mindengine.oculus.frontend.web.controllers.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleViewController;

public class ProfileController extends SimpleViewController {

	private UserDAO userDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		String login = request.getPathInfo().substring(9);

		User user = userDAO.getUserByLogin(login);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("choosenUser", user);
		return map;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
