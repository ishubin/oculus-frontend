package net.mindengine.oculus.frontend.web.controllers.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.ChangePasswordData;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ChangePasswordController extends SecureSimpleFormController {
	private UserDAO userDAO;
	private Config config;

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = getUser(request);
		ChangePasswordData cpData = (ChangePasswordData) command;
		if (user == null)
			throw new NotAuthorizedException();

		User userDB = userDAO.getUserById(user.getId());

		if (cpData.getOldPassword().equals(userDB.getPassword())) {
			/*
			 * changing the users password in DB
			 */
			userDAO.changeUserPassword(user.getId(), cpData.getNewPassword());
			Session session = Session.create(request);
			session.setTemporaryMessage("Your password was successfuly changed");
			return new ModelAndView(new RedirectView("../user/profile-" + user.getLogin()));
		}
		else
			errors.reject("change.password.enter.old.password");

		Map model = errors.getModel();
		model.put("changePassword", cpData);
		ModelAndView mav = new ModelAndView(getFormView(), model);
		return mav;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
