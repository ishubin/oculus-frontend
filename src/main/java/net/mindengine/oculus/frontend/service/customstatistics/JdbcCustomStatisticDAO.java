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
package net.mindengine.oculus.frontend.service.customstatistics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcCustomStatisticDAO extends MySimpleJdbcDaoSupport implements CustomStatisticDAO {

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public void changeCustomStatistic(CustomStatistic statistic) throws Exception {
		if(statistic==null){
			throw new IllegalArgumentException("Statistics shouldn't be null");
		}
		if(statistic.getId()==null || statistic.getId()<1){
			throw new IllegalArgumentException("Id should be positive long value");
		}
		update("update custom_statistics set name = :name, short_name = :shortName, description = :description, value_type =:valueType where id = :id", "name", statistic.getName(),"shortName",statistic.getShortName(), "description", statistic.getDescription(), "valueType", statistic.getValueType(), "id", statistic.getId());
	}

	@Override
	public long createCustomStatistic(CustomStatistic statistic) throws Exception {
		if (statistic == null)
			throw new IllegalArgumentException("Statistic should not be null");
		PreparedStatement ps = getConnection().prepareStatement("insert into custom_statistics (name, description, project_id, user_id, created_date, value_type, short_name) values (?,?,?,?,?,?,?)");

		ps.setString(1, statistic.getName());
		ps.setString(2, statistic.getDescription());
		ps.setLong(3, statistic.getProjectId());
		ps.setLong(4, statistic.getUserId());
		ps.setTimestamp(5, new Timestamp(new Date().getTime()));
		ps.setString(6, statistic.getValueType());
		ps.setString(7, statistic.getShortName());
		logger.info(ps);
		ps.execute();
		
		ResultSet rs = ps.getGeneratedKeys();
		
  		if (rs.next()) {
			return rs.getLong(1); 
		}
		else throw new Exception("Couldn't fetch custom statistics id");
	}

	@Override
	public CustomStatistic getCustomStatistic(long id) throws Exception {
		List<CustomStatistic> list = query("select * from custom_statistics where id = :id", CustomStatistic.class, "id", id);

		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

    
    @Override
    public Collection<CustomStatistic> getStatisticsByProject(Long projectId) throws Exception {
        return query("select * from custom_statistics where project_id = :projectId", CustomStatistic.class, "projectId", projectId);
    }

	@Override
    public void deleteCustomStatistic(long id) throws Exception {
		update("delete from custom_statistics where id = :id", "id", id);
		
		update("delete from custom_statistic_parameters where custom_statistic_id = :id", "id", id);
		
		update("delete from custom_statistic_data_parameters where custom_statistic_data_id in (select id from custom_statistic_data where custom_statistic_id = "+id+")");
		
		update("delete from custom_statistic_data where custom_statistic_id = 3");
		
    }

	@Override
    public long createCustomStatisticParameter(CustomStatisticParameter customStatisticParameter) throws Exception {
	    if (customStatisticParameter == null)
            throw new IllegalArgumentException("Parameter should not be null");
        PreparedStatement ps = getConnection().prepareStatement("insert into custom_statistic_parameters (name, custom_statistic_id, description, short_name) values (?,?,?,?)");

        ps.setString(1, customStatisticParameter.getName());
        ps.setLong(2, customStatisticParameter.getCustomStatisticId());
        ps.setString(3, customStatisticParameter.getDescription());
        ps.setString(4, customStatisticParameter.getShortName());
        logger.info(ps);
        ps.execute();
        
        ResultSet rs = ps.getGeneratedKeys();
        
        if (rs.next()) {
            return rs.getLong(1); 
        }
        else throw new Exception("Couldn't fetch custom statistic parameter id");
    }

    @Override
    public void changeCustomStatisticParameter(CustomStatisticParameter parameter) throws Exception {
        if(parameter.getId()==null){
            throw new IllegalArgumentException("id of parameter is not defined");
        }
        
        update("update custom_statistic_parameters set name=:name, description=:description, short_name=:shortName where id=:id",
                "name", parameter.getName(),
                "description", parameter.getDescription(),
                "id", parameter.getId(),
                "shortName", parameter.getShortName());
    }

    @Override
    public void deleteCustomStatisticParameter(Long id) throws Exception {
        update("delete from custom_statistic_parameters where id="+id);
        
        update("delete from custom_statistic_data_parameters where custom_statistic_parameter_id = "+id);
    }

    @Override
    public CustomStatisticParameter getCustomStatisticParameter(Long id) throws Exception {
        List<?> list = query("select * from custom_statistic_parameters where id = :id", CustomStatisticParameter.class, "id", id);

        if (list.size() == 1) {
            return (CustomStatisticParameter) list.get(0);
        }
        return null;
    }

    @Override
    public Collection<CustomStatisticParameter> getParametersByStatistic(Long statisticId) throws Exception {
        return query("select * from custom_statistic_parameters where custom_statistic_id = :statisticId", CustomStatisticParameter.class, "statisticId", statisticId);
    }

    
    @Override
    public long createCustomStatisticChart(CustomStatisticChart chart) throws Exception {
        
        PreparedStatement ps = getConnection().prepareStatement("insert into custom_statistic_charts (custom_statistic_id, name, parameters, type, date) values (?,?,?,?,?)");
        
        ps.setLong(1, chart.getCustomStatisticId());
        ps.setString(2, chart.getName());
        ps.setString(3, chart.getParameters());
        ps.setString(4, chart.getType());
        ps.setTimestamp(5, new Timestamp(chart.getDate().getTime()));
        
        logger.info(ps.execute());
        
        ResultSet rs = ps.getGeneratedKeys();
        
        if(rs.next()){
            return rs.getLong(1);
        }
        return 0;
    }
    
    @Override
    public void changeCustomStatisticChart(CustomStatisticChart chart) throws Exception {
        update("update custom_statistic_charts set name =:name, type=:type where id = "+chart.getId(),"name", chart.getName(), "type", chart.getType());
    }
    
    @Override
    public void changeCustomStatisticChartSettings(Long id, String parameters) throws Exception {
        update("update custom_statistic_charts set parameters=:parameters where id = "+id, "parameters", parameters);
    }

    @Override
    public void deleteCustomStatisticChart(Long id) throws Exception {
        update("delete from custom_statistic_charts where id = "+id);
    }

    @Override
    public CustomStatisticChart getCustomStatisticChart(Long id) throws Exception {
        return querySingle("select * from custom_statistic_charts where id = "+id, CustomStatisticChart.class);
    }

    @Override
    public Collection<CustomStatisticChart> getCustomStatisticCharts(Long customStatisticId) throws Exception {
        return query("select * from custom_statistic_charts where custom_statistic_id = "+customStatisticId, CustomStatisticChart.class);
    }

    @Override
    public Collection<String> getParameterValues(Long parameterId) throws Exception {
        /*
         * For now will limit the output only to 300 items as it will be displayed in drop-down list control
         * But later it will be better to fetch items with pagination and simple search functionality
         */
        return queryStrings("select value from custom_statistic_data_parameters where custom_statistic_parameter_id = "+parameterId+" group by value order by value asc limit 0,300");
    }
}



