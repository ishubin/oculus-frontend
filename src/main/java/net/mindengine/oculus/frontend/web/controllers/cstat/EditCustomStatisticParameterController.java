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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditCustomStatisticParameterController extends SecureSimpleFormController{
    
    private CustomStatisticDAO customStatisticDAO;
    private ProjectDAO projectDAO;
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        
        CustomStatisticParameter parameter = customStatisticDAO.getCustomStatisticParameter(id);
        return parameter;
    }
    
    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        CustomStatisticParameter parameter = (CustomStatisticParameter)command;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(parameter.getCustomStatisticId());
        map.put("customStatistic", customStatistic);
        if(customStatistic!=null){
            map.put("project", projectDAO.getProject(customStatistic.getProjectId()));
        }
        
        return map;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CustomStatisticParameter parameter = (CustomStatisticParameter)command;
        
        customStatisticDAO.changeCustomStatisticParameter(parameter);
        
        return new ModelAndView(new RedirectView("../cstat/edit-parameter?id="+parameter.getId()));
    }

    public CustomStatisticDAO getCustomStatisticDAO() {
        return customStatisticDAO;
    }

    public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
        this.customStatisticDAO = customStatisticDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
    

}
