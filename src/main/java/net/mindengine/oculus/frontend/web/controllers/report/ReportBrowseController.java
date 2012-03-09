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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.db.search.ColumnFactory;
import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.domain.Option;
import net.mindengine.oculus.frontend.domain.report.ReportSearchColumn;
import net.mindengine.oculus.frontend.domain.report.SearchFilter;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.runs.TestRunDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.apache.commons.collections.ListUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class ReportBrowseController extends SecureSimpleFormController {

	private ProjectDAO projectDAO;
	private Config config;
	/**
	 * This is a factory that is used to fetch the default list of columns to
	 * display in report table in case if column list wasn't defined previously
	 * in session
	 */
	private ColumnFactory columnFactory;

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Session session = Session.create(request);
		SearchFilter filter = (SearchFilter) command;

		filter.setColumnList(columnFactory.getColumnList());
	
		Map referenceData = new HashMap();
		List statuses = new ArrayList<Option>();
		
		List filterTCStatusList = null;
		
		if(filter!=null) {
		    filterTCStatusList = filter.getTestCaseStatusList();
		}
		
		if(filterTCStatusList == null) {
		    filterTCStatusList = ListUtils.EMPTY_LIST;
		}
		
		statuses.add(new Option("PASSED", "<img src=\"../images/filter-passed.png\"/>Passed", filterTCStatusList.contains("PASSED")));
		statuses.add(new Option("WARNING", "<img src=\"../images/filter-warning.png\"/>Warning", filterTCStatusList.contains("WARNING")));
		statuses.add(new Option("FAILED", "<img src=\"../images/filter-failed.png\"/>Failed", filterTCStatusList.contains("FAILED")));

		referenceData.put("testCaseStatusList", statuses);
		referenceData.put("columnFactory", columnFactory);

		referenceData.put("savedTestRunList", session.getCollectedTestRuns());
		referenceData.put("savedTestRunListCount", session.getCollectedTestRuns().size());

		referenceData.put("rootProjects", projectDAO.getRootProjects());
		referenceData.put("title", getTitle());
		return referenceData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Object object = super.formBackingObject(request);
		SearchFilter searchFilter = (SearchFilter) object;
		searchFilter.setOrderByColumnId(ReportSearchColumn.START_TIME);
		searchFilter.setOrderDirection(-1);
		searchFilter.setPageLimit(4);
		return object;
	}

	public SearchColumn getColumnById(int id, Collection<SearchColumn> columnList) {

		for (SearchColumn column : columnList) {
			if (column.getId().equals(id)) {
				return column;
			}
		}
		return null;
	}

	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		return true;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject("reportSearchFilter", command);
		mav.addAllObjects(referenceData(request, command, errors));
		return mav;
	}

	public ColumnFactory getColumnFactory() {
		return columnFactory;
	}

	public void setColumnFactory(ColumnFactory columnFactory) {
		this.columnFactory = columnFactory;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
}
