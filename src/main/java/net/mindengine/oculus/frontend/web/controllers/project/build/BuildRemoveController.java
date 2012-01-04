package net.mindengine.oculus.frontend.web.controllers.project.build;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.project.build.Build;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.NotAuthorizedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.web.Session;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Removes the specified build and redirects to builds page
 * 
 * @author Ivan Shubin
 * 
 */
public class BuildRemoveController extends SecureSimpleViewController {
	private ProjectDAO projectDAO;
	private BuildDAO buildDAO;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = Session.create(request);
		User user = session.getAuthorizedUser();

		if (user == null || !user.hasPermission(7))
			throw new NotAuthorizedException();

		Long buildId = Long.parseLong(request.getParameter("buildId"));
		Build build = buildDAO.getBuild(buildId);
		if (build == null)
			throw new UnexistentResource("The build with id=" + buildId + " doesn't exist");

		buildDAO.deleteBuild(buildId);

		Project project = projectDAO.getProject(build.getProjectId());
		return new ModelAndView(new RedirectView("../project/builds-" + project.getPath()));
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
