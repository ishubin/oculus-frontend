package net.mindengine.oculus.frontend.service.customstatistics;

import java.util.Collection;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;

public interface CustomStatisticDAO {

	public long createCustomStatistic(CustomStatistic statistic) throws Exception;

	public CustomStatistic getCustomStatistic(long id) throws Exception;

	public void changeCustomStatistic(CustomStatistic statistic) throws Exception;
	
	public Collection<CustomStatistic> getStatisticsByProject(Long projectId) throws Exception;
	
	public void deleteCustomStatistic(long id) throws Exception;
	
	public long createCustomStatisticParameter(CustomStatisticParameter customStatisticParameter) throws Exception;
	
	public CustomStatisticParameter getCustomStatisticParameter(Long id) throws Exception;
	
	public void changeCustomStatisticParameter(CustomStatisticParameter parameter) throws Exception;
	
	public Collection<CustomStatisticParameter> getParametersByStatistic(Long statisticId) throws Exception;
	
	public void deleteCustomStatisticParameter(Long id) throws Exception;

	public long createCustomStatisticChart(CustomStatisticChart chart) throws Exception;
	
	public void deleteCustomStatisticChart(Long id) throws Exception;
	
	public void changeCustomStatisticChart(CustomStatisticChart chart) throws Exception;
	
	public void changeCustomStatisticChartSettings(Long id, String parameters) throws Exception;
	
	public CustomStatisticChart getCustomStatisticChart(Long id) throws Exception;
	
	public Collection<CustomStatisticChart> getCustomStatisticCharts(Long customStatisticId) throws Exception;

	/**
	 * Fetches all possible values for custom statistic parameter
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getParameterValues(Long parameterId) throws Exception;
}
