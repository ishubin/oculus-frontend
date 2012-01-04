package net.mindengine.oculus.frontend.domain.issue;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class IssueSearchColumn extends SearchColumn {
	public final static Integer ISSUE_NAME = 1;
	public final static Integer ISSUE_SUMMARY = 2;
	public final static Integer ISSUE_DATE = 3;
	public final static Integer DEPENDENT_TESTS = 4;
	public final static Integer USER_NAME = 5;
	public final static Integer PROJECT = 6;
	public final static Integer SUBPROJECT = 7;

	public Integer getISSUE_NAME() {
		return ISSUE_NAME;
	}

	public Integer getISSUE_SUMMARY() {
		return ISSUE_SUMMARY;
	}

	public Integer getISSUE_DATE() {
		return ISSUE_DATE;
	}

	public Integer getDEPENDENT_TESTS() {
		return DEPENDENT_TESTS;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}

	public Integer getPROJECT() {
		return PROJECT;
	}

	public Integer getSUBPROJECT() {
		return SUBPROJECT;
	}
}
