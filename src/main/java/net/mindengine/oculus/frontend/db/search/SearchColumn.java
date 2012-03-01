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
package net.mindengine.oculus.frontend.db.search;

public class SearchColumn {
	private Integer id;
	private String name;
	private String fieldName;
	private String sqlColumn;
	private Boolean show = true;
	private Boolean isSet = false;

	public Boolean getIsSet() {
		return isSet;
	}

	public void setIsSet(Boolean isSet) {
		this.isSet = isSet;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public SearchColumn(String name, String fieldName) {
		super();
		this.name = name;
		this.fieldName = fieldName;
	}

	public SearchColumn() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSqlColumn() {
		return sqlColumn;
	}

	public void setSqlColumn(String sqlColumn) {
		this.sqlColumn = sqlColumn;
	}

}
