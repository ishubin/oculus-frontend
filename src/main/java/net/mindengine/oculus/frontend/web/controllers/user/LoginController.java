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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.user.LoginData;
import net.mindengine.oculus.frontend.domain.user.PermissionList;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Auth;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends SecureSimpleFormController {
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
		LoginData loginData = (LoginData) command;

		User user = userDAO.authorizeUser(loginData.getLogin(), loginData.getPassword());
		if (user != null) {
			logger.info("Authorization Allowed for user:[" + user.getId() + "," + user.getEmail() + "]");
			user.updatePermissions(getPermissionList());
			Auth.setUserCookieToResponse(response, user);
			
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
