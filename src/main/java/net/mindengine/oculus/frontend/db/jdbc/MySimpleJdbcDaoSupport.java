package net.mindengine.oculus.frontend.db.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 * An entry point for the JDBC DAO support. This class should be a parent to all
 * DAO classes as it provides with all needed JDBC mapping activities.
 * 
 * @author Ivan Shubin
 * 
 */
public class MySimpleJdbcDaoSupport extends SimpleJdbcDaoSupport {
	protected BeanMappingFactory beanMappingFactory;
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * Executes a sql query and converts the JDBC ResultSet to a list of java
	 * bean objects.
	 * 
	 * @param sql
	 *            The SQL command string
	 * @param mapperClass
	 *            The end-point class of a java bean
	 * @param args
	 *            An array of pairs of name and value for the
	 *            {@link MapSqlParameterSource}
	 * @return A list of mapped java bean objects.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public<T> List<T> query(String sql, Class<T> mapperClass, Object... args) throws Exception {
		logQuery(sql, args);
		if (args == null)
			return (List<T>) getSimpleJdbcTemplate().query(sql, beanMappingFactory.getBeanMapper(mapperClass));
		MapSqlParameterSource map = new MapSqlParameterSource();
		for (int i = 0; i < args.length; i += 2) {
			map.addValue((String) args[i], args[i + 1]);
		}
		return (List<T>) getSimpleJdbcTemplate().query(sql, beanMappingFactory.getBeanMapper(mapperClass), map);
	}
	
	/**
	 * Returns a collection of string values 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Collection<String> queryStrings(String sql) throws Exception {
        PreparedStatement ps = getConnection().prepareStatement(sql);
        logger.info(ps);
        
        ps.execute();
        ResultSet rs = ps.getResultSet();
        
        Collection<String> results = new LinkedList<String>();
        while(rs.next()){
            results.add(rs.getString(1));
        }
        return results;
    }
    
	
	public<T> T querySingle(String sql, Class<T> mapperClass, Object... args) throws Exception {
        
	    List<T> results = query(sql, mapperClass, args);
	    
	    if(results.size()>0){
	        return results.get(0);
	    }
	    return null;
    }
	

	/**
	 * Executes the SQL command and returns the long value of counts of entities
	 * 
	 * @param sql
	 *            SQL command string
	 * @param args
	 *            An array of pairs of name and value for the
	 *            {@link MapSqlParameterSource}
	 * @return
	 * @throws Exception
	 */
	public Long count(String sql, Object... args) throws Exception {
		MapSqlParameterSource map = new MapSqlParameterSource();
		for (int i = 0; i < args.length; i += 2) {
			map.addValue((String) args[i], args[i + 1]);
		}
		return getSimpleJdbcTemplate().queryForLong(sql, map);
	}

	/**
	 * Executes the SQL command for updating the entities in DB.
	 * 
	 * @param sql
	 *            SQL command string
	 * @param args
	 *            An array of pairs of name and value for the
	 *            {@link MapSqlParameterSource}
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, Object... args) throws Exception {
		logQuery(sql, args);
		if (args == null)
			return getSimpleJdbcTemplate().update(sql);

		MapSqlParameterSource map = new MapSqlParameterSource();
		for (int i = 0; i < args.length; i += 2) {
			map.addValue((String) args[i], args[i + 1]);
		}
		return getSimpleJdbcTemplate().update(sql, map);
	}

	public BeanMappingFactory getBeanMappingFactory() {
		return beanMappingFactory;
	}

	public void setBeanMappingFactory(BeanMappingFactory beanMappingFactory) {
		this.beanMappingFactory = beanMappingFactory;
	}

	/**
	 * Used for logging the SQL command string
	 * 
	 * @param sql
	 *            SQL command string
	 * @param args
	 *            An array of pairs of name and value for the
	 *            {@link MapSqlParameterSource}
	 */
	private void logQuery(String sql, Object[] args) {
		if (args != null) {

		}
		for (int i = 0; i < args.length - 1; i += 2) {
			sql = sql.replace(":" + args[i], "" + args[i + 1]);
		}

		logger.info(sql);
	}
}
