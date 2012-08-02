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
* along with Oculus Frontend.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package net.mindengine.oculus.frontend.web.controllers.api.trm;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.mindengine.oculus.frontend.api.GET;
import net.mindengine.oculus.frontend.api.POST;
import net.mindengine.oculus.frontend.api.Path;
import net.mindengine.oculus.frontend.api.RequestBody;
import net.mindengine.oculus.frontend.api.RequestVar;
import net.mindengine.oculus.frontend.domain.api.trm.RunTaskRequest;
import net.mindengine.oculus.frontend.domain.api.trmd.RunTaskResponse;
import net.mindengine.oculus.frontend.domain.trm.TrmTask;
import net.mindengine.oculus.frontend.service.exceptions.UnexistentResource;
import net.mindengine.oculus.frontend.service.project.ProjectDAO;
import net.mindengine.oculus.frontend.service.test.TestDAO;
import net.mindengine.oculus.frontend.service.trm.TrmDAO;
import net.mindengine.oculus.frontend.web.controllers.api.ApiController;
import net.mindengine.oculus.frontend.web.controllers.trm.tasks.RunTaskController;
import net.mindengine.oculus.grid.domain.task.DefaultTask;
import net.mindengine.oculus.grid.domain.task.SuiteTask;
import net.mindengine.oculus.grid.domain.task.TaskStatus;
import net.mindengine.oculus.grid.domain.task.TaskUser;

import org.springframework.stereotype.Controller;

@Controller
public class ApiGridRunTaskController extends ApiController {
    
    private TrmDAO trmDAO;
    private TestDAO testDAO;
    private ProjectDAO projectDAO;
    
    @POST @Path("/grid/run-([0-9].*)/stop") 
    public String stopTask(@RequestVar(1) Long gridTaskId) throws Exception{
        getConfig().lookupGridServer().stopTask(gridTaskId);
        return "stopped";
    }
    
    @GET @Path("/grid/run-([0-9].*)/status")
    public TaskStatus getGridTaskStatus(@RequestVar(1) Long gridTaskId) throws Exception {
        return getConfig().lookupGridServer().getTaskStatus(gridTaskId);
    }
    
    @POST @Path("/grid/run/task-([0-9].*)") 
    public RunTaskResponse runTask(@RequestVar(1) Long taskId, @RequestBody RunTaskRequest request) throws Exception {
        TrmTask trmTask = trmDAO.getTask(taskId);

        if (trmTask == null)
            throw new UnexistentResource("The task doesn't exist");

        Collection<TrmTask> taskDependencies = trmDAO.getDependentTasks(taskId);
        trmTask.setParameters(trmDAO.getTaskProperties(trmTask.getProjectId(), taskId, TrmDAO.PROPERTY_TYPE_SUITE_PARAMETER));
        
        List<Long> includedTaskIds = new LinkedList<Long>();
        if ( taskDependencies != null ) {
            for ( TrmTask trmSubTask : taskDependencies) {
                includedTaskIds.add(runTaskOnGrid(trmSubTask, request));
            }
        }
        
        RunTaskResponse response = new RunTaskResponse();
        response.setGridIncludedTaskIds(includedTaskIds);
        response.setGridTaskId(runTaskOnGrid(trmTask, request));
        return response;
    }

    private Long runTaskOnGrid(TrmTask trmTask, RunTaskRequest request) throws Exception {
        DefaultTask gridTask = RunTaskController.createGridTask(trmTask, trmDAO, testDAO, projectDAO, new TaskUser(1L, "admin"));
        
        for ( SuiteTask suiteTask : gridTask.getSuiteTasks()) {
            suiteTask.getSuite().setParameters(request.getParameters());
            suiteTask.setProjectVersion(request.getBuild());
            suiteTask.setAgentNames(request.getAgents());
        }
        
        return getConfig().lookupGridServer().runTask(gridTask);
    }

    public TrmDAO getTrmDAO() {
        return trmDAO;
    }

    public void setTrmDAO(TrmDAO trmDAO) {
        this.trmDAO = trmDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public TestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(TestDAO testDAO) {
        this.testDAO = testDAO;
    }

}
