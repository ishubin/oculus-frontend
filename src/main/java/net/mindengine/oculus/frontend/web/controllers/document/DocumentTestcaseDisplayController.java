package net.mindengine.oculus.frontend.web.controllers.document;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.testcase.Testcase;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentTestcaseDisplayController extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private UserDAO userDAO;
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		AjaxModel model = new AjaxModel();
		Long id = Long.parseLong(request.getParameter("id"));

		Document document = documentDAO.getDocument(id);
		if(document==null) throw new UnexistentResource("Document", id);
		document.setAttachments(documentDAO.getDocumentAttachments(id));
		
		Testcase testcase = Testcase.parse(document.getContent());

		// Clearing the document content to decrease the amount of data that
		// will be pasted to response
		document.setContent(null);
		testcase.setDocument(document);
		Long rootId = projectDAO.getProjectRootId(document.getProjectId(), 5);

		testcase.setCustomizationGroups(CustomizationUtils.fetchCustomizationGroups(customizationDAO, userDAO, rootId, document.getId(), Customization.UNIT_TEST_CASE));
		model.setObject(testcase);
		model.setResult("test-case");
		return model;
	}

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public DocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

}
