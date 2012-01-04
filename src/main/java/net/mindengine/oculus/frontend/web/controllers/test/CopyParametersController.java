package net.mindengine.oculus.frontend.web.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.test.TestParameter;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class CopyParametersController extends SecureSimpleViewController {
	private TestDAO testDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		Long testId = Long.parseLong(request.getParameter("testId"));

		String parameterIds = request.getParameter("parameterIds");
		if (parameterIds != null && !parameterIds.isEmpty()) {
			String[] ids = parameterIds.split(",");
			for (String id : ids) {
				if (id.startsWith("tp")) {
					Long parameterId = Long.parseLong(id.substring(2));
					TestParameter parameter = testDAO.getParameter(parameterId);
					if (parameter != null) {
						parameter.setId(null);
						parameter.setTestId(testId);
						testDAO.createTestParameter(parameter);
					}
				}
			}
		}

		return new ModelAndView("redirect:../test/display?id=" + testId);
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

}
