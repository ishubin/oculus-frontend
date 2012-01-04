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
