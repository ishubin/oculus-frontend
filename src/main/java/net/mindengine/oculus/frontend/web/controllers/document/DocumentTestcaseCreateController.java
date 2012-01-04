package net.mindengine.oculus.frontend.web.controllers.document;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.document.Document;
import net.mindengine.oculus.frontend.domain.document.testcase.Testcase;
import net.mindengine.oculus.frontend.domain.document.testcase.TestcaseStep;
import net.mindengine.oculus.frontend.domain.folder.Folder;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.document.DocumentDAO;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.folder.FolderDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class DocumentTestcaseCreateController extends SimpleAjaxController {
	private DocumentDAO documentDAO;
	private FolderDAO folderDAO;
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

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
		document.setUserId(user.getId());
		document.setFolderId(Long.parseLong(request.getParameter("folderId")));
		document.setProjectId(Long.parseLong(request.getParameter("projectId")));
		document.setType(Document._TYPE_TEST_CASE);
		Long stepsCount = Long.parseLong(request.getParameter("contentSize"));

		/*
		 * Need to copy the folders branch to document branch, it will be needed
		 * when we want to delete a folder which contains documents
		 */
		if (document.getFolderId() > 0) {
			Folder folder = folderDAO.getFolder(document.getFolderId());
			if (folder == null)
				throw new UnexistentResource("The folder with id '" + document.getFolderId() + "' doesn't exist");
			document.setProjectId(folder.getProjectId());
			document.setBranch(folder.getBranch());
		}

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

		Long id = documentDAO.createDocument(document);
		document.setId(id);

		updateTestcaseCustomizationValues(request, document);

		model.setResult("created");
		document.setContent("");
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

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}

	public FolderDAO getFolderDAO() {
		return folderDAO;
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
