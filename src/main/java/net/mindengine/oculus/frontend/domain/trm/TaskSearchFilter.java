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
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.domain.trm;

import java.util.Collection;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class TaskSearchFilter {
	public static final Integer[] PAGE_LIMITS = new Integer[] { 10, 20, 30, 50, 100, 200, 300, 500, 1000 };

	private Integer orderByColumnId;
	private Integer orderDirection;

	// This value is an id which will be used in PAGE_LIMITS array
	private Integer pageLimit = 0;

	private Integer pageOffset = 1;

	private String name;
	private String user;
	private Boolean onlyShared;

	private Collection<SearchColumn> columns;

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

	public Integer getOrderByColumnId() {
		return orderByColumnId;
	}

	public void setOrderByColumnId(Integer orderByColumnId) {
		this.orderByColumnId = orderByColumnId;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Collection<SearchColumn> getColumns() {
		return columns;
	}

	public void setColumns(Collection<SearchColumn> columns) {
		this.columns = columns;
	}

	public void setOrderDirection(Integer orderDirection) {
		this.orderDirection = orderDirection;
	}

	public Integer getOrderDirection() {
		return orderDirection;
	}

	public void setOnlyShared(Boolean onlyShared) {
		this.onlyShared = onlyShared;
	}

	public Boolean getOnlyShared() {
		return onlyShared;
	}

}
