package net.mindengine.oculus.frontend.domain.document;

import java.util.Collection;
import java.util.Date;

public class Document {
	public static final String _TYPE_FILE = "file";

	private Long id;
	private String name;
	private String description;
	private String content;
	private String type;
	private Long projectId;
	private Long folderId;
	private Date date;
	private Long userId;
	private String branch;
	private String typeExtended;

	private String parentProjectName;
	private String parentProjectPath;
	private String projectName;
	private String projectPath;

	private String authorLogin;
	private String authorName;

	private Long size = 0l;

	private Collection<DocumentAttachment> attachments;
	
	public Document() {
		date = new Date();
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setTypeExtended(String typeExtended) {
		this.typeExtended = typeExtended;
	}

	public String getTypeExtended() {
		return typeExtended;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getSize() {
		return size;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public String getParentProjectPath() {
		return parentProjectPath;
	}

	public void setParentProjectPath(String parentProjectPath) {
		this.parentProjectPath = parentProjectPath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getAuthorLogin() {
		return authorLogin;
	}

	public void setAuthorLogin(String authorLogin) {
		this.authorLogin = authorLogin;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

    public void setAttachments(Collection<DocumentAttachment> attachments) {
        this.attachments = attachments;
    }

    public Collection<DocumentAttachment> getAttachments() {
        return attachments;
    }
}
