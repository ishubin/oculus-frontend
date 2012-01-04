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
