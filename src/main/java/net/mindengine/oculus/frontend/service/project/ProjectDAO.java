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
