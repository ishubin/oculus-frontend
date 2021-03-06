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

import java.util.Collection;
import java.util.Map;

import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.domain.customization.CustomizationCriteria;

public class TestSearchFilter {
	public static final Integer[] PAGE_LIMITS = new Integer[] { 10, 20, 30, 50, 100, 200, 300, 500, 1000 };
	private Integer orderByColumnId;
	private Integer orderDirection;

	// This value is an id which will be used in PAGE_LIMITS array
	private Integer pageLimit = 0;

	private Integer pageOffset = 1;

	private String name;
	private String project;
	private String subProject;
	private String designer;
	private String testGroup;
	private Boolean groupBySubProject;
	private String automated;

	private Collection<SearchColumn> columns;
	/**
	 * A set of customization conditions where key is the id of the
	 * customization and value is a set of values
	 */
	private Map<Long, CustomizationCriteria> customizations;

	public Integer[] getPageLimitArray() {
		return PAGE_LIMITS;
	}

	public SearchColumn getColumnById(Integer id) {
		for (SearchColumn column : columns) {
			if (column.getId().equals(id)) {
				return column;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getSubProject() {
		return subProject;
	}

	public void setSubProject(String subProject) {
		this.subProject = subProject;
	}

	public String getDesigner() {
		return designer;
	}

	public void setDesigner(String designer) {
		this.designer = designer;
	}

	public Map<Long, CustomizationCriteria> getCustomizations() {
		return customizations;
	}

	public void setCustomizations(Map<Long, CustomizationCriteria> customizations) {
		this.customizations = customizations;
	}

	public void setOrderByColumnId(Integer orderByColumnId) {
		this.orderByColumnId = orderByColumnId;
	}

	public Integer getOrderByColumnId() {
		return orderByColumnId;
	}

	public void setOrderDirection(Integer orderDirection) {
		this.orderDirection = orderDirection;
	}

	public Integer getOrderDirection() {
		return orderDirection;
	}

	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setGroupBySubProject(Boolean groupBySubProject) {
		this.groupBySubProject = groupBySubProject;
	}

	public Boolean getGroupBySubProject() {
		return groupBySubProject;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setColumns(Collection<SearchColumn> columns) {
		this.columns = columns;
	}

	public Collection<SearchColumn> getColumns() {
		return columns;
	}

	public void setTestGroup(String testGroup) {
		this.testGroup = testGroup;
	}

	public String getTestGroup() {
		return testGroup;
	}

    public String getAutomated() {
        return automated;
    }

    public void setAutomated(String automated) {
        this.automated = automated;
    }
}
