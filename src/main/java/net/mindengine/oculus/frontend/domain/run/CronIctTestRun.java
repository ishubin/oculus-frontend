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
package net.mindengine.oculus.frontend.domain.run;

public class CronIctTestRun {
	private Long id;
	private Long testRunId;
	private Long suiteRunId;
	private Long testId;
	
	//An encoded with XmlReportRender list of report reasons
	private String reason;
	private String suiteRunParameters;
	private Long issueCollationId;
	private String testName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestRunId() {
		return testRunId;
	}

	public void setTestRunId(Long testRunId) {
		this.testRunId = testRunId;
	}

	public Long getSuiteRunId() {
		return suiteRunId;
	}

	public void setSuiteRunId(Long suiteRunId) {
		this.suiteRunId = suiteRunId;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSuiteRunParameters() {
		return suiteRunParameters;
	}

	public void setSuiteRunParameters(String suiteRunParameters) {
		this.suiteRunParameters = suiteRunParameters;
	}

	public void setIssueCollationId(Long issueCollationId) {
		this.issueCollationId = issueCollationId;
	}

	public Long getIssueCollationId() {
		return issueCollationId;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestName() {
		return testName;
	}
}
