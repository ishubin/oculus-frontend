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
package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.domain.customization.Customization;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customization.CustomizationUtils;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

public class CreateCustomStatisticController extends SecureSimpleFormController {

	private CustomStatisticDAO customStatisticDAO;
	private ProjectDAO projectDAO;
	

	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		verifyPermissions(request);
		Map map = new HashMap();
		
		Project project = projectDAO.getProject(Long.parseLong(request.getParameter("projectId")));
		if (project.getParentId() != 0L) {
			throw new IllegalArgumentException("The project you specified is not root");
		}
		map.put("project", project);
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
	    CustomStatistic customStatistic = new CustomStatistic();
	    customStatistic.setProjectId(Long.parseLong(request.getParameter("projectId")));
	    return customStatistic;
	}
	

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		verifyPermissions(request);
		CustomStatistic customStatistic = (CustomStatistic)command;
		
		User user = getUser(request);
		customStatistic.setUserId(user.getId());
		customStatistic.setCreatedDate(new Date());
		
		Long id = customStatisticDAO.createCustomStatistic(customStatistic);
		
		return new ModelAndView(new RedirectView("../cstat/display?id=" + id));
	}

	public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
		this.customStatisticDAO = customStatisticDAO;
	}

	public CustomStatisticDAO getCustomStatisticDAO() {
		return customStatisticDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

}
