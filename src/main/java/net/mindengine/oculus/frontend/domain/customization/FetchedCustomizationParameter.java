package net.mindengine.oculus.frontend.domain.customization;

import java.util.LinkedList;
import java.util.List;

public class FetchedCustomizationParameter extends Customization {
	private String value;

	/**
	 * Returns the value ids that are placed in a brackets in the value e.g.
	 * (45)(564)(3)(4)
	 * 
	 * @return the value ids that are placed in a brackets in the value
	 */
	public List<Long> fetchPossibleValueIds() {
		List<Long> list = new LinkedList<Long>();
		if (value != null) {
			String[] ids = value.split("(\\)\\(|\\(|\\))");
			for (String id : ids) {
				if (!id.isEmpty()) {
					try {
						list.add(Long.parseLong(id));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
