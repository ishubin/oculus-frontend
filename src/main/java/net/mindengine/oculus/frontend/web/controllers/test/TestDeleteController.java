package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Deletes the test and redirects to project page
 * 
 * @author Ivan Shubin
 * 
 */
public class TestDeleteController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private TestDAO testDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);
		Long testId = Long.parseLong(request.getParameter("id"));

		Test test = testDAO.getTest(testId);
		Project project = projectDAO.getProject(test.getProjectId());

		testDAO.delete(testId, project.getId());

		Long rootId = projectDAO.getProjectRootId(test.getProjectId(), 10);
		List<Customization> customizations = customizationDAO.getCustomizations(rootId, Customization.UNIT_TEST);
		customizationDAO.removeAllUnitCustomizationValues(test.getId(), customizations);

		Session session = Session.create(request);
		session.setTemporaryMessageId("test.delete.successful");

		return new ModelAndView(new RedirectView("../project/display-" + project.getPath()));
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
}
