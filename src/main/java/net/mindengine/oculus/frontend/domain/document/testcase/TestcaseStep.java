package net.mindengine.oculus.frontend.domain.document.testcase;

public class TestcaseStep {
	private String action;
	private String expected;
	private String comment;
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
