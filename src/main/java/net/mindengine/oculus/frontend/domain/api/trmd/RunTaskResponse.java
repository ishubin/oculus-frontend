package net.mindengine.oculus.frontend.domain.api.trmd;

import java.util.List;

public class RunTaskResponse {

    private Long gridTaskId;
    private List<Long> gridIncludedTaskIds;
    public Long getGridTaskId() {
        return gridTaskId;
    }
    public void setGridTaskId(Long gridTaskId) {
        this.gridTaskId = gridTaskId;
    }
    public List<Long> getGridIncludedTaskIds() {
        return gridIncludedTaskIds;
    }
    public void setGridIncludedTaskIds(List<Long> gridIncludedTaskIds) {
        this.gridIncludedTaskIds = gridIncludedTaskIds;
    }
}
