package net.mindengine.oculus.frontend.service.dashboard;

import java.util.Date;
import java.util.List;

import net.mindengine.oculus.frontend.domain.dashboard.Dashboard;
import net.mindengine.oculus.frontend.domain.dashboard.ProjectStatistics;

public interface ProjectStatisticsDAO {

	public List<ProjectStatistics> fetchProjectStatistics(Long projectId, Date startDate, Integer lastDays, Long runnerId) throws Exception;

	public Dashboard getDashboard(Long projectId) throws Exception;

	public void changeDashboard(Dashboard dashboard) throws Exception;
}
