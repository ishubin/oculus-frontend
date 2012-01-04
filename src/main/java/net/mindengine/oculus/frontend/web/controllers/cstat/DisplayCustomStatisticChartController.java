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
