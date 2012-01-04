package net.mindengine.oculus.frontend.web.controllers.dashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.dashboard.Dashboard;
import net.mindengine.oculus.frontend.service.dashboard.ProjectStatisticsDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditDashboardController extends SecureSimpleFormController {
	private ProjectDAO projectDAO;
	private ProjectStatisticsDAO projectStatisticsDAO;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		Dashboard dashboard = projectStatisticsDAO.getDashboard(projectId);
		dashboard.setProjectId(projectId);

		if (dashboard == null)
			throw new UnexistentResource("Dashboard for project " + projectId + " doesn't exist");
		return dashboard;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Dashboard dashboard = (Dashboard) command;
		Long projectId = Long.parseLong(request.getParameter("projectId"));
		dashboard.setProjectId(projectId);
		projectStatisticsDAO.changeDashboard(dashboard);
		return new ModelAndView(new RedirectView("../dashboard/edit?projectId=" + projectId));
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectStatisticsDAO getProjectStatisticsDAO() {
		return projectStatisticsDAO;
	}

	public void setProjectStatisticsDAO(ProjectStatisticsDAO projectStatisticsDAO) {
		this.projectStatisticsDAO = projectStatisticsDAO;
	}
}
