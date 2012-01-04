package net.mindengine.oculus.frontend.service.project.build;

import java.util.List;

import net.mindengine.oculus.frontend.domain.db.BrowseResult;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.domain.project.build.BuildSearchFilter;

public interface BuildDAO {
	/**
	 * Returns project builds which match the specified criteria
	 * 
	 * @param patternName
	 *            The name of the build with
	 * @param page
	 * @param projectId
	 *            Id of the related project
	 * @return
	 */
	public BrowseResult<Build> fetchBuilds(String patternName, int page, long projectId) throws Exception;

	public List<Build> getBuilds(long projectId) throws Exception;

	public Build getBuild(long buildId) throws Exception;

	public void deleteBuild(long buildId) throws Exception;

	public long createBuild(Build build) throws Exception;

	public void updateBuild(Build build) throws Exception;

	public Build getBuildByNameAndProject(String name, long projectId) throws Exception;

	public BrowseResult<Build> searchBuilds(BuildSearchFilter filter) throws Exception;

}
