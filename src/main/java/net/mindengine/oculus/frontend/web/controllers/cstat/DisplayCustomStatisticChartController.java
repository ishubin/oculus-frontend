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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DisplayCustomStatisticChartController extends SecureSimpleViewController {

    private CustomStatisticDAO customStatisticDAO;
    private ProjectDAO projectDAO;

    @Override
    public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        Long id = Long.parseLong(request.getParameter("id"));

        CustomStatisticChart chart = customStatisticDAO.getCustomStatisticChart(id);
        if (chart == null) {
            throw new UnexistentResource("Chart", id);
        }

        CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(chart.getCustomStatisticId());
        if (customStatistic == null) {
            throw new UnexistentResource("Custom statistic", id);
        }
        
        /*
         * Fetching all parameters for the specified statistic. The will be shown as bread-crumb navigation for fetching proper data into chart
         */
        Collection<CustomStatisticParameter> parameters = customStatisticDAO.getParametersByStatistic(customStatistic.getId());
        for(CustomStatisticParameter parameter : parameters){
            parameter.setPossibleValues(customStatisticDAO.getParameterValues(parameter.getId()));
        }
        
        map.put("parameters", parameters);
        map.put("customStatistic", customStatistic);
        map.put("chart", chart);
        map.put("project", projectDAO.getProject(customStatistic.getProjectId()));

        return map;
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
