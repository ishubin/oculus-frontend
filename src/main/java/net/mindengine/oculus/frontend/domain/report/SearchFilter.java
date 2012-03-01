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
package net.mindengine.oculus.frontend.domain.report;

import java.util.Collection;
import java.util.List;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class SearchFilter {
	public static final Integer[] PAGE_LIMITS = new Integer[] { 10, 20, 30, 50, 100, 200, 300, 500, 1000 };
	private String testCaseName;
	private List<?> testCaseStatusList;

	private Collection<SearchColumn> columnList;

	private Integer pageOffset = 1;

	// This value is an id which will be used in PAGE_LIMITS array
	private Integer pageLimit = 0;

	private String suiteRunTimeAfter;
	private String suiteRunTimeBefore;
	private String suite;
	/**
	 * The list of parameters declared by the following template:
	 * {parameter_name} = {parameter_value} \n {parameter_name} =
	 * {parameter_value}
	 */
	private String suiteRunParameters;
	private String suiteRunAgent;

	private String testRunReason;
	private String testRunReport;

	private String userRunner;
	private String userDesigner;

	private String rootProject;

	private String project;

	private Integer orderByColumnId;
	private Integer orderDirection;

	private String issue;

	public Integer[] getPageLimitArray() {
		return PAGE_LIMITS;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public List<?> getTestCaseStatusList() {
		return testCaseStatusList;
	}

	public void setTestCaseStatusList(List<?> testCaseStatusList) {
		this.testCaseStatusList = testCaseStatusList;
	}

	public String getTestRunReason() {
		return testRunReason;
	}

	public void setTestRunReason(String testRunReason) {
		this.testRunReason = testRunReason;
	}

	public String getTestRunReport() {
		return testRunReport;
	}

	public void setTestRunReport(String testRunReport) {
		this.testRunReport = testRunReport;
	}

	public String getUserRunner() {
		return userRunner;
	}

	public void setUserRunner(String userRunner) {
		this.userRunner = userRunner;
	}

	public String getUserDesigner() {
		return userDesigner;
	}

	public void setUserDesigner(String userDesigner) {
		this.userDesigner = userDesigner;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}

	public Integer getConvertedPageLimit() {
		if (pageLimit < PAGE_LIMITS.length) {
			return PAGE_LIMITS[pageLimit];
		}
		return 10;
	}

	public String getSuiteRunTimeAfter() {
		return suiteRunTimeAfter;
	}

	public void setSuiteRunTimeAfter(String suiteRunTimeAfter) {
		this.suiteRunTimeAfter = suiteRunTimeAfter;
	}

	public String getSuiteRunTimeBefore() {
		return suiteRunTimeBefore;
	}

	public void setSuiteRunTimeBefore(String suiteRunTimeBefore) {
		this.suiteRunTimeBefore = suiteRunTimeBefore;
	}

	public Integer getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(Integer orderDirection) {
		this.orderDirection = orderDirection;
	}

	public Integer getOrderByColumnId() {
		return orderByColumnId;
	}

	public void setOrderByColumnId(Integer orderByColumnId) {
		this.orderByColumnId = orderByColumnId;
	}

	public Collection<SearchColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(Collection<SearchColumn> columnList) {
		this.columnList = columnList;
	}

	public SearchColumn getColumnById(int id) {
		for (SearchColumn column : columnList) {
			if (column.getId().equals(id)) {
				return column;
			}
		}
		return null;
	}

	public void setSuiteRunParameters(String suiteRunParameters) {
		this.suiteRunParameters = suiteRunParameters;
	}

	public String getSuiteRunParameters() {
		return suiteRunParameters;
	}

	public void setSuiteRunAgent(String suiteRunAgent) {
		this.suiteRunAgent = suiteRunAgent;
	}

	public String getSuiteRunAgent() {
		return suiteRunAgent;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getSuite() {
		return suite;
	}

	public void setRootProject(String rootProject) {
		this.rootProject = rootProject;
	}

	public String getRootProject() {
		return rootProject;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssue() {
		return issue;
	}

}
