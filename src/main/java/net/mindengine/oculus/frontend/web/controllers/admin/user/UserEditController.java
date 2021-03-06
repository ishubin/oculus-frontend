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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.user.Permission;
import net.mindengine.oculus.frontend.domain.user.PermissionList;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.crypt.BitCrypter;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class UserEditController extends SecureSimpleFormController {
	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long id = new Long(request.getParameter("id"));
		User user = userDAO.getUserById(id);
		if (user == null)
			throw new UnexistentResource("user");
		return user;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		verifyPermissions(request);
		Long id = new Long(request.getParameter("id"));

		if (id.intValue() == 1)
			throw new Exception("Admin account cannot be changed");
		User user = (User) command;

		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		List<Permission> permissions = ((PermissionList) wac.getBean("permissionList")).getPermissions();

		// Getting state of permission checkboxes

		List<Integer> newPermissionCodes = new ArrayList<Integer>();

		for (Permission p : permissions) {
			int code = p.getCode();
			String state = request.getParameter("p_" + code);
			if ("on".equals(state)) {
				newPermissionCodes.add(code);
			}
			user.getClass();
		}
		BitCrypter bitCrypter = new BitCrypter();
		String encryptedPermissions = bitCrypter.encrypt(newPermissionCodes);
		user.setPermissions(encryptedPermissions);
		if (user != null) {
			userDAO.updateUser(id, user);
		}
		return new ModelAndView(new RedirectView("../admin/edit-user?id=" + id));
	}

}
