package net.mindengine.oculus.frontend.domain;

public class AjaxModel {
	private String result;
	private Object object;

	public AjaxModel() {
	}

	public AjaxModel(String result, Object object) {
		this.result = result;
		this.object = object;
	}

	public AjaxModel(Exception exception) {
		result = "error";
		setObject(new AjaxError(exception));
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
