package net.mindengine.oculus.frontend.domain;

public class AjaxError {
	private String type;
	private String text;
	private String details;

	public AjaxError() {

	}

	public AjaxError(Exception exception) {
		setType(exception.getClass().getName());
		setText(exception.getLocalizedMessage());
		setText(exception.getMessage());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
