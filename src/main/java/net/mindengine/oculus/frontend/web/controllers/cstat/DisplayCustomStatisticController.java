package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.customstatistics.CustomStatistic;
import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.user.UserDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class DisplayCustomStatisticController extends SecureSimpleViewController{
	
	private CustomStatisticDAO customStatisticDAO;
	private ProjectDAO		   projectDAO;
	private UserDAO			   userDAO;
	
	@Override
	public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
		Long id = Long.parseLong(request.getParameter("id"));
		CustomStatistic customStatistic = customStatisticDAO.getCustomStatistic(id);
		if(customStatistic==null) throw new UnexistentResource("Custom statistic with id = "+id+" doesn't exist");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customStatistic", customStatistic);
		map.put("author", userDAO.getUserById(customStatistic.getUserId()));
		map.put("customStatisticParameters", customStatisticDAO.getParametersByStatistic(id));
		Project project = projectDAO.getProject(customStatistic.getProjectId());
		map.put("project", project);
		map.put("title", getTitle() + customStatistic.getName());
		map.put("parameters", customStatisticDAO.getParametersByStatistic(id));
        map.put("charts", customStatisticDAO.getCustomStatisticCharts(id));
        
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
	public void setUserDAO(UserDAO userDAO) {
	    this.userDAO = userDAO;
    }
	public UserDAO getUserDAO() {
	    return userDAO;
    }
}
