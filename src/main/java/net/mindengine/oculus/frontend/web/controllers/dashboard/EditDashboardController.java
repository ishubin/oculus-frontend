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
		
		if(dashboard.getRunnerId()==null ) {
		    dashboard.setRunnerId(0L);
		}
		
		if(dashboard.getDaysPeriod()==null || dashboard.getDaysPeriod()<1){
		    errors.reject(null, "You have to specify days period higher than 0");
		}
		else {
		    projectStatisticsDAO.changeDashboard(dashboard);
		    return new ModelAndView(new RedirectView("../dashboard/edit?projectId=" + projectId));
		}
		return new ModelAndView(getFormView(), errors.getModel());
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
