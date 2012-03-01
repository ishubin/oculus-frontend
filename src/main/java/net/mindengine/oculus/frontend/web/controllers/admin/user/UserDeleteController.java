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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.admin.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class UserDeleteController extends SecureSimpleViewController {
	private UserDAO userDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		String sid = request.getParameter("id");
		Long id = new Long(sid);

		userDAO.deleteUser(id);

		Session session = Session.create(request);
		session.setTemporaryMessage("User was removed successfully");

		return super.handleController(request);
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
