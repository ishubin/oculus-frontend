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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mindengine.oculus.experior.Parameter;


public class TestRunSearchData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6368489000618681124L;

	// This is just a id number in the list from 0 to n values. Used only in JSP
	// templates.
	private Integer id;

	private Long testRunId;
	private String testRunName;
	private String testRunStatus;
	private Date testRunStartTime;
	private Date testRunEndTime;
	private String testRunReasons;
	private String testRunReport;
	private String testRunDescription;

	private Long suiteRunId;
	private String suiteRunName;
	private Date suiteRunStartTime;
	private Date suiteRunEndTime;
	private String suiteRunParameters;
	private String suiteRunAgentName;

	private Long projectId;
	private String projectName;
	private String projectPath;

	private Long parentProjectId;
	private String parentProjectName;
	private String parentProjectPath;

	private Long testId;
	private String testName;
	private String testDescription;

	private Long runnerId;
	private String runnerName;
	private String runnerLogin;

	private Long designerId;
	private String designerName;
	private String designerLogin;

	private Long issueId;
	private String issueName;
	private String issueLink;

	public Long getTestRunId() {
		return testRunId;
	}

	public void setTestRunId(Long testRunId) {
		this.testRunId = testRunId;
	}

	public String getTestRunName() {
		return testRunName;
	}

	public void setTestRunName(String testRunName) {
		this.testRunName = testRunName;
	}

	public String getTestRunStatus() {
		return testRunStatus;
	}

	public void setTestRunStatus(String testRunStatus) {
		this.testRunStatus = testRunStatus;
	}

	public Date getTestRunStartTime() {
		return testRunStartTime;
	}

	public void setTestRunStartTime(Date testRunStartTime) {
		this.testRunStartTime = testRunStartTime;
	}

	public Date getTestRunEndTime() {
		return testRunEndTime;
	}

	public void setTestRunEndTime(Date testRunEndTime) {
		this.testRunEndTime = testRunEndTime;
	}

	public String getTestRunReasons() {
		return testRunReasons;
	}

	public void setTestRunReasons(String testRunReasons) {
		this.testRunReasons = testRunReasons;
	}

	public String getTestRunReport() {
		return testRunReport;
	}

	public void setTestRunReport(String testRunReport) {
		this.testRunReport = testRunReport;
	}

	public Long getSuiteRunId() {
		return suiteRunId;
	}

	public void setSuiteRunId(Long suiteRunId) {
		this.suiteRunId = suiteRunId;
	}

	public String getSuiteRunName() {
		return suiteRunName;
	}

	public void setSuiteRunName(String suiteRunName) {
		this.suiteRunName = suiteRunName;
	}

	public Date getSuiteRunStartTime() {
		return suiteRunStartTime;
	}

	public void setSuiteRunStartTime(Date suiteRunStartTime) {
		this.suiteRunStartTime = suiteRunStartTime;
	}

	public Date getSuiteRunEndTime() {
		return suiteRunEndTime;
	}

	public void setSuiteRunEndTime(Date suiteRunEndTime) {
		this.suiteRunEndTime = suiteRunEndTime;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public String getRunnerName() {
		return runnerName;
	}

	public void setRunnerName(String runnerName) {
		this.runnerName = runnerName;
	}

	public String getRunnerLogin() {
		return runnerLogin;
	}

	public void setRunnerLogin(String runnerLogin) {
		this.runnerLogin = runnerLogin;
	}

	public Long getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Long designerId) {
		this.designerId = designerId;
	}

	public String getDesignerName() {
		return designerName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public String getDesignerLogin() {
		return designerLogin;
	}

	public void setDesignerLogin(String designerLogin) {
		this.designerLogin = designerLogin;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getFetchTestName() {
		if (testName != null) {
			return testName;
		}
		else
			return testRunName;
	}

	public String getFetchProjectName() {
		if (projectName != null) {
			return projectName;
		}
		else
			return suiteRunName;
	}

	public String getFieldByName(String name) {
		try {
			String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Method method = getClass().getMethod(methodName);
			Object value = method.invoke(this);
			if (value != null) {
				return value.toString();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean getHasReasons() {
		if (testRunReasons != null && !testRunReasons.isEmpty())
			return true;
		return false;
	}

	public String getFirstReason() {
		String[] reasons = getTestRunReasonsList();
		if (reasons != null && reasons.length > 0)
			return reasons[0];
		return null;
	}

	public String[] getTestRunReasonsList() {
		if (testRunReasons != null && !testRunReasons.isEmpty()) {
			return testRunReasons.split("<r>");
		}
		return null;
	}

	public void setSuiteRunParameters(String suiteRunParameters) {
		this.suiteRunParameters = suiteRunParameters;
	}

	public String getSuiteRunParameters() {
		return suiteRunParameters;
	}

	private List<Parameter> cachedSuiteRunParametersList = null;

	public List<Parameter> getCachedSuiteRunParametersList() {
		if (cachedSuiteRunParametersList == null) {
			cachedSuiteRunParametersList = new ArrayList<Parameter>();
			// Deserializing the suite run parameters
			if (suiteRunParameters != null && !suiteRunParameters.isEmpty()) {
				String[] parameters = suiteRunParameters.split("<p>");
				for (int i = 0; i < parameters.length; i++) {
					if (!parameters[i].isEmpty()) {
						String parameter[] = parameters[i].split("<v>");
						if (parameter.length > 1) {
							Parameter p = new Parameter();
							p.setName(parameter[0]);
							p.setValue(parameter[1]);
							cachedSuiteRunParametersList.add(p);
						}
					}
				}
			}
		}
		return cachedSuiteRunParametersList;
	}

	public void setSuiteRunAgentName(String suiteRunAgentName) {
		this.suiteRunAgentName = suiteRunAgentName;
	}

	public String getSuiteRunAgentName() {
		return suiteRunAgentName;
	}

	public void setParentProjectPath(String parentProjectPath) {
		this.parentProjectPath = parentProjectPath;
	}

	public String getParentProjectPath() {
		return parentProjectPath;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectId(Long parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public Long getParentProjectId() {
		return parentProjectId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueLink(String issueLink) {
		this.issueLink = issueLink;
	}

	public String getIssueLink() {
		return issueLink;
	}

	public String getFetchIssueName() {
		if (issueName != null && !issueName.isEmpty()) {
			return issueName;
		}
		else
			return issueLink;
	}

    public void setTestRunDescription(String testRunDescription) {
        this.testRunDescription = testRunDescription;
    }

    public String getTestRunDescription() {
        return testRunDescription;
    }
}
