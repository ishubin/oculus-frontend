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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.user.Permission;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.web.Auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONValue;

public class SimpleAjaxController implements Controller {
	protected Log logger = LogFactory.getLog(getClass());

	private Boolean authorizationCheck = false;
	private List<Permission> permissions;

	/**
	 * Returns the authorized user bean
	 * 
	 * @param request
	 * @return
	 */
	public User getUser(HttpServletRequest request) {
		return Auth.getUserFromRequest(request);
	}
	
	public User getAuthorizedUser(HttpServletRequest request) throws NotAuthorizedException {
        return Auth.getAuthorizedUser(request);
    }

	public void verifyPermissions(HttpServletRequest request) throws Exception {
		User user = getUser(request);
		if (authorizationCheck || permissions != null) {
			if (user == null)
				throw new NotAuthorizedException("User is not authorized");
			if (permissions != null) {
				if (!user.hasAllPermissions(permissions))
					throw new PermissionDeniedException("Permission denied");
			}
		}
	}

	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		return null;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxModel model = null;
		try {
			model = handleController(request);
			if (model == null)
				throw new Exception("The controller returned null model");
		}
		catch (Exception ex) {
			logger.error(ex, ex);
			model = new AjaxModel(ex);
		}

		OutputStream os = response.getOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(os);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");

		JSONValue jsonValue = JSONMapper.toJSON(model);
		w.write(jsonValue.render(true));

		w.flush();
		os.flush();
		os.close();

		return null;
	}

	public Boolean getAuthorizationCheck() {
		return authorizationCheck;
	}

	public void setAuthorizationCheck(Boolean authorizationCheck) {
		this.authorizationCheck = authorizationCheck;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
