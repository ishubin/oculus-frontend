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
package net.mindengine.oculus.frontend.domain.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class TestParameter {
	public static final String TYPE_INPUT = "input";
	public static final String TYPE_OUTPUT = "output";

	public static final String CONTROL_TEXT = "text";
	public static final String CONTROL_LIST = "list";
	public static final String CONTROL_BOOLEAN = "boolean";
	public static final String CONTROL_UNDEFINED = "undefined";

	private Long id;
	private String type;
	private String name;
	private String description;
	private String controlType;
	private String defaultValue;

	private transient String possibleValues;
	private List<String> possibleValuesList;
	private Long testId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(String possibleValues) {
		this.possibleValues = possibleValues;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public void setPossibleValuesList(List<String> possibleValuesList) {
		this.possibleValuesList = possibleValuesList;
	}

	public List<String> getPossibleValuesList() {
		if (possibleValuesList == null && possibleValues != null && !possibleValues.isEmpty()) {
			possibleValuesList = new ArrayList<String>();
			String[] pvs = possibleValues.split("<value>");
			for (String pv : pvs) {
				if (!pv.isEmpty()) {
					possibleValuesList.add(StringEscapeUtils.unescapeXml(pv));
				}
			}
		}
		return possibleValuesList;
	}

}
