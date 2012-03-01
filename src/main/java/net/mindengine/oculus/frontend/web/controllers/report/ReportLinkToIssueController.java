/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.issue.IssueCollation;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationCondition;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationTest;
import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.trm.TrmProperty;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class ReportLinkToIssueController extends SecureSimpleViewController {
	private TrmDAO trmDAO;
	private IssueDAO issueDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		verifyPermissions(request);
		String redirect = getMandatoryStringParameter(request, "redirect");
		Long issueId = getMandatoryLongParameter(request, "reportLinkIssue");
		Long projectId = getMandatoryLongParameter(request, "projectId");
		String reasonPattern = getMandatoryStringParameter(request, "linkIssueReasonPattern");

		/*
		 * Obtaining collected test runs from session data
		 */
		List<TestRunSearchData> collectedTestRuns = session.getCollectedTestRuns();
		if (collectedTestRuns == null || collectedTestRuns.size() == 0)
			throw new InvalidRequest("You have to collect test runs first");

		IssueCollation issueCollation = new IssueCollation();
		issueCollation.setIssueId(issueId);
		issueCollation.setReasonPattern(reasonPattern);

		/*
		 * Collecting a set of tests into the issue collation.
		 * 
		 * Using a map instead of list because we have to collect only unique
		 * tests. So to prevent a test duplication we will check for each test
		 * in a map before adding it. As we have 2 ways of linking tests to
		 * issue: by testId and by test name we will store them in two map
		 * variables
		 */
		Map<Long, IssueCollationTest> testsMap = new HashMap<Long, IssueCollationTest>();
		Map<String, IssueCollationTest> testNamesMap = new HashMap<String, IssueCollationTest>();

		for (TestRunSearchData testRun : collectedTestRuns) {
			if (testRun.getTestId() != null && testRun.getTestId() > 0) {
				IssueCollationTest test = new IssueCollationTest();
				test.setTestId(testRun.getTestId());
				if (!testsMap.containsKey(test.getTestId())) {
					testsMap.put(test.getTestId(), test);
				}
			}
			else {
				/*
				 * This means that a test run wasn't bound to any existent test.
				 * Here it will check that there is no
				 */
				IssueCollationTest test = new IssueCollationTest();
				test.setTestId(0L);
				test.setTestName(testRun.getTestRunName());
				if (!testNamesMap.containsKey(test.getTestName())) {
					testNamesMap.put(test.getTestName(), test);
				}
			}
		}
		Collection<IssueCollationTest> collatedTests = new LinkedList<IssueCollationTest>();
		collatedTests.addAll(testsMap.values());
		collatedTests.addAll(testNamesMap.values());
		issueCollation.setTests(collatedTests);

		/*
		 * Collecting a set of conditions To be able to do this we will need to
		 * fetch the project trm suite parameters before.
		 */
		issueCollation.setConditions(new LinkedList<IssueCollationCondition>());
		List<TrmProperty> suiteParameters = trmDAO.getProperties(projectId, TrmProperty._TYPE_SUITE_PARAMETER);
		if (suiteParameters.size() > 0) {
			for (TrmProperty suiteParameter : suiteParameters) {
				// suiteParameter.getId();
				Long spId = suiteParameter.getId();
				boolean used = false;
				String strUsed = request.getParameter("linkIssueSuiteParameterUse" + spId);
				if (strUsed != null && strUsed.toLowerCase().equals("on")) {
					used = true;
				}

				if (used) {
					String value = request.getParameter("linkIssueSuiteParameterValue" + spId);
					if (value == null)
						value = "off";
					if (!value.isEmpty()) {
						IssueCollationCondition condition = new IssueCollationCondition();
						condition.setTrmPropertyId(spId);
						condition.setValue(value);
						issueCollation.getConditions().add(condition);
					}
				}
			}
		}

		issueDAO.linkTestRunsToIssue(collectedTestRuns, issueId);
		issueDAO.linkTestsToIssue(issueCollation);

		if (!redirect.isEmpty())
			return new ModelAndView("redirect:" + redirect);
		return new ModelAndView("redirect:../display/home");
	}

	public void setTrmDAO(TrmDAO trmDAO) {
		this.trmDAO = trmDAO;
	}

	public TrmDAO getTrmDAO() {
		return trmDAO;
	}

	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public IssueDAO getIssueDAO() {
		return issueDAO;
	}

}
