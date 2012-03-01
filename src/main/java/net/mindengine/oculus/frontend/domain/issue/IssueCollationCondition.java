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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
