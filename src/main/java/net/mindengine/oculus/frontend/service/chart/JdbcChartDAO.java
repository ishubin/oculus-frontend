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
package net.mindengine.oculus.frontend.service.chart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.experior.chart.Chart;

public class JdbcChartDAO extends MySimpleJdbcDaoSupport implements ChartDAO {

	@Override
	public long createChart(Chart chart) throws Exception {
		PreparedStatement ps = getConnection().prepareStatement("insert into charts (title, type, x_axis_name, x_axis_type, y_axis_name, y_axis_type, data) values (?,?,?,?,?,?,?)");

		ps.setString(1, chart.getTitle());
		ps.setString(2, chart.getType());
		ps.setString(3, chart.getXAxisName());
		ps.setString(4, chart.getXAxisType());
		ps.setString(5, chart.getYAxisName());
		ps.setString(6, chart.getYAxisType());
		ps.setString(7, chart.getData());

		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		long chartId = 0;
		if (rs.next()) {
			chartId = rs.getLong(1);
		}
		return chartId;
	}

	@Override
	public Chart getChart(long id) throws Exception {
		List<?> chartList = query("select * from charts where id = :id", Chart.class, "id", id);

		if (chartList.size() == 1) {
			return (Chart) chartList.get(0);
		}
		return null;
	}

}
