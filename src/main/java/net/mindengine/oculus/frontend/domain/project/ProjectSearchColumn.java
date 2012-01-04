package net.mindengine.oculus.frontend.domain.project;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class ProjectSearchColumn extends SearchColumn {
	public final static Integer PROJECT_NAME = 1;
	public final static Integer PARENT_PROJECT = 2;
	public final static Integer PROJECT_DATE = 3;
	public final static Integer USER_NAME = 4;

	public Integer getPROJECT_NAME() {
		return PROJECT_NAME;
	}

	public Integer getPARENT_PROJECT() {
		return PARENT_PROJECT;
	}

	public Integer getPROJECT_DATE() {
		return PROJECT_DATE;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}
}
