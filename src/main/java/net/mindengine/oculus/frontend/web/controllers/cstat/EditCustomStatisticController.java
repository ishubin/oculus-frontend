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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

public class EditCustomStatisticController extends SecureSimpleFormController{

	private ProjectDAO projectDAO;
	private CustomStatisticDAO customStatisticDAO;
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
	    Long id = Long.parseLong(request.getParameter("id"));
	    CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(id);
	    if(customStatistic==null) throw new UnexistentResource("Custom statistic with id = "+id+" doesn't exist");
	    return customStatistic;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
	    CustomStatistic customStatistic = (CustomStatistic)command;
	    Map<String, Object> map  = new HashMap<String, Object>();
	    
	    map.put("project", projectDAO.getProject(customStatistic.getProjectId()));
	    return map;
	}
	
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		verifyPermissions(request);
		CustomStatistic customStatistic = (CustomStatistic)command;
		
		customStatisticDAO.changeCustomStatistic(customStatistic);
		
		return new ModelAndView(new RedirectView("../cstat/edit?id=" + customStatistic.getId()));
	}


	public ProjectDAO getProjectDAO() {
    	return projectDAO;
    }


	public void setProjectDAO(ProjectDAO projectDAO) {
    	this.projectDAO = projectDAO;
    }


	public CustomStatisticDAO getCustomStatisticDAO() {
    	return customStatisticDAO;
    }


	public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
    	this.customStatisticDAO = customStatisticDAO;
    }
}
