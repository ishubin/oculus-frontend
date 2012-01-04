package net.mindengine.oculus.frontend.web.controllers.document;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.testcase.Testcase;
import net.mindengine.oculus.frontend.domain.document.testcase.TestcaseStep;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentTestcaseEditController extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private CustomizationDAO customizationDAO;
	private ProjectDAO projectDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		verifyPermissions(request);
		Session s = Session.create(request);
		User user = s.getAuthorizedUser();
		if (user == null)
			throw new NotAuthorizedException();

		AjaxModel model = new AjaxModel();

		Document document = new Document();
		document.setName(request.getParameter("name"));
		document.setDescription(request.getParameter("description"));
		document.setId(Long.parseLong(request.getParameter("id")));

		Long stepsCount = Long.parseLong(request.getParameter("contentSize"));

		Testcase testcase = new Testcase();
		List<TestcaseStep> steps = testcase.getSteps();

		for (long i = 0; i < stepsCount; i++) {
		    String action = request.getParameter("content[" + i+"][action]");
            String expected = request.getParameter("content[" + i+"][expected]");
            String comment = request.getParameter("content[" + i+"][comment]");
            if (action != null) {
                TestcaseStep step = new TestcaseStep();
                step.setAction(action);
                step.setExpected(expected);
                step.setComment(comment);
                steps.add(step);
            }
		}
		document.setContent(testcase.generateXml());

		documentDAO.updateDocument(document);

		/*
		 * The next operation is done to obtain all the needed data of the
		 * document which wasn't specified in AJAX request Without this
		 * operations the update of the customization parameters will not work
		 */
		document = documentDAO.getDocument(document.getId());

		updateTestcaseCustomizationValues(request, document);
		model.setResult("edited");
		model.setObject(document);
		return model;
	}

	public void updateTestcaseCustomizationValues(HttpServletRequest request, Document document) throws Exception {
		Long rootId = projectDAO.getProjectRootId(document.getProjectId(), 10);
		CustomizationUtils.updateUnitCustomizationValues(rootId, document.getId(), Customization.UNIT_TEST_CASE, customizationDAO, request);
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
}
