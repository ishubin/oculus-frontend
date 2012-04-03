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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.document.testcase.Testcase;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class TestEditController extends SecureSimpleFormController {
	private TestDAO testDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private CustomizationDAO customizationDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long id = new Long(request.getParameter("id"));
		Test test = testDAO.getTest(id);
		test.setInputParameters(testDAO.getTestInputParameters(id));
		test.setOutputParameters(testDAO.getTestOutputParameters(id));
		return test;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Long id = new Long(request.getParameter("id"));
		Test test = testDAO.getTest(id);
		Map map = new HashMap();
		Project project = projectDAO.getProject(test.getProjectId());

		map.put("project", project);

		/*
		 * Fetching customizations
		 */
		Long rootId = projectDAO.getProjectRootId(project.getId(), 10);
		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, test.getId(), Customization.UNIT_TEST));
		map.put("title", getTitle());
		map.put("testPanelTitle", "Edit Test");
		map.put("testCommand", "Save");

		if (test.getGroupId() > 0) {
			map.put("group", testDAO.getTestGroup(test.getGroupId()));
		}

		map.put("groups", testDAO.getProjectTestGroups(test.getProjectId()));
		
		
		map.put("testContentJson", exportTestContentToJson(test.getContent()));
		return map;
	}

	private String exportTestContentToJson(String content) throws Exception {
        Testcase testcase = Testcase.parse(content);
        
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, testcase);
        return sw.toString();
    }

    @Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		verifyPermissions(request);
		Long id = new Long(request.getParameter("id"));
		User user = getUser(request);
		user.verifyPermission("test_managment");

		Test test = (Test) command;
		testDAO.updateTest(id, test);
		updateTestCustomizationValues(request, test);

		return new ModelAndView(new RedirectView("../test/edit?id=" + id));
	}

	public void updateTestCustomizationValues(HttpServletRequest request, Test test) throws Exception {
		Long rootId = projectDAO.getProjectRootId(test.getProjectId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, test.getId(), Customization.UNIT_TEST, customizationDAO, request);
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
