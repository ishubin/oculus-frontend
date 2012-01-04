package net.mindengine.oculus.frontend.web.controllers.project.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.InvalidRequest;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class BuildsBrowseController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private BuildDAO buildDAO;

	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		String path = request.getPathInfo().substring(8);
		Map<String, Object> map = new HashMap<String, Object>();

		Project project = projectDAO.getProjectByPath(path);
		if (project == null)
			throw new UnexistentResource("The project \"" + path + "\" wasn't found");

		if (project.getParentId() > 0) {
			throw new InvalidRequest("The subprojects cannot have separate builds");
		}

		boolean bManageBuild = false;
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();
		if (user != null && user.hasPermission(7)) {
			bManageBuild = true;
		}

		List<Build> builds = buildDAO.getBuilds(project.getId());

		map.put("project", project);
		map.put("builds", builds);
		map.put("manageBuildAllowed", bManageBuild);
		map.put("title", getTitle());
		return map;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

}
