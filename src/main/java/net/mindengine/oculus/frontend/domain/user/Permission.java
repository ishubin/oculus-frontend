package net.mindengine.oculus.frontend.domain.user;

public class Permission {

	private int code;

	/**
	 * This value should be unique for all permissions in project
	 */
	private String name;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Permission() {

	}

	public Permission(int code) {
		this.code = code;

	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
