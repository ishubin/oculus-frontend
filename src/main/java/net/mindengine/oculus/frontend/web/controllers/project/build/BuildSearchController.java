package net.mindengine.oculus.frontend.web.controllers.project.build;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.build.BuildSearchColumn;
import net.mindengine.oculus.frontend.domain.project.build.BuildSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class BuildSearchController extends SecureSimpleFormController {
	private BuildDAO buildDAO;
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;
	private UserDAO userDAO;
	/**
	 * This is a factory that is used to fetch the default list of columns to
	 * display in report table in case if column list wasn't defined previously
	 * in session
	 */
	private ColumnFactory columnFactory;

	private Config config;

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		BuildSearchFilter filter = (BuildSearchFilter) command;
		filter.setColumns(columnFactory.getColumnList());
		Map map = new HashMap<String, Object>();

		map.put("columns", columnFactory.getColumnList());

		if (filter.getProject() != null && filter.getProject().equals("0")) {
			filter.setProject("");
		}
		/*
		 * Collecting customization search criteria parameters
		 */
		filter.setCustomizations(CustomizationUtils.collectCustomizationSearchCriteriaParameters(request));

		if (filter.getProject() != null && !filter.getProject().isEmpty()) {
			if (StringUtils.isNumeric(filter.getProject())) {
				map.put("customizations", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, Long.parseLong(filter.getProject()), Customization.UNIT_BUILD, filter.getCustomizations()));
			}
		}

		map.put("builds", buildDAO.searchBuilds(filter));
		map.put("rootProjects", projectDAO.getRootProjects());
		map.put("columnFactory", columnFactory);
		map.put("searchFilter", filter);
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		return true;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		BuildSearchFilter filter = (BuildSearchFilter) super.formBackingObject(request);
		filter.setPageLimit(1);
		filter.setPageOffset(1);
		filter.setOrderByColumnId(BuildSearchColumn.BUILD_NAME);
		filter.setOrderDirection(-1);
		return filter;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addAllObjects(referenceData(request, command, errors));
		return mav;
	}

	public void setColumnFactory(ColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public ColumnFactory getColumnFactory() {
		return columnFactory;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

}
