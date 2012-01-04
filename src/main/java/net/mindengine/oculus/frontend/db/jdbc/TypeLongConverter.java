package net.mindengine.oculus.frontend.db.jdbc;

public class TypeLongConverter extends TypeConverter {
	@Override
	public Object convert(String value) {
		Long i = new Long(value);
		return i;
	}

}
