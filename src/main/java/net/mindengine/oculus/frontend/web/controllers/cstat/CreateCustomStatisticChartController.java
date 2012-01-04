package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class CreateCustomStatisticChartController extends SecureSimpleFormController {

    private CustomStatisticDAO customStatisticDAO;
    private ProjectDAO projectDAO;
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CustomStatisticChart chart = new CustomStatisticChart();
        chart.setCustomStatisticId(Long.parseLong(request.getParameter("customStatisticId")));
        return chart;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        verifyPermissions(request);
        
        Long customStatisticId = Long.parseLong(request.getParameter("customStatisticId"));
        
        CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(customStatisticId);
        if(customStatistic==null)throw new UnexistentResource("Custom statistic with id "+customStatisticId+" doesn't exist");
        
        Project project = projectDAO.getProject(customStatistic.getProjectId());
        
        Map map = new HashMap<String, Object>();
        map.put("customStatistic", customStatistic);
        map.put("project", project);
        return map;
    }
    
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        verifyPermissions(request);
        CustomStatisticChart chart = (CustomStatisticChart)command;
        chart.setDate(new Date());
        Long chartId = customStatisticDAO.createCustomStatisticChart(chart);
        
        return new ModelAndView(new RedirectView("../cstat/display-chart?id="+chartId));
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
