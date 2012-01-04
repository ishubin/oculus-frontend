package net.mindengine.oculus.frontend.utils;

public class SqlUtils {
	public static String escape(String text) {
		// TODO Escape sql symbols
		text.replace("\\", "\\\\");
		text.replace("'", "\\'");
		return text;
	}
}
