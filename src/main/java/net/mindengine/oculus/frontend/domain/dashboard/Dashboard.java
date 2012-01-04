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
