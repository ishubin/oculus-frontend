/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
