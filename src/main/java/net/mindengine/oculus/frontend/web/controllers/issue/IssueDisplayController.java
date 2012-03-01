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
package net.mindengine.oculus.frontend.web.controllers.issue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.issue.Issue;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class IssueDisplayController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private IssueDAO issueDAO;
	private UserDAO userDAO;
	private CustomizationDAO customizationDAO;
	private CommentDAO commentDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long issueId = Long.parseLong(request.getParameter("id"));
		Issue issue = issueDAO.getIssue(issueId);
		if (issue == null)
			throw new UnexistentResource("The issue doesn't exist");

		boolean displayDependentTests = "true".equals(request.getParameter("displayDependentTests"));

		/*
		 * Loading comments
		 */
		Integer page = 1;
		if (request.getParameter("commentsPage") != null) {
			page = Integer.parseInt(request.getParameter("commentsPage"));
		}
		map.put("comments", commentDAO.getComments(issue.getId(), Comment.UNIT_ISSUE, page, 20));

		/*
		 * Loading customization
		 */
		if (issue.getProjectId() > 0) {
			map.put("project", getProjectDAO().getProject(issue.getProjectId()));
			Long rootId = projectDAO.getProjectRootId(issue.getProjectId(), 10);
			map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, issue.getId(), Customization.UNIT_ISSUE));
		}
		map.put("issue", issue);
		if (issue.getSubProjectId() > 0) {
			map.put("subproject", getProjectDAO().getProject(issue.getSubProjectId()));
		}
		if (issue.getAuthorId() > 0) {
			map.put("author", userDAO.getUserById(issue.getAuthorId()));
		}

		if (displayDependentTests) {
			map.put("collations", issueDAO.getIssueCollations(issueId));
		}
		map.put("title", getTitle() + issue.getName());
		return map;
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

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}
}
