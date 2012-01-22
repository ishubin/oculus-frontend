package net.mindengine.oculus.frontend.web.controllers.project.build;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class BuildEditController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private BuildDAO buildDAO;
	private CustomizationDAO customizationDAO;
	private UserDAO userDAO;

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Build build = (Build) command;
		Map map = new HashMap();
		Project project = projectDAO.getProject(build.getProjectId());

		map.put("project", project);
		Long buildId = Long.parseLong(request.getParameter("id"));

		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, buildId, Customization.UNIT_BUILD));
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return buildDAO.getBuild(Long.parseLong(request.getParameter("id")));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Build build = (Build) command;
		Project project = projectDAO.getProject(build.getProjectId());

		build.setId(Long.parseLong(request.getParameter("id")));

		if (buildDAO.getBuildByNameAndProject(build.getName(), build.getProjectId()) != null) {
			errors.reject("Build with such name already exists");
			Map model = errors.getModel();
			model.put("project", project);
			return new ModelAndView(getFormView(), model);
		}

		buildDAO.updateBuild(build);

		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, build.getId(), Customization.UNIT_BUILD, customizationDAO, request);

		return new ModelAndView(new RedirectView("../project/build-display?id=" + build.getId()));
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
