package net.mindengine.oculus.frontend.domain.run;

public class CronIctTestRun {
	private Long id;
	private Long testRunId;
	private Long suiteRunId;
	private Long testId;
	private String reason;
	private String suiteRunParameters;
	private Long issueCollationId;
	private String testName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestRunId() {
		return testRunId;
	}

	public void setTestRunId(Long testRunId) {
		this.testRunId = testRunId;
	}

	public Long getSuiteRunId() {
		return suiteRunId;
	}

	public void setSuiteRunId(Long suiteRunId) {
		this.suiteRunId = suiteRunId;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSuiteRunParameters() {
		return suiteRunParameters;
	}

	public void setSuiteRunParameters(String suiteRunParameters) {
		this.suiteRunParameters = suiteRunParameters;
	}

	public void setIssueCollationId(Long issueCollationId) {
		this.issueCollationId = issueCollationId;
	}

	public Long getIssueCollationId() {
		return issueCollationId;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestName() {
		return testName;
	}
}
