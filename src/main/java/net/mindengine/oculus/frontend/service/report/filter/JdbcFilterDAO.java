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
package net.mindengine.oculus.frontend.service.report.filter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import net.mindengine.oculus.frontend.db.jdbc.MySimpleJdbcDaoSupport;
import net.mindengine.oculus.frontend.domain.report.filter.Filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcFilterDAO extends MySimpleJdbcDaoSupport implements FilterDAO {
	private Log logger = LogFactory.getLog(getClass());

	@Override
	public long createFilter(Filter filter) throws Exception {
		String sql = "insert into filters (name, description, user_id, date, filter) values (?,?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ps.setString(1, filter.getName());
		ps.setString(2, filter.getDescription());
		ps.setLong(3, filter.getUserId());
		ps.setTimestamp(4, new Timestamp(filter.getDate().getTime()));
		ps.setString(5, filter.getFilter());

		logger.info(ps);
		ps.execute();

		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next()) {
			return rs.getLong(1);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Filter> getUserFilters(long userId) throws Exception {
		List<Filter> filters = (List<Filter>) query("select * from filters where user_id = :userId", Filter.class, "userId", userId);

		return filters;
	}

	@Override
	public void deleteFilter(long filterId) throws Exception {
		update("delete from filters where id = :id", "id", filterId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Filter getFilter(long filterId) throws Exception {
		List<Filter> filters = (List<Filter>) query("select * from filters where id = :id", Filter.class, "id", filterId);
		if (filters.size() > 0)
			return filters.get(0);
		return null;
	}

}
