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
