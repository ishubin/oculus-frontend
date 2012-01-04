package net.mindengine.oculus.frontend.domain.test;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class TestSearchColumn extends SearchColumn {
	public final static Integer TEST_NAME = 1;
	public final static Integer PROJECT = 2;
	public final static Integer SUBPROJECT = 3;
	public final static Integer TEST_DATE = 4;
	public final static Integer USER_NAME = 5;

	public Integer getTEST_NAME() {
		return TEST_NAME;
	}

	public Integer getPROJECT() {
		return PROJECT;
	}

	public Integer getSUBPROJECT() {
		return SUBPROJECT;
	}

	public Integer getTEST_DATE() {
		return TEST_DATE;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}

}
