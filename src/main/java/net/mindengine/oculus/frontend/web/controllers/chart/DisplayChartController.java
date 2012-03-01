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
package net.mindengine.oculus.frontend.web.controllers.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import net.mindengine.oculus.frontend.service.chart.ChartDAO;
import net.mindengine.oculus.experior.chart.BarChart;
import net.mindengine.oculus.experior.chart.Chart;
import net.mindengine.oculus.experior.chart.LineChart;
import net.mindengine.oculus.experior.chart.PieChart;
import net.mindengine.oculus.experior.chart.data.BarChartData;
import net.mindengine.oculus.experior.chart.data.LineChartDataPoint;
import net.mindengine.oculus.experior.chart.data.LineChartDataSet;
import net.mindengine.oculus.experior.chart.data.PieChartData;
import net.mindengine.oculus.frontend.web.controllers.SimpleViewController;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

public class DisplayChartController extends SimpleViewController {
	private ChartDAO chartDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer width;
		Integer height;

		if (request.getParameter("width") != null) {
			width = Integer.parseInt(request.getParameter("width"));
		}
		else
			width = 500;

		if (request.getParameter("height") != null) {
			height = Integer.parseInt(request.getParameter("height"));
		}
		else
			height = 400;

		Long chartId = Long.parseLong(request.getParameter("chartId"));

		JFreeChart jfreeChart;

		Chart chart = chartDAO.getChart(chartId);

		if (chart.getType().equals(Chart.LINE)) {
			jfreeChart = createLineChart(chart);
		}
		else if (chart.getType().equals(Chart.PIE)) {
			jfreeChart = createPieChart(chart);
		}
		else if (chart.getType().equals(Chart.BAR)) {
			jfreeChart = createBarChart(chart);
		}
		else
			throw new Exception("Invalid chart");
		OutputStream os = response.getOutputStream();
		ChartUtilities.writeChartAsPNG(os, jfreeChart, width, height);
		os.flush();
		os.close();
		return null;
	}

	public JFreeChart createPieChart(Chart chart) throws ParserConfigurationException, SAXException, IOException {
		Map<String, PieChartData> pieces = PieChart.readPieces(chart.getData(), chart.getYAxisType());
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (PieChartData piece : pieces.values()) {
			if (chart.getYAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
				dataset.setValue(piece.getName() + " (" + piece.getValue() + ")", (double) ((Float) piece.getValue()));
			}
			else
				dataset.setValue(piece.getName(), (Number) ((Integer) piece.getValue()));
		}

		JFreeChart jfreeChart = ChartFactory.createPieChart3D(chart.getTitle(), // chart
																				// title
				dataset, // data
				true, // include legend
				true, false);
		Plot plot = jfreeChart.getPlot();

		plot.setBackgroundPaint(Color.WHITE);
		jfreeChart.setBackgroundPaint(new Color(230, 230, 230));

		return jfreeChart;
	}

	public JFreeChart createBarChart(Chart chart) throws ParserConfigurationException, SAXException, IOException {
		BarChart barChart = BarChart.readData(chart.getData(), chart.getYAxisType());
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int category = 0; category < barChart.getCategoryValues().size(); category++) {
			String categoryName = barChart.getCategories().get(category);
			List<Object> values = barChart.getCategoryValues().get(category);
			for (int bar = 0; bar < values.size(); bar++) {
				BarChartData barData = barChart.getBars().get(bar);
				if (chart.getYAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
					dataset.addValue((Float) values.get(bar), barData.getName(), categoryName);
				}
				else
					dataset.addValue((Integer) values.get(bar), barData.getName(), categoryName);
			}
		}

		JFreeChart jfreeChart = ChartFactory.createBarChart3D(chart.getTitle(), chart.getXAxisName(), // domain
																										// axis
																										// label
				chart.getYAxisName(), // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		CategoryPlot plot = (CategoryPlot) jfreeChart.getPlot();

		CategoryItemRenderer renderer = plot.getRenderer();

		int i = 0;
		for (BarChartData bar : barChart.getBars()) {
			renderer.setSeriesPaint(i, Color.decode(bar.getColor()));
			i++;
		}

		plot.getDomainAxis().setVisible(true);
		plot.getRangeAxis().setVisible(true);

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		jfreeChart.setBackgroundPaint(new Color(230, 230, 230));

		return jfreeChart;
	}

	public JFreeChart createLineChart(Chart chart) throws ParserConfigurationException, SAXException, IOException {
		List<LineChartDataSet> dataSets = LineChart.readDataSet(chart.getData(), chart.getXAxisType(), chart.getYAxisType());

		AbstractIntervalXYDataset dataset = null;
		if (chart.getXAxisType().equals(Chart.AXIS_TYPE_DATE)) {
			dataset = new TimeSeriesCollection();
		}
		else {
			dataset = new XYSeriesCollection();
		}
		for (LineChartDataSet lcDataSet : dataSets) {
			if (chart.getXAxisType().equals(Chart.AXIS_TYPE_DATE)) {
				TimeSeries series = new TimeSeries(lcDataSet.getName());
				for (LineChartDataPoint point : lcDataSet.getData()) {
					if (chart.getYAxisType().equals(Chart.AXIS_TYPE_INT)) {
						series.addOrUpdate(new Second((Date) point.getX()), (Integer) point.getY());
					}
					else if (chart.getYAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
						series.addOrUpdate(new Second((Date) point.getX()), (Float) point.getY());
					}
				}
				((TimeSeriesCollection) dataset).addSeries(series);
			}
			else {
				XYSeries series = new XYSeries(lcDataSet.getName());
				for (LineChartDataPoint point : lcDataSet.getData()) {
					if (chart.getYAxisType().equals(Chart.AXIS_TYPE_INT)) {
						if (chart.getXAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
							series.add((Float) point.getX(), (Integer) point.getY());
						}
						else
							series.add((Integer) point.getX(), (Integer) point.getY());

					}
					else if (chart.getYAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
						if (chart.getXAxisType().equals(Chart.AXIS_TYPE_FLOAT)) {
							series.add((Float) point.getX(), (Float) point.getY());
						}
						else
							series.add((Integer) point.getX(), (Float) point.getY());
					}
				}
				((XYSeriesCollection) dataset).addSeries(series);
			}
		}
		JFreeChart jfreeChart = null;
		if (dataset instanceof TimeSeriesCollection) {
			jfreeChart = ChartFactory.createTimeSeriesChart(chart.getTitle(), chart.getXAxisName(), chart.getYAxisName(), dataset, true, true, false);
		}
		else {
			jfreeChart = ChartFactory.createXYLineChart(chart.getTitle(), chart.getXAxisName(), chart.getYAxisName(), dataset, PlotOrientation.VERTICAL, true, true, false);
		}
		XYPlot plot = jfreeChart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);

		int i = 0;
		Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f);

		for (LineChartDataSet lcDataSet : dataSets) {
			Color color = Color.decode(lcDataSet.getColor());
			renderer.setSeriesPaint(i, color);
			renderer.setSeriesStroke(i, stroke);
			i++;
		}
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.gray);
		plot.setDomainGridlinePaint(Color.gray);
		jfreeChart.setBackgroundPaint(new Color(240, 240, 240));
		return jfreeChart;
	}

	public void setChartDAO(ChartDAO chartDAO) {
		this.chartDAO = chartDAO;
	}

	public ChartDAO getChartDAO() {
		return chartDAO;
	}
}
