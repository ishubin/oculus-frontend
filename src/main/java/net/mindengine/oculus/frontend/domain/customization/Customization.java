package net.mindengine.oculus.frontend.domain.customization;

import java.util.Collection;

public class Customization {
	public static final String UNIT_PROJECT = "project".intern();
	public static final String UNIT_TEST = "test".intern();
	public static final String UNIT_TEST_CASE = "test-case".intern();
	public static final String UNIT_ISSUE = "issue".intern();
	public static final String UNIT_BUILD = "build".intern();

	public static final String TYPE_TEXT = "text".intern();
	public static final String TYPE_ASSIGNEE = "assignee".intern();
	public static final String TYPE_LIST = "list".intern();
	public static final String TYPE_CHECKBOX = "checkbox".intern();
	public static final String TYPE_CHECKLIST = "checklist".intern();

	public static final String LIST_SUBTYPE_DROPDOWN = "drop-down".intern();
	public static final String LIST_SUBTYPE_LIST = "list".intern();

	private Long id;
	private String unit;
	private Long projectId;
	private String name;
	private String description;
	private String subtype;
	private String type;
	private String groupName;
	
	/*
	 * Not used in JDBC components. Only used for temporary variables somewhere
	 */
	private Collection<CustomizationPossibleValue> possibleValues;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

    public void setPossibleValues(Collection<CustomizationPossibleValue> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public Collection<CustomizationPossibleValue> getPossibleValues() {
        return possibleValues;
    }
}
