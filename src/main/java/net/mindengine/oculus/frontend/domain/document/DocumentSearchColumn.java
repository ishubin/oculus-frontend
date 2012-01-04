package net.mindengine.oculus.frontend.domain.document;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class DocumentSearchColumn extends SearchColumn {
	public final static Integer TESTCASE_NAME = 1;
	public final static Integer PROJECT = 2;
	public final static Integer SUBPROJECT = 3;
	public final static Integer TESTCASE_DATE = 4;
	public final static Integer USER_NAME = 5;

	public Integer getTESTCASE_NAME() {
		return TESTCASE_NAME;
	}

	public Integer getPROJECT() {
		return PROJECT;
	}

	public Integer getSUBPROJECT() {
		return SUBPROJECT;
	}

	public Integer getTESTCASE_DATE() {
		return TESTCASE_DATE;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}
}
