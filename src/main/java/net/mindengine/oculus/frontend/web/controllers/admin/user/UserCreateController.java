package net.mindengine.oculus.frontend.web.controllers.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.utils.MailUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class UserCreateController extends SecureSimpleFormController {
	private UserDAO userDAO;
	private Config config;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = (User) command;
		if (user.getPassword() == null)
			user.setPassword("");
		userDAO.createUser(user);

		String msg = "";
		msg += "Your account is:\n";
		msg += "Login: " + user.getLogin() + "\n";
		msg += "Password: " + user.getPassword();

		boolean isMailSent = true;
		String mailExceptionMessage = "";
		try {
			MailUtils.postMail(config.getMailSmtpHost(), new String[] { user.getEmail() }, "Oculus Password Recovery", msg, config.getMailSender());
		}
		catch (Exception e) {
			e.printStackTrace();
			isMailSent = false;
			mailExceptionMessage = e.getMessage();
		}

		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("createdUser", user);
		mav.addObject("isMailSent", isMailSent);
		mav.addObject("mailExceptionMessage", mailExceptionMessage);
		return mav;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

}
