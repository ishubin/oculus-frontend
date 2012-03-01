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
package net.mindengine.oculus.frontend.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.user.Permission;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.web.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class SecureSimpleFormController extends SimpleFormController {
	private List<Permission> permissions = null;
	private List<Permission> permissionsSelective = null;

	private Boolean authorizationCheck = false;
	private String failedView;

	private String title = "";
	protected Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap<String, Object>();

		map.put("title", title);

		Map refMap = super.referenceData(request);
		if(refMap!=null){
			map.putAll(refMap);
		}
		return map;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		return super.handleRequest(request, response);
	}

	/**
	 * Returns the authorized user bean
	 * 
	 * @param request
	 * @return
	 */
	public User getUser(HttpServletRequest request) {
		Session session = Session.create(request);
		return session.getAuthorizedUser();
	}

	protected void verifyPermissions(HttpServletRequest request) throws Exception {
		User user = getUser(request);
		if (authorizationCheck) {
			if (user == null)
				throw new NotAuthorizedException("User is not authorized");
		}
		if (authorizationCheck && permissions != null) {
			if (permissions != null) {
				if (!user.hasAllPermissions(permissions))
					throw new PermissionDeniedException("Permission denied");
			}
		}
		if (authorizationCheck && permissionsSelective != null) {
			if (permissions != null) {
				if (!user.hasSelectivePermissions(permissions))
					throw new PermissionDeniedException("Permission denied");
			}
		}
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getFailedView() {
		return failedView;
	}

	public void setFailedView(String failedView) {
		this.failedView = failedView;
	}

	public Boolean getAuthorizationCheck() {
		return authorizationCheck;
	}

	public void setAuthorizationCheck(Boolean authorizationCheck) {
		this.authorizationCheck = authorizationCheck;
	}

	public List<Permission> getPermissionsSelective() {
		return permissionsSelective;
	}

	public void setPermissionsSelective(List<Permission> permissionsSelective) {
		this.permissionsSelective = permissionsSelective;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
