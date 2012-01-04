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
