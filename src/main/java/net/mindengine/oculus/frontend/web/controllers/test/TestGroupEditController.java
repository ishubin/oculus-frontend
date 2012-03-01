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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

public class TestGroupEditController extends SecureSimpleFormController {

	private ProjectDAO projectDAO;
	private TestDAO testDAO;

	@SuppressWarnings( { "rawtypes", "unchecked" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> map = super.referenceData(request);
		TestGroup group = testDAO.getTestGroup(Long.parseLong(request.getParameter("groupId")));

		Project project = projectDAO.getProject(group.getProjectId());
		map.put("project", project);
		map.put("parentProject", projectDAO.getProject(project.getParentId()));
		map.put("testGroupTitle", group.getName());
        map.put("testGroupCommand", "Save");
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TestGroup group = testDAO.getTestGroup(Long.parseLong(request.getParameter("groupId")));
		return group;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		verifyPermissions(request);

		TestGroup group = (TestGroup) command;

		testDAO.saveTestGroup(group);

		Project project = projectDAO.getProject(group.getProjectId());
		return new ModelAndView("redirect:../project/display-" + project.getPath() + "?groupId=" + group.getId());
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

}
