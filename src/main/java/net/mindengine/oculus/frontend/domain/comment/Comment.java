package net.mindengine.oculus.frontend.domain.comment;

import java.util.Date;

public class Comment {
	public static final String UNIT_PROJECT = "project".intern();
	public static final String UNIT_TEST = "test".intern();
	public static final String UNIT_ISSUE = "issue".intern();
	public static final String UNIT_DOCUMENT = "document".intern();
	public static final String UNIT_BUILD = "build".intern();

	private Long id;
	private String text;
	private Long userId;
	private Date date;
	private Long unitId;
	private String unit;
	private String userName;
	private String userLogin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
