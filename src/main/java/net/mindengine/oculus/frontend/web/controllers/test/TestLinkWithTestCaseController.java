package net.mindengine.oculus.frontend.web.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class TestLinkWithTestCaseController extends SecureSimpleViewController {
	private TestDAO testDAO;
	private DocumentDAO documentDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if (user == null)
			throw new NotAuthorizedException();

		if (!user.hasPermission(4))
			throw new NotAuthorizedException("You have no permissions for this operation");

		Long testId = Long.parseLong(request.getParameter("testId"));
		Long testCaseId = Long.parseLong(request.getParameter("testCaseId"));

		Document document = documentDAO.getDocument(testCaseId);
		if (document == null || !"testcase".equals(document.getType()))
			throw new UnexistentResource("The test-case doesn't exist");
		testDAO.linkWithTestCase(testId, testCaseId);

		return new ModelAndView(new RedirectView("../test/display?id=" + testId));
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}
}
