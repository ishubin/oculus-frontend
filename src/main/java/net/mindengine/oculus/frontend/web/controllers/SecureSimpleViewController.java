package net.mindengine.oculus.frontend.web.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.user.Permission;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.service.exceptions.RedirectException;
import net.mindengine.oculus.frontend.web.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class SecureSimpleViewController implements Controller {
	private String view;
	private Boolean authorizationCheck = false;
	private List<Permission> permissions;

	private String title = "";
	protected Log logger = LogFactory.getLog(getClass());

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

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		verifyPermissions(request);
		Map<String, Object> model=null;
		
		try{
			model = handleController(request);
			if (model != null) {
				if (!model.containsKey("title")) {
					model.put("title", title);
				}
			}
		}
		catch (RedirectException redirectException) {
			return new ModelAndView(new RedirectView(redirectException.getRedirectUrl()));
		}

		if (view == null)
			throw new Exception("The view is not defined");

		ModelAndView mav = new ModelAndView(view);
		if (model != null)
			mav.addAllObjects(model);

		return mav;
	}

	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		return null;
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

	public String getMandatoryStringParameter(HttpServletRequest request, String parameterName) throws InvalidRequest {
		String value = request.getParameter(parameterName);
		if (value == null)
			throw new InvalidRequest();
		return value;
	}

	public Long getMandatoryLongParameter(HttpServletRequest request, String parameterName) throws InvalidRequest {
		String value = request.getParameter(parameterName);
		if (value == null)
			throw new InvalidRequest();
		return Long.parseLong(value);
	}

	public Boolean getMandatoryCheckboxParameter(HttpServletRequest request, String parameterName) throws InvalidRequest {
		String value = request.getParameter(parameterName);
		if (value == null)
			throw new InvalidRequest();
		if (value.toLowerCase().equals("on"))
			return true;
		else
			return false;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public Boolean getAuthorizationCheck() {
		return authorizationCheck;
	}

	public void setAuthorizationCheck(Boolean authorizationCheck) {
		this.authorizationCheck = authorizationCheck;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
