package net.mindengine.oculus.frontend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class JSONUtils {
	public static Long readInteger(JSONValue value) {
		if (value == null || value instanceof JSONNull)
			return null;
		JSONInteger js = (JSONInteger) value;
		return js.getValue().longValue();
	}

	public static String readString(JSONValue value) {
		if (value == null || value instanceof JSONNull)
			return null;
		JSONString js = (JSONString) value;
		return js.getValue();
	}

	public static Date readDate(JSONValue value) throws ParseException {
		if (value == null || value instanceof JSONNull)
			return null;
		JSONString js = (JSONString) value;
		String str = js.getValue();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(str);

	}

	public static boolean isArrayEmpty(JSONValue array) {
		if (array == null || array instanceof JSONNull)
			return true;
		JSONArray arr = (JSONArray) array;
		if (arr.size() == 0)
			return true;
		return false;
	}
}
