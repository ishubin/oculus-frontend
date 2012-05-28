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
package net.mindengine.oculus.frontend.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.mindengine.oculus.frontend.domain.user.User;

/**
 * This class is used for translating all session variables to a model which is
 * used in JSP via pageContext property. It should be called in each jsp
 * template where the session variable should be used
 * 
 * @author Ivan Shubin
 * 
 */
public class SessionViewHandler {
	public static void createSessionModel(PageContext pageContext, HttpServletRequest request) {
		String temporaryMessage = null;
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			Session session = Session.create(httpSession);
			temporaryMessage = session.getTemporaryMessage();
		}
		
		User user = Auth.getUserFromRequest(request);
		pageContext.setAttribute("user", user);

		pageContext.setAttribute("temporaryMessage", temporaryMessage);

	}
}
