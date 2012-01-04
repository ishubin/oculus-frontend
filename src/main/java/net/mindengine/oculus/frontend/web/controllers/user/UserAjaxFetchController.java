package net.mindengine.oculus.frontend.web.controllers.user;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class UserAjaxFetchController extends SimpleAjaxController {
	private UserDAO userDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Integer page = Integer.parseInt(request.getParameter("page"));
		String name = request.getParameter("name");
		AjaxModel model = new AjaxModel();

		model.setObject(userDAO.fetchUsers(name, page, 10));
		return model;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
}
