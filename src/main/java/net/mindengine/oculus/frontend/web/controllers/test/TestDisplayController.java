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
package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.comment.Comment;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.document.testcase.Testcase;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.service.comment.CommentDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class TestDisplayController extends SecureSimpleViewController {
	private TestDAO testDAO;
	private ProjectDAO projectDAO;
	
	private UserDAO userDAO;
	private CommentDAO commentDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long id = null;
		try {
			id = Long.parseLong(request.getParameter("id"));
		}
		catch (Exception e) {
			throw new UnexistentResource("The id of the test is not specified");
		}

		Test test = testDAO.getTest(id);
		if (test == null)
			throw new UnexistentResource(Test.class, id);

		map.put("testAuthor", userDAO.getUserById(test.getAuthorId()));

		Project project = projectDAO.getProject(test.getProjectId());
		Project parentProject = projectDAO.getProject(project.getParentId());
		List<TestParameter> testInputParameters = testDAO.getTestInputParameters(id);
		List<TestParameter> testOutputParameters = testDAO.getTestOutputParameters(id);

		map.put("testInputParameters", testInputParameters);
		map.put("testInputParametersCount", testInputParameters.size());

		map.put("testOutputParameters", testOutputParameters);
		map.put("testOutputParametersCount", testOutputParameters.size());

		map.put("test", test);
		map.put("project", project);
		map.put("parentProject", parentProject);
		
		map.put("testContent", Testcase.parse(test.getContent()));

		/*
		 * Loading comments
		 */
		Integer page = 1;
		if (request.getParameter("commentsPage") != null) {
			page = Integer.parseInt(request.getParameter("commentsPage"));
		}
		map.put("comments", commentDAO.getComments(test.getId(), Comment.UNIT_TEST, page, 20));

		/*
		 * Loading customization
		 */

		Long rootId = projectDAO.getProjectRootId(test.getProjectId(), 10);
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, test.getId(), Customization.UNIT_TEST));
		map.put("title", getTitle() + test.getName());
		return map;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}
}
