package net.mindengine.oculus.frontend.web.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.TestGroup;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class TestGroupDeleteController extends SecureSimpleViewController {

	private TestDAO testDAO;
	private ProjectDAO projectDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		Long groupId = Long.parseLong(request.getParameter("groupId"));
		TestGroup group = testDAO.getTestGroup(groupId);
		if (group == null)
			throw new UnexistentResource("The test group with id " + groupId + " doesn't exist");
		Project project = projectDAO.getProject(group.getProjectId());

		testDAO.deleteTestGroup(groupId);

		if (project != null) {
			return new ModelAndView("redirect:../project/display-" + project.getPath());
		}
		else
			return new ModelAndView("redirect:../display/home");
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
