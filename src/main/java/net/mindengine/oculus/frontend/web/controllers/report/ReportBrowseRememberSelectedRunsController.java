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
package net.mindengine.oculus.frontend.web.controllers.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.report.TestRunSearchData;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.web.Auth;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ReportBrowseRememberSelectedRunsController extends SecureSimpleViewController {
	private TestRunDAO testRunDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		String redirect = request.getParameter("redirect");
		if (redirect == null) {
			redirect = "";
		}
		String reqIds = request.getParameter("ids");
		if (reqIds != null && !reqIds.isEmpty()) {
			String[] ids = reqIds.split(",");
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				list.add(Integer.parseInt(ids[i]));
			}
			collectTestRuns(request, list, session);
		}

		String removeIds = request.getParameter("removeIds");
		if (removeIds != null && !removeIds.isEmpty()) {
			removeCollectedTestRuns(removeIds, session);
		}

		return new ModelAndView(new RedirectView(redirect));
	}

	public void removeCollectedTestRuns(String ids, Session session) {
		List<TestRunSearchData> list = session.getCollectedTestRuns();
		if (list != null) {
			String[] id = ids.split(",");

			for (int i = 0; i < id.length; i++) {
				Iterator<TestRunSearchData> iterator = list.iterator();
				boolean bFound = false;
				while (iterator.hasNext() && !bFound) {
					TestRunSearchData run = iterator.next();
					if (run.getTestRunId().equals(Long.parseLong(id[i]))) {
						iterator.remove();
						bFound = true;
					}
				}
			}
		}
		// Setting again the collected runs list to session as the content of
		// this list was changed and it is necessary
		// to update the id field of each item in list in the following method
		session.setCollectedTestRuns(list);
	}

	private void collectTestRuns(HttpServletRequest request, List<Integer> list, Session session) throws Exception {
	    
	    User user = Auth.getUserFromRequest(request);
		if (user != null) {
			List<TestRunSearchData> testRunListToSave = testRunDAO.getTestRunsByIds(list);
			List<TestRunSearchData> savedTestRunList = session.getCollectedTestRuns();
			// Adding new list to the existent list in session
			// To get rid of duplicates here it is needed to compare the new
			// items with existent by testRunId field
			for (TestRunSearchData dataToSave : testRunListToSave) {
				boolean bHasAlready = false;

				Iterator<TestRunSearchData> it = savedTestRunList.iterator();
				while (it.hasNext() && !bHasAlready) {
					TestRunSearchData saved = it.next();
					if (saved.getTestRunId().equals(dataToSave.getTestRunId())) {
						bHasAlready = true;
					}
				}
				if (!bHasAlready) {
					// Adding to the existent list in session
					savedTestRunList.add(dataToSave);
				}
			}
			session.setCollectedTestRuns(savedTestRunList);
		}
		else
			throw new NotAuthorizedException();
	}

	public void setTestRunDAO(TestRunDAO testRunDAO) {
		this.testRunDAO = testRunDAO;
	}

	public TestRunDAO getTestRunDAO() {
		return testRunDAO;
	}
}
