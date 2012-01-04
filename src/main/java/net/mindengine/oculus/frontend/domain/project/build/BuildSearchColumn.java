package net.mindengine.oculus.frontend.domain.project.build;

import net.mindengine.oculus.frontend.db.search.SearchColumn;

public class BuildSearchColumn extends SearchColumn {
	public final static Integer BUILD_NAME = 1;
	public final static Integer PROJECT = 2;
	public final static Integer BUILD_DATE = 3;

	public Integer getBUILD_NAME() {
		return BUILD_NAME;
	}

	public Integer getPROJECT() {
		return PROJECT;
	}

	public Integer getBUILD_DATE() {
		return BUILD_DATE;
	}

}
