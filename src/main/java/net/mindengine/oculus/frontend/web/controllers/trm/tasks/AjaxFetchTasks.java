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
package net.mindengine.oculus.frontend.web.controllers.trm.tasks;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.AjaxModel;
import net.mindengine.oculus.frontend.domain.trm.SuiteStatisticUtils;
import net.mindengine.oculus.frontend.domain.user.User;
import net.mindengine.oculus.frontend.web.controllers.SimpleAjaxController;
import net.mindengine.oculus.grid.domain.agent.AgentInformation;
import net.mindengine.oculus.grid.domain.task.SuiteInformation;
import net.mindengine.oculus.grid.domain.task.SuiteStatistic;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.domain.task.TaskStatus;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * Fetches all user tasks of the user.
 * 
 * @author Ivan Shubin
 * 
 */
public class AjaxFetchTasks extends SimpleAjaxController {
	private Config config;

	/**
	 * Using this structure for proper conversion to JSON. Is is only used in this controller
	 * @author soulrevax
	 *
	 */
	public class TaskNode{
	    private Long id;
	    private String name;
	    private int type; //0-task, 1-suite
	    private Date created;
	    private Date completed;
	    private String message;
	    private Float progress; //percents from 0.0 to 100.00
	    private int status;//uses the same status numbers as in TaskStatus
	    private String report; //comma separated list of suiteIds
	    private Collection<TaskNode> children;
	    private AgentInformation assignedAgent;
        public AgentInformation getAssignedAgent() {
            return assignedAgent;
        }
        public void setAssignedAgent(AgentInformation assignedAgent) {
            this.assignedAgent = assignedAgent;
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public Date getCreated() {
            return created;
        }
        public void setCreated(Date created) {
            this.created = created;
        }
        public Date getCompleted() {
            return completed;
        }
        public void setCompleted(Date completed) {
            this.completed = completed;
        }
        public Float getProgress() {
            return progress;
        }
        public void setProgress(Float progress) {
            this.progress = progress;
        }
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public String getReport() {
            return report;
        }
        public void setReport(String report) {
            this.report = report;
        }
        public Collection<TaskNode> getChildren() {
            return children;
        }
        public void setChildren(Collection<TaskNode> children) {
            this.children = children;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
	}
	
	public TaskNode convert(TaskInformation task, ClientServerRemoteInterface server){
	    TaskNode node = new TaskNode();
	    node.setName(task.getTaskName());
	    node.setProgress(task.getTaskStatus().getPercent());
	    node.setStatus(task.getTaskStatus().getStatus());
	    node.setId(task.getTaskId());
	    node.setAssignedAgent(task.getTaskStatus().getAssignedAgent());
	    node.setCompleted(task.getCompletedDate());
	    node.setCreated(task.getCreatedDate());
	 
	    if(task.getTaskStatus().getSuiteInformation()==null) {
	        node.setType(0);
	        
	        TaskInformation[] childTasks = server.getTasks(task.getTaskId());
	        if(childTasks!=null){
	            node.setChildren(new LinkedList<TaskNode>());
	            
	            boolean bcomma = false;
	            String report = "";
	            for(TaskInformation childTask : childTasks){
	                node.getChildren().add(convert(childTask, server));
	                SuiteInformation suiteInformation = childTask.getTaskStatus().getSuiteInformation();
	                if(suiteInformation!=null && suiteInformation.getSuiteId()!=null) {
	                    if ( bcomma ) {
	                        report += ",";
	                    }
	                    report += childTask.getTaskStatus().getSuiteInformation().getSuiteId();
	                    bcomma = true;
	                }
	            }
	            
	            SuiteStatistic statisticFromTasks = SuiteStatisticUtils.collectStatisticFromTasks(childTasks);
	            if ( statisticFromTasks != null ) {
	                node.setMessage(SuiteStatisticUtils.getPrettyStatistics(statisticFromTasks));
	            }
	            
	            node.setReport(report);
	        }
	    }
	    else {
            node.setType(1);
            
            StringBuffer message = new StringBuffer();
            AgentInformation assignedAgent = task.getTaskStatus().getAssignedAgent();
            if ( assignedAgent != null ) {
                if ( TaskStatus.ACTIVE.equals(task.getTaskStatus().getStatus())) {
                    message.append("Assigned to " + assignedAgent.getName() );
                }
                else if ( TaskStatus.COMPLETED.equals(task.getTaskStatus().getStatus())) {
                    message.append( "Completed on " + assignedAgent.getName());
                }
            }
            
            SuiteInformation suiteInformation = task.getTaskStatus().getSuiteInformation();
            
            if ( suiteInformation != null ) {
                Long suiteId = task.getTaskStatus().getSuiteInformation().getSuiteId();
                
                if ( suiteId!=null ) {
                    node.setReport(suiteId.toString());
                }
                
                message.append(SuiteStatisticUtils.getPrettyStatistics(suiteInformation.calculateStatistics()));
            }
            
            node.setMessage(message.toString());
        } 
	    return node;
	}
	
	@Override
	public AjaxModel handleController(HttpServletRequest request) throws Exception {
		User user = getAuthorizedUser(request);

		AjaxModel ajaxModel = new AjaxModel();

		ClientServerRemoteInterface server = config.lookupGridServer();

		TaskInformation[] tasks = server.getAllUserTasks(user.getId());

		Collection<TaskNode> taskNodes = new LinkedList<TaskNode>();

		for(TaskInformation task : tasks){
		    taskNodes.add(convert(task, server));
		}
		
		ajaxModel.setResult("fetched");
		ajaxModel.setObject(taskNodes);
		return ajaxModel;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}
}
