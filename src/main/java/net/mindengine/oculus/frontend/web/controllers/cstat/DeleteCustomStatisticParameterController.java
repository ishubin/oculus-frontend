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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DeleteCustomStatisticParameterController extends SecureSimpleViewController{

    private CustomStatisticDAO  customStatisticDAO;
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        Long id = Long.parseLong(request.getParameter("id"));
        
        CustomStatisticParameter parameter = customStatisticDAO.getCustomStatisticParameter(id);
        if(parameter == null) throw new UnexistentResource("Parameter with id = " + id + " doesn't exist");
        
        customStatisticDAO.deleteCustomStatisticParameter(id);
        
        return new ModelAndView(new RedirectView("../cstat/display?id="+parameter.getCustomStatisticId()));
    }

    public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
        this.customStatisticDAO = customStatisticDAO;
    }

    public CustomStatisticDAO getCustomStatisticDAO() {
        return customStatisticDAO;
    }
}
