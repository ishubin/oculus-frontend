package net.mindengine.oculus.frontend.web.controllers.cstat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class SaveChartSettingsController extends SecureSimpleViewController {
    
    private CustomStatisticDAO customStatisticDAO;
    
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        
        Long id = Long.parseLong(request.getParameter("id"));
        String parameters = request.getParameter("parameters");
        
        customStatisticDAO.changeCustomStatisticChartSettings(id, parameters);
        
        return new ModelAndView(new RedirectView("../cstat/display-chart?id="+id));
    }

    public CustomStatisticDAO getCustomStatisticDAO() {
        return customStatisticDAO;
    }

    public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
        this.customStatisticDAO = customStatisticDAO;
    }

}
