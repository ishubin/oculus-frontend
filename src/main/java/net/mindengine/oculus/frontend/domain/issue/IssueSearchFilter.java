package net.mindengine.oculus.frontend.domain.issue;

import java.util.Collection;
import java.util.Map;

import net.mindengine.oculus.frontend.db.search.SearchColumn;
import net.mindengine.oculus.frontend.domain.customization.CustomizationCriteria;

public class IssueSearchFilter {
	public static final Integer[] PAGE_LIMITS = new Integer[] { 10, 20, 30, 50, 100, 200, 300, 500, 1000 };
	private String name;
	private String summary;
	private String details;
	private String user;
	private String project;
	private String subProject;
	private Integer pageOffset = 1;
	private Integer orderByColumnId;
	private Integer orderDirection;
	// This value is an id which will be used in PAGE_LIMITS array
	private Integer pageLimit = 0;
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

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}

	public static Integer[] getPageLimits() {
		return PAGE_LIMITS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public void setOrderDirection(Integer orderDirection) {
		this.orderDirection = orderDirection;
	}

	public Integer getOrderDirection() {
		return orderDirection;
	}

	public void setOrderByColumnId(Integer orderByColumnId) {
		this.orderByColumnId = orderByColumnId;
	}

	public Integer getOrderByColumnId() {
		return orderByColumnId;
	}

	public void setColumns(Collection<SearchColumn> columns) {
		this.columns = columns;
	}

	public Collection<SearchColumn> getColumns() {
		return columns;
	}

	public void setCustomizations(Map<Long, CustomizationCriteria> customizations) {
		this.customizations = customizations;
	}

	public Map<Long, CustomizationCriteria> getCustomizations() {
		return customizations;
	}

}
