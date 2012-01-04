package net.mindengine.oculus.frontend.web.controllers.project.build;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.service.project.build.BuildDAO;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;

public class BuildAjaxFetchController extends SimpleAjaxController {
	private BuildDAO buildDAO;

	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Integer page = Integer.parseInt(request.getParameter("page"));
		String name = request.getParameter("name");
		AjaxModel model = new AjaxModel();

		model.setObject(buildDAO.fetchBuilds(name, page, projectId));
		return model;
	}

	public void setBuildDAO(BuildDAO buildDAO) {
		this.buildDAO = buildDAO;
	}

	public BuildDAO getBuildDAO() {
		return buildDAO;
	}
}
