/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
