package net.mindengine.oculus.frontend.web.controllers.project.build;

import java.util.Date;
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
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class BuildCreateController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private BuildDAO buildDAO;
	private CustomizationDAO customizationDAO;

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Map map = new HashMap();
		Project project = projectDAO.getProject(projectId);

		map.put("project", project);

		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, null, rootId, 0L, Customization.UNIT_BUILD));
		map.put("title", getTitle());
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Project project = projectDAO.getProject(projectId);

		Build build = (Build) command;
		build.setProjectId(Long.parseLong(request.getParameter("projectId")));

		build.setDate(new Date());
		Long buildId = buildDAO.createBuild(build);

		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, buildId, Customization.UNIT_BUILD, customizationDAO, request);

		return new ModelAndView(new RedirectView("../project/build-display?id=" + buildId));
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

}
