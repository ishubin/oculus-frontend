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
package net.mindengine.oculus.frontend.domain.trm;

/**
 * Used to be bind to database table "trm_task_dependencies"
 * @author ishubin
 *
 */
public class TrmTaskDependency {

    private Long id;
    private Long taskId;
    private Long refTaskId;
    private String refTaskName;
    private Long   ownerId;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTaskId() {
        return taskId;
    }
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public Long getRefTaskId() {
        return refTaskId;
    }
    public void setRefTaskId(Long refTaskId) {
        this.refTaskId = refTaskId;
    }
    public String getRefTaskName() {
        return refTaskName;
    }
    public void setRefTaskName(String refTaskName) {
        this.refTaskName = refTaskName;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getOwnerId() {
        return ownerId;
    }
}
