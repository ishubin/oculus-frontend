package net.mindengine.oculus.frontend.db.jdbc;

public class TypeIntegerConverter extends TypeConverter {
	@Override
	public Object convert(String value) {
		Integer i = new Integer(value);
		return i;
	}

}
