package net.mindengine.oculus.frontend.web.controllers.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.ForgotPasswordData;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.utils.MailUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class ForgotPasswordController extends SecureSimpleFormController {
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

		ForgotPasswordData data = (ForgotPasswordData) command;
		User user = userDAO.getUserByEmail(data.getEmail());

		if (user != null) {
			String msg = "";
			msg += "Your account is:\n";
			msg += "Login: " + user.getLogin() + "\n";
			msg += "Password: " + user.getPassword();

			MailUtils.postMail(config.getMailSmtpHost(), new String[] { user.getEmail() }, "Oculus Password Recovery", msg, config.getMailSender());

			ModelAndView mav = new ModelAndView(getSuccessView());
			mav.addObject("forgotPasswordEmail", data.getEmail());
			return mav;
		}
		else {
			ModelAndView mav = new ModelAndView(getFailedView());
			mav.addObject("forgotPassword", data);
			mav.addObject("errorMessage", "User with email '" + data.getEmail() + "' doesn't exist in DB");
			return mav;
		}
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
