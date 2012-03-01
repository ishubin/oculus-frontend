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
package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class TestGroupCreateController extends SecureSimpleFormController {

	private ProjectDAO projectDAO;
	private TestDAO testDAO;

	@SuppressWarnings( { "rawtypes", "unchecked" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> map = super.referenceData(request);

		Long projectId = Long.parseLong(request.getParameter("projectId"));

		Project project = projectDAO.getProject(projectId);
		map.put("project", project);
		map.put("parentProject", projectDAO.getProject(project.getParentId()));
		map.put("testGroupTitle", "Create Test Group");
		map.put("testGroupCommand", "Create");
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		TestGroup group = new TestGroup();
		group.setProjectId(Long.parseLong(request.getParameter("projectId")));
		return group;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		verifyPermissions(request);

		TestGroup group = (TestGroup) command;

		Long groupId = testDAO.createTestGroup(group);

		Project project = projectDAO.getProject(group.getProjectId());
		return new ModelAndView("redirect:../project/display-" + project.getPath() + "?groupId=" + groupId);
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
