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

public class Dashboard {
	private Long projectId;
	private Long runnerId = 1L;
	private Integer daysPeriod = 16;
	private Integer dayStart = 0;
	private Boolean summaryStatistics = true;
	private Boolean healthChart = true;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}

	public Integer getDaysPeriod() {
		return daysPeriod;
	}

	public void setDaysPeriod(Integer daysPeriod) {
		this.daysPeriod = daysPeriod;
	}

	public Integer getDayStart() {
		return dayStart;
	}

	public void setDayStart(Integer dayStart) {
		this.dayStart = dayStart;
	}

	public Boolean getSummaryStatistics() {
		return summaryStatistics;
	}

	public void setSummaryStatistics(Boolean summaryStatistics) {
		this.summaryStatistics = summaryStatistics;
	}

	public Boolean getHealthChart() {
		return healthChart;
	}

	public void setHealthChart(Boolean healthChart) {
		this.healthChart = healthChart;
	}

}
