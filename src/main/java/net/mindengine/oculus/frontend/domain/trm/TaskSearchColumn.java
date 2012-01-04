package net.mindengine.oculus.frontend.domain.trm;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class TaskSearchColumn extends SearchColumn {
	public final static Integer TASK_NAME = 1;
	public final static Integer TASK_DATE = 2;
	public final static Integer USER_NAME = 3;

	public Integer getTASK_NAME() {
		return TASK_NAME;
	}

	public Integer getTASK_DATE() {
		return TASK_DATE;
	}

	public Integer getUSER_NAME() {
		return USER_NAME;
	}
}
