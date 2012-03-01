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
package net.mindengine.oculus.frontend.domain.project;

public class ProjectBrowseFilter {
	public static final Integer[] PAGE_LIMITS = new Integer[] { 10, 20, 30, 50, 100, 200, 300, 500, 1000 };

	public Integer[] getPageLimitArray() {
		return PAGE_LIMITS;
	}

	private String orderBy;
	private Integer orderDirection;

	private Boolean onlyRoot = false;

	private Integer pageLimit = 0;
	private Integer pageOffset = 1;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(Integer orderDirection) {
		this.orderDirection = orderDirection;
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

	public void setOnlyRoot(Boolean onlyRoot) {
		this.onlyRoot = onlyRoot;
	}

	public Boolean getOnlyRoot() {
		return onlyRoot;
	}
}
