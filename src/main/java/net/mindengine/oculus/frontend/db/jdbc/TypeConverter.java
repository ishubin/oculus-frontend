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
