/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
			MailUtils.postMail(config.getMailSmtpHost(), config.getMailSmtpPort(), new String[] { user.getEmail() }, "Oculus Password Recovery", msg, config.getMailSenderName());
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
