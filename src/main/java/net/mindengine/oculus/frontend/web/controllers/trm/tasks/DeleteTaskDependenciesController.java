package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class DeleteTaskDependenciesController extends SecureSimpleViewController {

    private TrmDAO trmDAO;
    
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        
        Long taskId = Long.parseLong(request.getParameter("taskId"));
        
        User user = getUser(request);
        
        TrmTask task = trmDAO.getTask(taskId);
        if(task==null) throw new UnexistentResource("Task with id "+taskId+" doesn't exist");
        if(!task.getUserId().equals(user.getId())){
            throw new PermissionDeniedException();
        }
        
        String depIds[] = request.getParameter("dependencies").split(",");
        for(String depId : depIds) {
            trmDAO.deleteTaskDependency(Long.parseLong(depId), taskId);
        }
        
        return new ModelAndView(new RedirectView("../test-run-manager/edit-task?id="+taskId));
    }

    public TrmDAO getTrmDAO() {
        return trmDAO;
    }

    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }
}
