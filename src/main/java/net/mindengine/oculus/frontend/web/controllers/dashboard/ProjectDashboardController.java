package net.mindengine.oculus.frontend.web.controllers.dashboard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.dashboard.Dashboard;
import net.mindengine.oculus.frontend.domain.dashboard.ProjectStatistics;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.dashboard.ProjectStatisticsDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ProjectDashboardController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private ProjectStatisticsDAO projectStatisticsDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Date todayDate = new Date();
		Session session = Session.create(request);
		String path = request.getPathInfo().substring(9);
		Project project = projectDAO.getProjectByPath(path);

		Dashboard dashboard = projectStatisticsDAO.getDashboard(project.getId());

		List<Project> subprojects = projectDAO.getSubprojects(project.getId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("project", project);

		List<Map<String, Object>> subprojectCharts = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> healthChartData = new ArrayList<Map<String, Object>>();

		for (Project subproject : subprojects) {
			List<ProjectStatistics> statistics = projectStatisticsDAO.fetchProjectStatistics(subproject.getId(), todayDate, dashboard.getDaysPeriod(), dashboard.getRunnerId());
			List<ProjectStatistics> todayStatistics = projectStatisticsDAO.fetchProjectStatistics(subproject.getId(), todayDate, 1, dashboard.getRunnerId());

			// Generating chart for subproject statitistics
			Date endDate = ridOfTime(new Date());
			Date startDate = subDateDays(endDate, dashboard.getDaysPeriod());
			JFreeChart subprojectStatisticsChart = createSubprojectStatisticsChart(fillStatistics(statistics, startDate, endDate), "\"" + subproject.getName() + "\" statistics for last " + dashboard.getDaysPeriod() + " days");

			String statisticsChartId = "project-" + subproject.getPath() + "-statistics-chart.png";
			session.saveChart(statisticsChartId, subprojectStatisticsChart);

			Map<String, Object> subprojectChart = new HashMap<String, Object>();
			subprojectChart.put("statisticsChartId", statisticsChartId);

			JFreeChart todayChart = createTodayStatisticsChart(todayStatistics, subproject.getName());
			String todayChartId = "project-" + subproject.getPath() + "-today-chart.png";
			session.saveChart(todayChartId, todayChart);
			subprojectChart.put("todayChartId", todayChartId);
			subprojectChart.put("project", subproject);
			if (dashboard.getHealthChart()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("name", subproject.getName());
				m.put("percents", getTodayStatisticsChartPercents(todayStatistics));
				healthChartData.add(m);
			}
			subprojectChart.put("subproject", subproject);
			subprojectCharts.add(subprojectChart);
		}

		if (dashboard.getHealthChart()) {
			JFreeChart healthChart = createHealthChart(project.getName(), healthChartData);
			String healthChartId = "project-" + project.getPath() + "-health-chart.png";
			session.saveChart(healthChartId, healthChart);
			map.put("healthChartId", healthChartId);
			map.put("healthChartHeight", 100 + healthChartData.size() * 60);

		}

		map.put("subprojectCharts", subprojectCharts);
		Long uniqueKey = new Date().getTime();
		map.put("uniqueKey", uniqueKey);
		map.put("dashboard", dashboard);
		map.put("title", getTitle() + " " + project.getName());
		return map;
	}

	public JFreeChart createHealthChart(String projectName, List<Map<String, Object>> healthChartData) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Map<String, Object> data : healthChartData) {
			Integer[] percents = (Integer[]) data.get("percents");
			dataset.addValue((double) percents[0], "Passed", (String) data.get("name"));
			dataset.addValue((double) percents[1], "Failed", (String) data.get("name"));
			dataset.addValue((double) percents[2], "Warning", (String) data.get("name"));
		}

		JFreeChart chart = ChartFactory.createBarChart3D("\"" + projectName + "\" Health Chart", // chart
																									// title
				"Subprojects", // domain axis label
				"Status", // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		ValueAxis axis = plot.getRangeAxis();
		axis.setRange(0, 100);
		CategoryItemRenderer renderer = plot.getRenderer();

		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesPaint(2, Color.ORANGE);
		plot.getDomainAxis().setVisible(true);
		plot.getRangeAxis().setVisible(true);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		chart.setBackgroundPaint(new Color(230, 230, 230));
		return chart;
	}

	public Integer[] getTodayStatisticsChartPercents(List<ProjectStatistics> statistics) {
		Integer[] percents = new Integer[] { 0, 0, 0 };
		Integer passed = 0;
		Integer failed = 0;
		Integer warning = 0;
		Integer total = 0;
		for (ProjectStatistics statistic : statistics) {
			passed += statistic.getPassed();
			failed += statistic.getFailed();
			warning += statistic.getWarning();
			total += statistic.getTotal();
		}

		if (total > 0) {
			percents[0] = (passed * 100) / total;
			percents[1] = (failed * 100) / total;
			percents[2] = (warning * 100) / total;
		}
		return percents;
	}

	public JFreeChart createTodayStatisticsChart(List<ProjectStatistics> statistics, String subprojectName) {

		Integer[] percents = getTodayStatisticsChartPercents(statistics);
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(new String[] { "Passed", "Failed", "Warning" }, new String[] { "Today" }, new double[][] { { percents[0] }, { percents[1] }, { percents[2] } });

		JFreeChart chart = ChartFactory.createBarChart("\"" + subprojectName + "\" today statistics", // chart
																										// title
				"Tests", // domain axis label
				"Percent (%)", // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				true, // include legend
				true, false);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryItemRenderer renderer = plot.getRenderer();
		ValueAxis axis = plot.getRangeAxis();
		axis.setRange(0, 100);
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesPaint(2, Color.ORANGE);
		plot.getDomainAxis().setVisible(true);
		plot.getRangeAxis().setVisible(true);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		chart.setBackgroundPaint(new Color(230, 230, 230));
		return chart;
	}

	public JFreeChart createSubprojectStatisticsChart(List<ProjectStatistics> sortedStatistics, String title) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		TimeSeries totalSet = new TimeSeries("Total");
		TimeSeries failedSet = new TimeSeries("Failed");
		TimeSeries passedSet = new TimeSeries("Passed");
		TimeSeries warningSet = new TimeSeries("Warning");
		for (ProjectStatistics ps : sortedStatistics) {
			Date d = ridOfTime(ps.getStartTime());
			totalSet.add(new Day(d), ps.getTotal());
			failedSet.add(new Day(d), ps.getFailed());
			passedSet.add(new Day(d), ps.getPassed());
			warningSet.add(new Day(d), ps.getWarning());

		}
		dataset.addSeries(totalSet);
		dataset.addSeries(failedSet);
		dataset.addSeries(passedSet);
		dataset.addSeries(warningSet);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date", "Amount of Tests", dataset, true, true, false);
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		renderer.setSeriesPaint(0, Color.BLUE);
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesPaint(2, Color.GREEN);
		renderer.setSeriesPaint(3, Color.ORANGE);
		Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f);
		renderer.setSeriesStroke(0, stroke);
		renderer.setSeriesStroke(1, stroke);
		renderer.setSeriesStroke(2, stroke);
		renderer.setSeriesStroke(3, stroke);

		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setSeriesShapesVisible(2, true);
		renderer.setSeriesShapesVisible(3, true);

		plot.getDomainAxis().setVisible(true);
		plot.getRangeAxis().setVisible(true);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		chart.setBackgroundPaint(new Color(230, 230, 230));

		return chart;
	}

	public List<ProjectStatistics> fillStatistics(List<ProjectStatistics> oldStatistics, Date startDate, Date endDate) {
		List<ProjectStatistics> statistics = new ArrayList<ProjectStatistics>();
		Map<String, ProjectStatistics> map = new HashMap<String, ProjectStatistics>();

		for (ProjectStatistics oldPS : oldStatistics) {
			String key = dateToString(oldPS.getStartTime());
			ProjectStatistics ps = map.get(key);
			if (ps == null) {
				ps = oldPS;
				map.put(key, ps);
			}
			else {
				ps.setTotal(ps.getTotal() + oldPS.getTotal());
				ps.setFailed(ps.getFailed() + oldPS.getFailed());
				ps.setWarning(ps.getWarning() + oldPS.getWarning());
				ps.setPassed(ps.getPassed() + oldPS.getPassed());
			}
		}
		long days = numberOfDaysBetween(startDate, endDate);
		for (long i = 0; i < days; i++) {
			Date currentDate = subDateDays(endDate, i);
			String key = dateToString(currentDate);
			ProjectStatistics ps;
			if (map.containsKey(key)) {
				ps = map.get(key);
			}
			else
				ps = new ProjectStatistics();
			ps.setStartTime(currentDate);

			statistics.add(ps);
		}
		return statistics;
	}

	public Date subDateDays(Date date, long days) {
		return new Date(date.getTime() - days * 86400000L);
	}

	public long numberOfDaysBetween(Date d1, Date d2) {
		d1 = ridOfTime(d1);
		d2 = ridOfTime(d2);
		long diff = d2.getTime() - d1.getTime();

		return diff / 86400000L;
	}

	public String dateToString(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.YEAR) + "-" + c1.get(Calendar.MONTH) + "-" + c1.get(Calendar.DAY_OF_MONTH);
	}

	public Date ridOfTime(Date date) {
		Calendar c1 = Calendar.getInstance();

		c1.setTime(date);

		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		return c1.getTime();
	}

	public boolean isSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(d1);
		c2.setTime(d2);

		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		if (c1.compareTo(c2) == 0) {
			return true;
		}
		return false;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectStatisticsDAO getProjectStatisticsDAO() {
		return projectStatisticsDAO;
	}

	public void setProjectStatisticsDAO(ProjectStatisticsDAO projectStatisticsDAO) {
		this.projectStatisticsDAO = projectStatisticsDAO;
	}

	public static void main(String[] args) throws ParseException, IOException {
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		TimeSeries totalSet = new TimeSeries("Total");
		TimeSeries failedSet = new TimeSeries("Failed");
		TimeSeries passedSet = new TimeSeries("Passed");
		TimeSeries warningSet = new TimeSeries("Warning");

		Random rnd = new Random();

		totalSet.add(new Day(10, 10, 2010), rnd.nextInt(100));
		totalSet.add(new Day(9, 10, 2010), rnd.nextInt(100));
		totalSet.add(new Day(8, 10, 2010), rnd.nextInt(100));
		totalSet.add(new Day(7, 10, 2010), rnd.nextInt(100));

		failedSet.add(new Day(10, 10, 2010), rnd.nextInt(100));
		failedSet.add(new Day(9, 10, 2010), rnd.nextInt(100));
		failedSet.add(new Day(8, 10, 2010), rnd.nextInt(100));
		failedSet.add(new Day(7, 10, 2010), rnd.nextInt(100));

		passedSet.add(new Day(10, 10, 2010), rnd.nextInt(100));
		passedSet.add(new Day(9, 10, 2010), rnd.nextInt(100));
		passedSet.add(new Day(8, 10, 2010), rnd.nextInt(100));
		passedSet.add(new Day(7, 10, 2010), rnd.nextInt(100));

		warningSet.add(new Day(10, 10, 2010), rnd.nextInt(100));
		warningSet.add(new Day(9, 10, 2010), rnd.nextInt(100));
		warningSet.add(new Day(8, 10, 2010), rnd.nextInt(100));
		warningSet.add(new Day(7, 10, 2010), rnd.nextInt(100));

		dataset.addSeries(totalSet);
		dataset.addSeries(passedSet);
		dataset.addSeries(failedSet);
		dataset.addSeries(warningSet);

		JFreeChart chart = ChartFactory.createTimeSeriesChart("dffasfsaf", "Date", "Amount of Tests", dataset, true, true, false);

		XYPlot plot = chart.getXYPlot();

		// XYItemRenderer renderer = plot.getRenderer();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		renderer.setSeriesPaint(0, Color.BLUE);
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesPaint(2, Color.RED);
		renderer.setSeriesPaint(3, Color.ORANGE);
		Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f);
		renderer.setSeriesStroke(0, stroke);
		renderer.setSeriesStroke(1, stroke);
		renderer.setSeriesStroke(2, stroke);
		renderer.setSeriesStroke(3, stroke);

		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setSeriesShapesVisible(2, true);
		renderer.setSeriesShapesVisible(3, true);

		plot.getDomainAxis().setVisible(true);
		plot.getRangeAxis().setVisible(true);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		chart.setBackgroundPaint(new Color(230, 230, 230));

		ChartUtilities.saveChartAsPNG(new File("d:\\temp\\chart.png"), chart, 500, 300);

	}
}
