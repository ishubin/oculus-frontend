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
