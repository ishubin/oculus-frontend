package net.mindengine.oculus.frontend.db.search;

public class SearchColumn {
	private Integer id;
	private String name;
	private String fieldName;
	private String sqlColumn;
	private Boolean show = true;
	private Boolean isSet = false;

	public Boolean getIsSet() {
		return isSet;
	}

	public void setIsSet(Boolean isSet) {
		this.isSet = isSet;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public SearchColumn(String name, String fieldName) {
		super();
		this.name = name;
		this.fieldName = fieldName;
	}

	public SearchColumn() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSqlColumn() {
		return sqlColumn;
	}

	public void setSqlColumn(String sqlColumn) {
		this.sqlColumn = sqlColumn;
	}

}
