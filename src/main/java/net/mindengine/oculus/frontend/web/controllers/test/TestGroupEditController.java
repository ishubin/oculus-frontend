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
