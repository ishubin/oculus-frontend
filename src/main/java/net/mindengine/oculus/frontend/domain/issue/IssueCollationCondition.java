package net.mindengine.oculus.frontend.domain.issue;

public class IssueCollationCondition {
	private Long trmPropertyId;
	private String value;
	private Long issueCollationId;
	private String trmPropertyName;

	public Long getTrmPropertyId() {
		return trmPropertyId;
	}

	public void setTrmPropertyId(Long trmPropertyId) {
		this.trmPropertyId = trmPropertyId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getIssueCollationId() {
		return issueCollationId;
	}

	public void setIssueCollationId(Long issueCollationId) {
		this.issueCollationId = issueCollationId;
	}

	public void setTrmPropertyName(String trmPropertyName) {
		this.trmPropertyName = trmPropertyName;
	}

	public String getTrmPropertyName() {
		return trmPropertyName;
	}
}
