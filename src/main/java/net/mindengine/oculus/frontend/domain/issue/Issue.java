package net.mindengine.oculus.frontend.domain.issue;

import java.util.Date;

public class Issue {
	private Long id;
	private String name;
	private String link;
	private String summary;
	private String description;
	private Long authorId;
	private Date date;
	private Integer fixed;
	private Date fixedDate;
	private Long projectId;
	private Long subProjectId;
	private Long dependentTests;

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getFixed() {
		return fixed;
	}

	public void setFixed(Integer fixed) {
		this.fixed = fixed;
	}

	public Date getFixedDate() {
		return fixedDate;
	}

	public void setFixedDate(Date fixedDate) {
		this.fixedDate = fixedDate;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getSubProjectId() {
		return subProjectId;
	}

	public void setSubProjectId(Long subProjectId) {
		this.subProjectId = subProjectId;
	}

	public void setDependentTests(Long dependentTests) {
		this.dependentTests = dependentTests;
	}

	public Long getDependentTests() {
		return dependentTests;
	}
}
