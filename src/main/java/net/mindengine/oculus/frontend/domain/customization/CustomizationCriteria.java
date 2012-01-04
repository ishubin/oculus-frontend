package net.mindengine.oculus.frontend.domain.customization;

import java.util.Collection;

public class CustomizationCriteria {
	public static final Integer FETCH_TYPE_STRICT = 1;
	public static final Integer FETCH_TYPE_AT_LEAST_ONE = 2;
	private Boolean checklist;
	private Collection<String> values;

	/**
	 * Used for declaring the fetching type:
	 * <ul>
	 * <li>1 - Strict - means that all of the specified checklist parameters
	 * should be contained in the required element</li>
	 * <li>2 - At least one - means that at least one of the specified checklist
	 * parameters should be contained in the required element</li>
	 * </ul>
	 */
	private Integer fetchConditionType = FETCH_TYPE_STRICT;

	public CustomizationCriteria() {
		// TODO Auto-generated constructor stub
	}

	public CustomizationCriteria(Collection<String> values) {
		super();
		this.values = values;
	}

	public CustomizationCriteria(Boolean checklist, Collection<String> values) {
		super();
		this.checklist = checklist;
		this.values = values;
	}

	public void setChecklist(Boolean checklist) {
		this.checklist = checklist;
	}

	public Boolean getChecklist() {
		return checklist;
	}

	public void setValues(Collection<String> values) {
		this.values = values;
	}

	public Collection<String> getValues() {
		return values;
	}

	public void setFetchConditionType(Integer fetchConditionType) {
		this.fetchConditionType = fetchConditionType;
	}

	public Integer getFetchConditionType() {
		return fetchConditionType;
	}
}
