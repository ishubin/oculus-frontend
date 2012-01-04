package net.mindengine.oculus.frontend.web.controllers.issue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.issue.Issue;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class IssueCreateController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private IssueDAO issueDAO;
	private UserDAO userDAO;
	private CustomizationDAO customizationDAO;
	private String chooseProjectView;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("projectId") == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("projects", projectDAO.getRootProjects());
			return new ModelAndView(chooseProjectView, map);
		}
		return super.handleRequest(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap<String, Object>();
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Long rootId = projectDAO.getProjectRootId(projectId, 10);
		map.put("rootProjectId", rootId);
		map.put("projectId", projectId);
		map.put("project", projectDAO.getProject(projectId));
		if (rootId == projectId && rootId > 0) {
			map.put("subprojects", projectDAO.getSubprojects(projectId));
		}
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, 0L, Customization.UNIT_ISSUE));
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Issue issue = new Issue();
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		issue.setProjectId(projectId);
		issue.setDate(new Date());
		issue.setFixed(0);
		issue.setFixedDate(new Date());
		return issue;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();

		Issue issue = (Issue) command;

		issue.setDate(new Date());
		issue.setFixed(0);
		issue.setAuthorId(user.getId());
		if (issue.getSubProjectId() == null) {
			issue.setSubProjectId(0L);
		}
		Long issueId = issueDAO.createIssue(issue);

		issue.setId(issueId);
		updateIssueCustomizationValues(request, issue);

		return new ModelAndView("redirect:../issue/display?id=" + issue.getId());
	}

	public void updateIssueCustomizationValues(HttpServletRequest request, Issue issue) throws Exception {
		Long rootId = projectDAO.getProjectRootId(issue.getProjectId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, issue.getId(), Customization.UNIT_ISSUE, customizationDAO, request);
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public IssueDAO getIssueDAO() {
		return issueDAO;
	}

	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public String getChooseProjectView() {
		return chooseProjectView;
	}

	public void setChooseProjectView(String chooseProjectView) {
		this.chooseProjectView = chooseProjectView;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
