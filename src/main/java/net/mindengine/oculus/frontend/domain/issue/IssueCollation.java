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

import java.util.Collection;

public class IssueCollation {
	private Long id;
	private Collection<IssueCollationTest> tests;
	private Long issueId;
	private Collection<IssueCollationCondition> conditions;
	private String reasonPattern;

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public void setConditions(Collection<IssueCollationCondition> conditions) {
		this.conditions = conditions;
	}

	public Collection<IssueCollationCondition> getConditions() {
		return conditions;
	}

	public void setTests(Collection<IssueCollationTest> tests) {
		this.tests = tests;
	}

	public Collection<IssueCollationTest> getTests() {
		return tests;
	}

	public void setReasonPattern(String reasonPattern) {
		this.reasonPattern = reasonPattern;
	}

	public String getReasonPattern() {
		return reasonPattern;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
