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
		else {
			errors.reject(null, "Please enter correct old password");
		}

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
