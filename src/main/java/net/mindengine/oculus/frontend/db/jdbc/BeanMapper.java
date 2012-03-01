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
package net.mindengine.oculus.frontend.db.jdbc;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * This is a Spring row mapper which uses the specified Java beans for mapping
 * the JDBC Result set to a Java object.
 * 
 * @author Ivan Shubin
 * 
 */
public class BeanMapper implements ParameterizedRowMapper<Object> {
	protected Log logger = LogFactory.getLog(getClass());

	private String className;
	private Map<String, FieldMapper> fields;

	public Map<String, FieldMapper> getFields() {
		return fields;
	}

	public void setFields(Map<String, FieldMapper> fields) {
		this.fields = fields;
	}

	public String createSetter(String field) {
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	public static void main(String[] args) {

	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();
		try {
			Class<?> clazz = Class.forName(className);

			Object obj = clazz.newInstance();
			for (int i = 1; i <= columns; i++) {
				String column = metaData.getColumnName(i);
				FieldMapper fm = fields.get(column);
				if (fm != null) {
					Method method = fm.getMethod();

					String typeName = fm.getType().getSimpleName();

					typeName = StringUtils.capitalize(typeName);
					if (typeName.endsWith("[]")) {
						typeName = typeName.substring(0, typeName.length() - 2) + "s";
					}
					if (typeName.equals("Date")) {
						typeName = "Timestamp";
					}
					if (typeName.equals("Integer")) {
						typeName = "Int";
					}

					Method rsMethod = ResultSet.class.getMethod("get" + typeName, String.class);
					Object value = rsMethod.invoke(rs, column);

					if (value instanceof Timestamp) {
						Timestamp timestamp = (Timestamp) value;
						value = new Date(timestamp.getTime());
					}
					method.invoke(obj, value);
				}
			}
			return obj;
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;

	}

}
