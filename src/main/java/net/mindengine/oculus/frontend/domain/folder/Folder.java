package net.mindengine.oculus.frontend.domain.folder;

public class Folder {
	private Long id;
	private Long projectId;
	private Long parentId;
	private String name;
	private String description;
	private Long userId;
	private Long children;
	private Long documents;
	private String branch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setChildren(Long children) {
		this.children = children;
	}

	public Long getChildren() {
		return children;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public void setDocuments(Long documents) {
		this.documents = documents;
	}

	public Long getDocuments() {
		return documents;
	}

}
