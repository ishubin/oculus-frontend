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

import java.util.Date;

public abstract class TypeConverter {
	public Object convert(String value) {
		return value;
	}

	public static TypeConverter createConverter(Class<?> clazz) {
		if (clazz == Integer.class)
			return new TypeIntegerConverter();
		if (clazz == String.class)
			return new TypeStringConverter();
		if (clazz == Date.class)
			return new TypeDateConverter();
		if (clazz == Long.class)
			return new TypeLongConverter();
		return null;
	}

}
