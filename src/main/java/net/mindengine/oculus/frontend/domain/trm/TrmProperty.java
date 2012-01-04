package net.mindengine.oculus.frontend.domain.trm;

import java.util.LinkedList;
import java.util.List;

/**
 * Used for test run manager properties specification. Also used for customizing
 * the suite parameters which are displayed in Test Run Manager
 * 
 * @author Ivan Shubin
 * 
 */
public class TrmProperty {
	public final static String _TYPE_SUITE_PARAMETER = "suite_parameter".intern();

	private Long id;
	private String name;
	private String description;
	private String value;
	private String type;
	private String subtype;
	private Long projectId;

	public class Controls {
		public final static String _TEXT = "text";
		public final static String _LIST = "list";
		public final static String _CHECKBOX = "checkbox";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getSubtype() {
		return subtype;
	}

	public List<String> getValuesAsList() {
		List<String> values = new LinkedList<String>();
		if (value != null) {
			String[] str = value.split("<value>");
			for (String s : str) {
				if (s != null && !s.isEmpty()) {
					values.add(s);
				}
			}
		}
		return values;
	}

	public int getValuesCount() {
		if (value != null) {
			int amount = value.split("<value>").length;
			if (amount > 0)
				return amount - 1;
		}
		return 0;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}
}
