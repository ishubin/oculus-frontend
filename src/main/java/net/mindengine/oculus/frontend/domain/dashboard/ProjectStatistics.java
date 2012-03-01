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
package net.mindengine.oculus.frontend.domain.dashboard;

import java.util.Date;

public class ProjectStatistics {

	private Long suiteRunId;
	private Long projectId;
	private String projectName;
	private String projectPath;
	private Integer total = 0;
	private Integer failed = 0;
	private Integer passed = 0;
	private Integer warning = 0;
	private Date startTime;

	public Long getSuiteRunId() {
		return suiteRunId;
	}

	public void setSuiteRunId(Long suiteRunId) {
		this.suiteRunId = suiteRunId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getFailed() {
		return failed;
	}

	public void setFailed(Integer failed) {
		this.failed = failed;
	}

	public Integer getPassed() {
		return passed;
	}

	public void setPassed(Integer passed) {
		this.passed = passed;
	}

	public Integer getWarning() {
		return warning;
	}

	public void setWarning(Integer warning) {
		this.warning = warning;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
