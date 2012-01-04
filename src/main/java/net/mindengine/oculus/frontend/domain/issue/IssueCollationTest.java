package net.mindengine.oculus.frontend.domain.issue;

public class IssueCollationTest {

	private Long id;
	private Long issueCollationId;
	private Long testId;
	private String testName;
	private String projectPath;
	private String projectName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public void setIssueCollationId(Long issueCollationId) {
		this.issueCollationId = issueCollationId;
	}

	public Long getIssueCollationId() {
		return issueCollationId;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}
}
