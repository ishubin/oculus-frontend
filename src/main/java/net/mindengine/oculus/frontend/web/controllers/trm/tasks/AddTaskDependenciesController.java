/*******************************************************************************
* 2012 Ivan Shubin http://mindengine.net
* 
* This file is part of MindEngine.net Oculus Frontend.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oculus Experior.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.domain.trm.TrmTaskDependency;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.service.exceptions.PermissionDeniedException;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddTaskDependenciesController extends SecureSimpleViewController {

    private TrmDAO trmDAO;
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyPermissions(request);
        Long taskId = Long.parseLong(request.getParameter("taskId"));
        
        TrmTask task = trmDAO.getTask(taskId);
        if(task==null) throw new UnexistentResource("Task with id = "+taskId+" doesn't exist");
        
        User user = getUser(request);
        if(!user.getId().equals(task.getUserId())){
            throw new PermissionDeniedException("You are not allowed to change tasks of other users");
        }
        
        /*
         * Fetching task dependencies in order to check if there are the same dependencies as in request.
         * We should avoid dependency coupling.
         */
        Collection<TrmTaskDependency> dependencies = trmDAO.getTaskDependencies(taskId);
        
        String refTaskIds[] = request.getParameter("refTaskIds").split(",");
        
        for(String strRefTaskId : refTaskIds) {
            /*
             * As each task is sent in request starting with 't' letter we need to remove it from here 
             */
            Long refTaskId = Long.parseLong(strRefTaskId.substring(1));
            if(!refTaskId.equals(taskId)){
                if(!contains(dependencies, refTaskId)) {
                    trmDAO.createTaskDependency(taskId, refTaskId);
                }
            }
        }
        
        return new ModelAndView(new RedirectView("../grid/edit-task?id="+taskId));
    }
    
    public boolean contains(Collection<TrmTaskDependency> dependencies, Long refTaskId){
        for(TrmTaskDependency dependency : dependencies){
            if(dependency.getRefTaskId().equals(refTaskId)) return true;
        }
        return false;
    }


    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }


    public TrmDAO getTrmDAO() {
        return trmDAO;
    }
    
    
}
