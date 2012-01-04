package net.mindengine.oculus.frontend.domain.issue;

public class IssueSearchData extends Issue {
	private String userName;
	private String userLogin;
	private String projectName;
	private String projectPath;
	private String subProjectName;
	private String subProjectPath;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
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

	public String getSubProjectName() {
		return subProjectName;
	}

	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
	}

	public String getSubProjectPath() {
		return subProjectPath;
	}

	public void setSubProjectPath(String subProjectPath) {
		this.subProjectPath = subProjectPath;
	}
}
