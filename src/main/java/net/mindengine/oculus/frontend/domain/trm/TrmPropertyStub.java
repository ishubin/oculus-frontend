package net.mindengine.oculus.frontend.domain.trm;

import java.util.List;

/**
 * This class is used for saving the property to JSON format with
 * {@link JSONMapper} The instance of this class is filled with data from
 * {@link TrmProperty} instance
 * 
 * @author Ivan Shubin
 * 
 */
public class TrmPropertyStub {
	private Long id;
	private String name;
	private String description;
	private List<String> values;
	private String type;
	private String subtype;
	private Long projectId;

	public TrmPropertyStub() {

	}

	public TrmPropertyStub(TrmProperty trmProperty) {
		this.setId(trmProperty.getId());
		this.setName(trmProperty.getName());
		this.setDescription(trmProperty.getDescription());
		this.setValues(trmProperty.getValuesAsList());
		this.setType(trmProperty.getType());
		this.setSubtype(trmProperty.getSubtype());
		this.setProjectId(trmProperty.getProjectId());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}
