package net.mindengine.oculus.frontend.service.dashboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.dashboard.Dashboard;
import net.mindengine.oculus.frontend.domain.dashboard.ProjectStatistics;
import net.mindengine.oculus.experior.utils.FileUtils;

public class JdbcProjectStatisticsDAO extends MySimpleJdbcDaoSupport implements ProjectStatisticsDAO {
	private File dashboardFetchProjectFile;
	private String dashboardFetchProjectTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectStatistics> fetchProjectStatistics(Long projectId, Date startDate, Integer lastDays, Long runnerId) throws Exception {
		String sql = dashboardFetchProjectTemplate;
		if (runnerId > 0) {
			sql = sql.replaceAll("\\[runner-condition\\](.*?)\\[/runner-condition\\]", "$1");
			sql = sql.replace("#runner-id#", "" + runnerId);
		}
		else {
			sql = sql.replaceAll("\\[runner-condition\\](.*?)\\[/runner-condition\\]", "");
		}

		List<ProjectStatistics> list = (List<ProjectStatistics>) query(sql, ProjectStatistics.class, "projectId", projectId, "interval", lastDays, "startDate", startDate);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dashboard getDashboard(Long projectId) throws Exception {
		List<Dashboard> list = (List<Dashboard>) query("select * from dashboards where project_id = :projectId", Dashboard.class, "projectId", projectId);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public void changeDashboard(Dashboard dashboard) throws Exception {
		update("update dashboards set runner_id = :runnerId, " + "days_period = :daysPeriod, " + "day_start = :dayStart, " + "summary_statistics = :summaryStatistics, " + "health_chart = :healthChart " + "where project_id = :projectId", "runnerId", dashboard.getRunnerId(), "daysPeriod", dashboard
				.getDaysPeriod(), "dayStart", dashboard.getDayStart(), "summaryStatistics", dashboard.getSummaryStatistics(), "healthChart", dashboard.getHealthChart(), "projectId", dashboard.getProjectId());
	}

	public File getDashboardFetchProjectFile() {
		return dashboardFetchProjectFile;
	}

	public void setDashboardFetchProjectFile(File dashboardFetchProjectFile) throws FileNotFoundException, IOException {
		this.dashboardFetchProjectFile = dashboardFetchProjectFile;
		setDashboardFetchProjectTemplate(FileUtils.readFile(dashboardFetchProjectFile));
	}

	public String getDashboardFetchProjectTemplate() {
		return dashboardFetchProjectTemplate;
	}

	public void setDashboardFetchProjectTemplate(String dashboardFetchProjectTemplate) {
		this.dashboardFetchProjectTemplate = dashboardFetchProjectTemplate;
	}

}
