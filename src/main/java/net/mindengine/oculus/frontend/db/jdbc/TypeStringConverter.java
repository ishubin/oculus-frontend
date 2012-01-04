package net.mindengine.oculus.frontend.db.jdbc;

public class TypeStringConverter extends TypeConverter {

	@Override
	public Object convert(String value) {
		return value;
	}

}
