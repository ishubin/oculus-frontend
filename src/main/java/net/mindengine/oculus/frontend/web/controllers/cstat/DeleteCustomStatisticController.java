package net.mindengine.oculus.frontend.web.controllers.cstat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DeleteCustomStatisticController extends SecureSimpleViewController{
	private CustomStatisticDAO customStatisticDAO;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    verifyPermissions(request);
	    
	    Long id = Long.parseLong(request.getParameter("id"));
	    CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(id);
	    if(customStatistic==null)throw new UnexistentResource("Custom statistics with id "+id+" doesn't exist");
	    
	    customStatisticDAO.deleteCustomStatistic(id);
	    return new ModelAndView(new RedirectView("../cstat/browse?projectId="+customStatistic.getProjectId()));
	}

	public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
	    this.customStatisticDAO = customStatisticDAO;
    }

	public CustomStatisticDAO getCustomStatisticDAO() {
	    return customStatisticDAO;
    }
}