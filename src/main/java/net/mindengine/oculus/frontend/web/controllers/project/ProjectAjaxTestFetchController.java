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
package net.mindengine.oculus.frontend.web.controllers.project;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

/**
 * Fetches Ids of the tests in specified project and its sub-projects
 * 
 * @author Ivan Shubin
 * 
 */
public class ProjectAjaxTestFetchController extends SimpleAjaxController {
	private TestDAO testDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		AjaxModel model = new AjaxModel();

		Long projectId = Long.parseLong(request.getParameter("projectId"));

		List<Test> tests = testDAO.fetchTestsFromProjects(projectId);
		Long[] ids = new Long[tests.size()];

		Iterator<Test> it = tests.iterator();
		for (int i = 0; i < ids.length; i++) {
			Test t = it.next();
			ids[i] = t.getId();
		}
		model.setResult("fetched");
		model.setObject(ids);
		return model;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}
}
