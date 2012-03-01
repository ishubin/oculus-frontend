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
package net.mindengine.oculus.frontend.web.controllers.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class ProjectDisplayController extends SecureSimpleViewController {

	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;
	private TestDAO testDAO;
	private UserDAO userDAO;
	private CommentDAO commentDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String path = request.getPathInfo().substring(9);

		Long groupId = 0L;
		if (request.getParameter("groupId") != null) {
			groupId = Long.parseLong(request.getParameter("groupId"));
		}

		Project project = projectDAO.getProjectByPath(path);
		if (project == null)
			throw new UnexistentResource(path);

		if (project.getSubprojectsCount() > 0) {
			List<Project> subprojects = projectDAO.getSubprojects(project.getId());
			map.put("subprojects", subprojects);
		}
		if (project.getTestsCount() > 0) {
			List<Test> tests = testDAO.getTestsByProjectId(project.getId(), groupId);
			map.put("tests", tests);
		}

		if (project.getParentId() > 0) {
			List<TestGroup> groups = testDAO.getProjectTestGroups(project.getId());
			map.put("groups", groups);
		}

		if (groupId > 0) {
			map.put("group", testDAO.getTestGroup(groupId));
		}

		if (project.getParentId() > 0) {
			Project parentProject = projectDAO.getProject(project.getParentId());
			map.put("parentProject", parentProject);
		}

		map.put("project", project);
		map.put("projectAuthor", userDAO.getUserById(project.getAuthorId()));
		map.put("testsCount", project.getTestsCount());
		map.put("subprojectsCount", project.getSubprojectsCount());

		/*
		 * Loading comments
		 */
		Integer page = 1;
		if (request.getParameter("commentsPage") != null) {
			page = Integer.parseInt(request.getParameter("commentsPage"));
		}
		map.put("comments", commentDAO.getComments(project.getId(), Comment.UNIT_PROJECT, page, 20));

		/*
		 * Loading customization
		 */
		if (project.getParentId() > 0) {
			Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
			map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, project.getId(), Customization.UNIT_PROJECT));
		}
		map.put("title", getTitle() + project.getName());
		return map;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
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
