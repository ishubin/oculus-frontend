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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.oculus.frontend.config.Config;
import net.mindengine.oculus.frontend.domain.trm.SuiteStatisticUtils;
import net.mindengine.oculus.frontend.web.controllers.SecureSimpleViewController;
import net.mindengine.oculus.grid.domain.agent.AgentInformation;
import net.mindengine.oculus.grid.domain.task.SuiteInformation;
import net.mindengine.oculus.grid.domain.task.SuiteStatistic;
import net.mindengine.oculus.grid.domain.task.TaskInformation;
import net.mindengine.oculus.grid.domain.task.TaskStatus;
import net.mindengine.oculus.grid.service.ClientServerRemoteInterface;

/**
 * Returns the data for jqgrid treeGrid in xml format
 * @author Ivan Shubin
 *
 */
public class AjaxMyActiveTasksController extends SecureSimpleViewController{

    private Config config;
    
    
    public class Row{
        //0 - task
        //1 - Suite
        //2 - test
        private int type;
        private String id;
        private String name;
        private Date completed;
        private Date created;
        private int percents;
        private int status;
        private String parentId;
        private boolean hasChildren;
        private AgentInformation assignedAgent;
        private String message;
        private String report;
        private int level;
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Date getCompleted() {
            return completed;
        }
        public void setCompleted(Date completed) {
            this.completed = completed;
        }
        public Date getCreated() {
            return created;
        }
        public void setCreated(Date created) {
            this.created = created;
        }
        public int getPercents() {
            return percents;
        }
        public void setPercents(int percents) {
            this.percents = percents;
        }
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public String getParentId() {
            return parentId;
        }
        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
        public boolean isHasChildren() {
            return hasChildren;
        }
        public void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }
        public void setAssignedAgent(AgentInformation assignedAgent) {
            this.assignedAgent = assignedAgent;
        }
        public AgentInformation getAssignedAgent() {
            return assignedAgent;
        }
        public void setLevel(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }
        public void setReport(String report) {
            this.report = report;
        }
        public String getReport() {
            return report;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        
    }
    
    public ClientServerRemoteInterface server() throws Exception{
        return config.lookupGridServer();
    }
    
    public Row convertTask(TaskInformation task, int parentLevel, String parentId, ClientServerRemoteInterface server){
        Row row = new Row();
        row.setName(task.getTaskName());
        row.setCompleted(task.getCompletedDate());
        row.setCreated(task.getCreatedDate());
        row.setStatus(task.getTaskStatus().getStatus());
        
        AgentInformation assignedAgent = task.getTaskStatus().getAssignedAgent();
        
        row.setMessage("");
        if ( TaskStatus.ERROR.equals(task.getTaskStatus().getStatus()) ) {
            row.setMessage(task.getTaskStatus().getMessage());
        }
        else if ( task.getParentId() == null ) {
            StringBuffer mainMessage = new StringBuffer();
            try {
                TaskInformation[] tasks = server.getTasks(task.getTaskId());
                SuiteStatistic statistics = SuiteStatisticUtils.collectStatisticFromTasks(tasks);
                if ( statistics != null ) {
                    mainMessage.append(SuiteStatisticUtils.getPrettyStatistics(statistics));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            row.setMessage(mainMessage.toString());
        }
        else if ( assignedAgent != null ) {
            if ( TaskStatus.ACTIVE.equals(task.getTaskStatus().getStatus())) {
                row.setMessage( "Assigned to " + assignedAgent.getName() );
            }
            else if ( TaskStatus.COMPLETED.equals(task.getTaskStatus().getStatus())) {
                StringBuffer message = new StringBuffer();
                message.append( "Completed on " + assignedAgent.getName());
                
                try {
                    SuiteInformation suiteInformation = task.getTaskStatus().getSuiteInformation();
                    if ( suiteInformation != null ) {
                        SuiteStatistic statistics = suiteInformation.calculateStatistics();
                        message.append(SuiteStatisticUtils.getPrettyStatistics(statistics));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                row.setMessage(message.toString());
            } 
        }
        
        
        row.setPercents(task.getTaskStatus().getPercent().intValue());
        row.setAssignedAgent(task.getTaskStatus().getAssignedAgent());
        
        if(task.getTaskStatus().getSuiteInformation()==null){
            row.setType(0);
            row.setHasChildren(true);
            row.setId("tsk"+task.getTaskId());
            
            
            TaskInformation[] childTasks = server.getTasks(task.getTaskId());
            StringBuilder report = new StringBuilder();
            boolean bcomma = false;
            for(TaskInformation childTask : childTasks) {
                if(childTask.getTaskStatus().getSuiteInformation()!=null) {
                    Long suiteId = childTask.getTaskStatus().getSuiteInformation().getSuiteId(); 
                    if ( suiteId != null ) {
                        if(bcomma) {
                            report.append(",");
                        }
                        bcomma = true;
                        report.append(Long.toString(suiteId));
                    }
                }
            }
            row.setReport(report.toString());
        }
        else {
            row.setType(1);
            row.setHasChildren(false);
            row.setId("ste"+task.getTaskId());
            Long suiteId = task.getTaskStatus().getSuiteInformation().getSuiteId();
            if(suiteId!=null) {
                row.setReport(suiteId.toString());
            }
            else row.setReport("");
        }
        
        row.setParentId(parentId);
        row.setLevel(parentLevel+1);
        return row;
    }

    @Override
    public Map<String, Object> handleController(HttpServletRequest request) throws Exception {
        String nodeId = request.getParameter("nodeid");
        String strLevel =request.getParameter("n_level");
        Integer level;
        if(strLevel!=null){
            level = Integer.parseInt(strLevel);    
        }
        else level = 0;
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        Collection<Row> rows = new LinkedList<Row>();
        TaskInformation[] tasks = null;
        Long userId = getUser(request).getId();
        
        if( nodeId == null ){
            nodeId = "0";
            tasks = server().getAllUserTasks(userId);
        }
        else if(nodeId.startsWith("tsk")) {
            Long taskId = Long.parseLong(nodeId.substring(3));
            tasks = server().getTasks(taskId);
        }
        
        if( tasks != null ) {
            for(TaskInformation task : tasks) {
                rows.add(convertTask(task, level, nodeId, server()));
            }
        }
        
        map.put("records", rows.size());
        map.put("rows", rows);
        return map;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
