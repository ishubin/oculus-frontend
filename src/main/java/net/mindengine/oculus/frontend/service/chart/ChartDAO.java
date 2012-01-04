package net.mindengine.oculus.frontend.service.chart;

import net.mindengine.oculus.experior.chart.Chart;

public interface ChartDAO {
	public long createChart(Chart chart) throws Exception;

	public Chart getChart(long id) throws Exception;
}
