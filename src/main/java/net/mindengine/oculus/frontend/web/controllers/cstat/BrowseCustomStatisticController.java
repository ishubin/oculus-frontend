package net.mindengine.oculus.frontend.web.controllers.cstat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.domain.project.Project;
import net.mindengine.oculus.frontend.service.customstatistics.CustomStatisticDAO;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

public class BrowseCustomStatisticController extends SecureSimpleViewController{
    
    private ProjectDAO projectDAO;
    private CustomStatisticDAO customStatisticDAO;
    
    
    @Override
    public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        
        Long projectId = Long.parseLong(request.getParameter("projectId"));
        Project project = projectDAO.getProject(projectId);
        if(project==null) throw new UnexistentResource(Project.class, projectId);
        map.put("project", project);
        map.put("customStatistics", customStatisticDAO.getStatisticsByProject(projectId));
        return map;
    }
    
    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
    public CustomStatisticDAO getCustomStatisticDAO() {
        return customStatisticDAO;
    }
    public void setCustomStatisticDAO(CustomStatisticDAO customStatisticDAO) {
        this.customStatisticDAO = customStatisticDAO;
    }

}
