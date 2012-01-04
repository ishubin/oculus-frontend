package net.mindengine.oculus.frontend.domain.customstatistics;

import java.util.Date;

public class CustomStatistic {
	
	public static final String VALUE_TYPE_TIME = "time".intern();
	public static final String VALUE_TYPE_NUMBER = "number".intern();
	
	private Long id;
	private String name;
	private String description;
	private Long projectId;
	private Long userId;
	private Date createdDate;
	private String valueType;
	private String shortName;
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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setValueType(String valueType) {
	    this.valueType = valueType;
    }

	public String getValueType() {
	    return valueType;
    }

	public void setShortName(String shortName) {
	    this.shortName = shortName;
    }

	public String getShortName() {
	    return shortName;
    }

}
