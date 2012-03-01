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
package net.mindengine.oculus.frontend.web.controllers.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.test.Test;
import net.mindengine.oculus.frontend.domain.test.TestSearchFilter;
import net.mindengine.oculus.frontend.service.customization.CustomizationDAO;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class TestAjaxSearchController extends SimpleAjaxController {
	private TestDAO testDAO;
	private CustomizationDAO customizationDAO;
	private ColumnFactory columnFactory;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		/*
		 * Filling a test divided into groups by the sub-project
		 */
		Map<String, Object> map = new HashMap<String, Object>();

		TestSearchFilter filter = new TestSearchFilter();
		filter.setGroupBySubProject(true);
		filter.setProject(request.getParameter("projectId"));
		filter.setName(request.getParameter("name"));
		filter.setSubProject(request.getParameter("subproject"));
		filter.setDesigner(request.getParameter("designer"));
		filter.setTestGroup(request.getParameter("testGroup"));
		filter.setPageLimit(Integer.parseInt(request.getParameter("pageLimit")));
		filter.setColumns(columnFactory.getColumnList());
		Integer page;
		String strPage = request.getParameter("page");
		if (strPage != null) {
			page = Integer.parseInt(strPage);
		}
		else
			page = 1;
		filter.setPageOffset(page);

		/*
		 * Collecting all customization parameters
		 */
		filter.setCustomizations(CustomizationUtils.collectCustomizationSearchCriteriaParameters(request));

		BrowseResult<Test> tests = testDAO.searchTestsGroupedBySubproject(filter);

		List<Map<String, Object>> testProjects = new LinkedList<Map<String, Object>>();

		/**
		 * Grouping all tests in a structure where the first grouping is made by
		 * project and then it will be made by test_groups
		 */
		Long group = -1L;
		Long project = -1L;

		Map<String, Object> currentTestGroup = null;
		Map<String, Object> currentProjectGroup = null;
		Collection<Test> currentTestList = null;
		Collection<Map<String, Object>> currentTestGroupList = null;
		for (Test test : tests.getResults()) {
			if (!project.equals(test.getProjectId())) {
				group = -1L;
				project = test.getProjectId();
				currentProjectGroup = new HashMap<String, Object>();
				currentProjectGroup.put("name", test.getProjectName());
				currentTestGroupList = new LinkedList<Map<String, Object>>();
				currentProjectGroup.put("testGroups", currentTestGroupList);
				testProjects.add(currentProjectGroup);
			}
			if (!group.equals(test.getGroupId())) {
				group = test.getGroupId();
				currentTestGroup = new HashMap<String, Object>();
				currentTestGroup.put("name", test.getGroupName());
				currentTestList = new LinkedList<Test>();
				currentTestGroup.put("tests", currentTestList);
				currentTestGroupList.add(currentTestGroup);
			}
			currentTestList.add(test);
		}

		map.put("numberOfResults", tests.getNumberOfResults());
		map.put("testProjects", testProjects);
		map.put("page", tests.getPage());
		map.put("limit", tests.getLimit());
		return new AjaxModel("search", map);
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setCustomizationDAO(CustomizationDAO customizationDAO) {
		this.customizationDAO = customizationDAO;
	}

	public CustomizationDAO getCustomizationDAO() {
		return customizationDAO;
	}

	public void setColumnFactory(ColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public ColumnFactory getColumnFactory() {
		return columnFactory;
	}
}
