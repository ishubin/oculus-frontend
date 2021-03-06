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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.issue;

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.issue.IssueCollation;
import net.mindengine.oculus.frontend.domain.issue.IssueCollationTest;
import net.mindengine.oculus.frontend.service.issue.IssueDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;

public class RemoveIssueCollationsController extends SecureSimpleViewController {
	private IssueDAO issueDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		verifyPermissions(request);

		Long issueId = getMandatoryLongParameter(request, "issueId");
		Collection<IssueCollation> collations = issueDAO.getIssueCollations(issueId);

		for (IssueCollation collation : collations) {
			Collection<IssueCollationTest> tests = collation.getTests();
			if (tests != null && tests.size() > 0) {
				Collection<IssueCollationTest> selectedTests = new LinkedList<IssueCollationTest>();

				for (IssueCollationTest test : tests) {
					String checkboxState = request.getParameter("chkTest_" + collation.getId() + "_" + test.getId());
					if (checkboxState != null && checkboxState.toLowerCase().equals("on")) {
						selectedTests.add(test);
					}
				}
				if (selectedTests.size() > 0) {
					issueDAO.deleteIssueCollationTests(issueId, collation.getId(), selectedTests);
				}
			}
		}

		String redirect = request.getParameter("redirect");
		if (redirect != null && !redirect.isEmpty()) {
			return new ModelAndView("redirect:" + redirect);
		}
		return new ModelAndView("redirect:../display/home");
	}

	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public IssueDAO getIssueDAO() {
		return issueDAO;
	}

}
