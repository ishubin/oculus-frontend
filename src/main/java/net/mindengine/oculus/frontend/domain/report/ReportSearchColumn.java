package net.mindengine.oculus.frontend.domain.report;

import java.io.Serializable;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class ReportSearchColumn extends SearchColumn implements Serializable {
	private static final long serialVersionUID = -3388729881602415833L;
	public final static Integer TEST_NAME = 1;
	public final static Integer PROJECT = 2;
	public final static Integer STATUS = 3;
	public final static Integer DESIGNER = 4;
	public final static Integer RUNNER = 5;
	public final static Integer START_TIME = 6;
	public final static Integer SUITE_NAME = 7;
	public final static Integer REASONS = 8;
	public final static Integer AGENT = 9;
	public final static Integer ISSUE = 10;

	public Integer getTEST_NAME() {
		return TEST_NAME;
	}

	public Integer getPROJECT() {
		return PROJECT;
	}

	public Integer getSTATUS() {
		return STATUS;
	}

	public Integer getDESIGNER() {
		return DESIGNER;
	}

	public Integer getRUNNER() {
		return RUNNER;
	}

	public Integer getSTART_TIME() {
		return START_TIME;
	}

	public Integer getSUITE_NAME() {
		return SUITE_NAME;
	}

	public Integer getREASONS() {
		return REASONS;
	}

	public Integer getAGENT() {
		return AGENT;
	}

	public Integer getISSUE() {
		return ISSUE;
	}

}
