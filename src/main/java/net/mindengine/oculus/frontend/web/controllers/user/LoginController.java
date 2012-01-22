package net.mindengine.oculus.frontend.web.controllers.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.LoginData;
import net.mindengine.oculus.frontend.domain.user.PermissionList;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends SimpleFormController {
	private UserDAO userDAO;
	private PermissionList permissionList;
	private Config config;
	private Log logger = LogFactory.getLog(getClass());

	public PermissionList getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(PermissionList permissionList) {
		this.permissionList = permissionList;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Session session = Session.create(request);
		LoginData loginData = (LoginData) command;

		User user = userDAO.authorizeUser(loginData.getLogin(), loginData.getPassword());
		if (user != null) {
			logger.info("Authorization Allowed for user:[" + user.getId() + "," + user.getEmail() + "]");
			session.authorizeUser(user, permissionList);
			String redirectUrl = "../user/profile-" + user.getLogin();

			ModelAndView mav = new ModelAndView(new RedirectView(redirectUrl));

			return mav;
		}
		else {
			logger.info("Authorization Denied");
			errors.reject(null, "Wrong credentials");
			Map model = errors.getModel();
			ModelAndView mav = new ModelAndView("login", model);
			mav.addObject("login", loginData);
			mav.addObject("title", "Login");
			return mav;
		}
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
