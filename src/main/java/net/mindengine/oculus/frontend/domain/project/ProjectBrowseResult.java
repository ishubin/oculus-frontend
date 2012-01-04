package net.mindengine.oculus.frontend.domain.project;

import java.util.List;

public class ProjectBrowseResult {
	private List<Project> projects;
	private Long numberOfResults;

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Long getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(Long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

}
