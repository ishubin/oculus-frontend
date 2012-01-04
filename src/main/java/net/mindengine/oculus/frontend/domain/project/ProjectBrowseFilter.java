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
