package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleFormController;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditCustomStatisticChartController extends SecureSimpleFormController{
    
    private CustomStatisticDAO customStatisticDAO;
    private ProjectDAO projectDAO;
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        
        CustomStatisticChart chart = customStatisticDAO.getCustomStatisticChart(id);
        if(chart==null) throw new UnexistentResource("Chart with id = "+id +" doesn't exist");
        return chart;
    }
    
    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        CustomStatisticChart chart = (CustomStatisticChart)command;
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(chart.getCustomStatisticId());
        map.put("customStatistic", customStatistic);
        if(customStatistic!=null){
            map.put("project", projectDAO.getProject(customStatistic.getProjectId()));
        }
        
        return map;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CustomStatisticChart chart = (CustomStatisticChart)command;
        
        customStatisticDAO.changeCustomStatisticChart(chart);
        
        return new ModelAndView(new RedirectView("../cstat/edit-chart?id="+chart.getId()));
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
