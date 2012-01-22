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
