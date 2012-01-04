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
