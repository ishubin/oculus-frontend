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
package net.mindengine.oculus.frontend.db.jdbc;

import java.lang.reflect.Method;

/**
 * Used for mapping the JDBC ResultSet cell to a specific field of the java
 * bean.
 * 
 * @author Ivan Shubin
 * 
 */
public class FieldMapper {
	/**
	 * A name of the field of a java bean.
	 */
	private String property;
	/**
	 * A name of the column from a JDBC ResultSet
	 */
	private String column;
	/**
	 * A setter method which will be used to set the field value.
	 */
	private Method method;
	/**
	 * Used for converting the String value to a java object field type value.
	 */
	private TypeConverter typeConverter;
	/**
	 * A class of the java object field.
	 */
	private Class<?> type;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
		Class<?>[] types = method.getParameterTypes();
		if (types.length == 1) {
			type = types[0];
			typeConverter = TypeConverter.createConverter(types[0]);
		}
		else
			assert false : "Method " + method.getName() + " hasn't 1 parameter";
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public TypeConverter getTypeConverter() {
		return typeConverter;
	}

	public void setTypeConverter(TypeConverter typeConverter) {
		this.typeConverter = typeConverter;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}
