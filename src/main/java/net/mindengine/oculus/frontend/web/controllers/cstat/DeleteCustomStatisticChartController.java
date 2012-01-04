package net.mindengine.oculus.frontend.web.controllers.cstat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DeleteCustomStatisticChartController extends SecureSimpleViewController{
    
    private CustomStatisticDAO customStatisticDAO;
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        
        Long id = Long.parseLong(request.getParameter("id"));
        CustomStatisticChart chart = customStatisticDAO.getCustomStatisticChart(id);
        if(chart==null) throw new UnexistentResource("Chart with id = "+id+" wasn't found");
        
        customStatisticDAO.deleteCustomStatisticChart(id);
        
        return new ModelAndView(new RedirectView("../cstat/display?id="+chart.getCustomStatisticId()));
    }

    public CustomStatisticDAO getCustomStatisticDAO() {
        return customStatisticDAO;
    }

    public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
        this.customStatisticDAO = customStatisticDAO;
    }
}
