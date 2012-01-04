package net.mindengine.oculus.frontend.service.project;

import java.util.List;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter;
import net.mindengine.oculus.frontend.domain.project.ProjectBrowseResult;
import net.mindengine.oculus.frontend.domain.project.ProjectSearchFilter;

public interface ProjectDAO {
	public Long createProject(Project project) throws Exception;

	public Project getProjectByPath(String path) throws Exception;

	public Project getProject(Long id) throws Exception;

	public void updateProject(Long id, Project project) throws Exception;

	public void deleteProject(Long id) throws Exception;

	public long getProjectRootId(Long id, Integer maxIterations) throws Exception;

	public List<Project> getSubprojects(Long parentId) throws Exception;

	public List<Project> getRootProjects() throws Exception;

	public BrowseResult<Project> getSubprojects(Long parentId, int page, int limit) throws Exception;

	public ProjectBrowseResult browseProjects(ProjectBrowseFilter filter) throws Exception;

	public BrowseResult<Project> searchSubprojects(ProjectSearchFilter filter) throws Exception;
}
