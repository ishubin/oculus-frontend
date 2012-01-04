package net.mindengine.oculus.frontend.db.search;

import java.util.ArrayList;
import java.util.List;

import net.mindengine.oculus.frontend.utils.SqlUtils;


public class SqlSearchCondition {

	private List<String> conditions = new ArrayList<String>();

	public SqlSearchCondition() {

	}

	public void append(String condition) {
		conditions.add(condition);
	}

	public String generateConditionString() {
		StringBuffer buffer = new StringBuffer(" where ");
		boolean bFirst = true;
		for (String condition : conditions) {
			if (!bFirst) {
				buffer.append(" and ");
			}
			buffer.append("(");
			buffer.append(condition);
			buffer.append(")");
			bFirst = false;
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		if (conditions.size() == 0) {
			return "";
		}
		else {

			return generateConditionString();
		}
	}

	/**
	 * @param columns
	 *            - the pair of column name and value for it
	 */
	public String createSimpleCondition(Boolean checkRegEx, String... columns) {
		String condition = "";
		for (int i = 0; i < columns.length; i += 2) {
			if (i > 0)
				condition += " or ";
			String name = columns[i];
			String value = columns[i + 1];
			if (checkRegEx && value.contains("*")) {
				String convertedValue = value.replace("*", "%");
				condition += name + " like '" + SqlUtils.escape(convertedValue) + "'";
			}
			else {
				condition += name + " = '" + SqlUtils.escape(value) + "'";
			}
		}
		return condition;
	}

	public String createSimpleCondition(String value, Boolean checkRegEx, String... columnNames) {

		if (checkRegEx && value.contains("*")) {
			String convertedValue = value.replace("*", "%");
			// use like
			String condition = "";
			for (int i = 0; i < columnNames.length; i++) {
				if (i > 0)
					condition += " or ";
				condition += columnNames[i] + " like '" + SqlUtils.escape(convertedValue) + "'";
			}
			return condition;
		}
		else {
			// use equals
			String condition = "";
			for (int i = 0; i < columnNames.length; i++) {
				if (i > 0)
					condition += " or ";
				condition += columnNames[i] + " = '" + SqlUtils.escape(value) + "'";
			}
			return condition;
		}

	}

	/**
	 * Generates the "in" condition for the specified column name
	 * 
	 * @param values
	 *            Values separated by comma
	 * @param columnName
	 * @return
	 */
	public String createArrayCondition(String values, String... columnNames) {
		String[] arr = values.split(",");
		StringBuffer condition = new StringBuffer(" ");
		for (int i = 0; i < columnNames.length; i++) {
			if (i > 0)
				condition.append(" or ");
			condition.append(columnNames[i]);
			condition.append(" in (");
			for (int j = 0; j < arr.length; j++) {
				if (j > 0)
					condition.append(",");
				condition.append("'");
				condition.append(SqlUtils.escape(arr[j].trim()));
				condition.append("'");
			}
			condition.append(") ");
		}

		return condition.toString();
	}
}
