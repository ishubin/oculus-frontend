package net.mindengine.oculus.frontend.db.jdbc;

import java.sql.Timestamp;
import java.util.Date;

public class TypeDateConverter extends TypeConverter {
	@Override
	public Object convert(String value) {
		return new Date(Timestamp.valueOf(value).getTime());
	}
}
