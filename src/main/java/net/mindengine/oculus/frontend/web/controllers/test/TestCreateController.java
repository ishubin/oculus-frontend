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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class TestCreateController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private TestDAO testDAO;
	private CustomizationDAO customizationDAO;
	private UserDAO userDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Test test = new Test();
		test.setGroupId(0L);
		if (request.getParameter("groupId") != null) {
			test.setGroupId(Long.parseLong(request.getParameter("groupId")));
		}
		test.setContent("");
		return test;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		String projectId = request.getParameter("projectId");
		if (projectId != null && !projectId.isEmpty()) {
			Project project = projectDAO.getProject(Long.parseLong(projectId));
			Project parentProject = projectDAO.getProject(project.getParentId());
			map.put("project", project);
			map.put("parentProject", parentProject);
			map.put("testPanelTitle", "Create Test");
			map.put("testCommand", "Create");
			Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
			map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, 0L, Customization.UNIT_TEST));

			map.put("groups", testDAO.getProjectTestGroups(Long.parseLong(projectId)));
		}

		if (request.getParameter("groupId") != null) {
			Long groupId = Long.parseLong(request.getParameter("groupId"));
			if (groupId > 0) {
				map.put("group", testDAO.getTestGroup(groupId));
			}
		}
		map.put("title", getTitle());
		return map;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = getUser(request);
		user.verifyPermission("test_managment");

		Test test = (Test) command;
		test.setAuthorId(user.getId());
		Long id = testDAO.create(test);

		test.setId(id);

		updateTestCustomizationValues(request, test);
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView("../test/display?id=" + id));
		return mav;
	}

	public void updateTestCustomizationValues(HttpServletRequest request, Test test) throws Exception {
		Long rootId = projectDAO.getProjectRootId(test.getProjectId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, test.getId(), Customization.UNIT_TEST, customizationDAO, request);
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
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
}
