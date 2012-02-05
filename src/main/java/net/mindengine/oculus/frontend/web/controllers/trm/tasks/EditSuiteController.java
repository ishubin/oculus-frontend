package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.trm.TrmSuite;
import net.mindengine.oculus.frontend.domain.trm.TrmSuiteStub;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class EditSuiteController extends SecureSimpleViewController {
	private TrmDAO trmDAO;
	private TestDAO testDAO;
	private ProjectDAO projectDAO;
	private CustomizationDAO customizationDAO;

	private void saveSuite(TrmSuite suite, HttpServletRequest request) throws Exception {
		String name = request.getParameter("suiteName");
		String description = request.getParameter("suiteDescription");
		
		if (name == null || name.isEmpty())
			throw new InvalidRequest();

		suite.setName(name);
		suite.setDescription(description);
		suite.setSuiteData(request.getParameter("suiteData"));
		suite.setGroupId(Long.parseLong(request.getParameter("groupId")));
		

		trmDAO.saveSuite(suite);
	}

	/**
	 * Fetches tests with all latest data. It will be stored in map so when the
	 * test-suite is about to be rendered - it will use this data to adjust all
	 * changes in tests.
	 * 
	 * @param trmSuite
	 * @return
	 */
	public Map<Long, Test> getCashedLatestTests(TrmSuite trmSuite) throws Exception {
		Map<Long, Test> cashedTest = new HashMap<Long, Test>();

		if (trmSuite.getSuiteData() != null && !trmSuite.getSuiteData().isEmpty()) {
		    List<TrmSuiteStub> suite = TrmSuite.convertSuiteFromJSONToStub(trmSuite.getSuiteData());

			for (TrmSuiteStub td : suite) {
				if (td.getTestId() != null) {
					if (!cashedTest.containsKey(td.getTestId())) {
						cashedTest.put(td.getTestId(), testDAO.fetchTestWithParameterById(td.getTestId()));
					}
				}
				if(td.getTests()!=null) {
				    for (TrmSuiteStub childTd : td.getTests()) {
		                if (childTd.getTestId() != null) {
		                    if (!cashedTest.containsKey(childTd.getTestId())) {
		                        cashedTest.put(childTd.getTestId(), testDAO.fetchTestWithParameterById(childTd.getTestId()));
		                    }
		                }
				    }
				}
			}
		}
		return cashedTest;
	}

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long suiteId = Long.parseLong(request.getParameter("id"));

		TrmSuite suite = trmDAO.getSuite(suiteId);

		if (suite == null)
			throw new UnexistentResource("The suite with id = " + suiteId + " wasn't found");

		User user = getUser(request);
		TrmTask task = trmDAO.getTask(suite.getTaskId());
		if (!task.getUserId().equals(user.getId()))
			throw new NotAuthorizedException("You are not authorized to edit suites of other users");

		String submit = request.getParameter("Submit");
		if (submit != null) {
			if (submit.equals("Save")) {
				saveSuite(suite, request);
			}
		}

		Map<Long, Test> cashedTests = getCashedLatestTests(suite);
		map.put("cashedTests", cashedTests);
		map.put("task", task);

		map.put("groups", trmDAO.getTaskSuiteGroups(task.getId()));

		map.put("suite", suite);
		map.put("bodyOnLoad", "loadTestList();");

		map.put("subprojects", projectDAO.getSubprojects(task.getProjectId()));

		map.put("customizationGroups", CustomizationUtils.fetchCustomizationGroups(customizationDAO, null, task.getProjectId(), 0L, Customization.UNIT_TEST));
		return map;
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
}
