<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:workflow-panel title="Shared Task Actions" width="100%" id="workflow_SharedTasks">
<tag:workflow-element id="workflowRunTaskLink" icon="prepare-run" link="../grid/run-task?taskId=${task.id}">Prepare task</tag:workflow-element>
    <tag:workflow-element id="workflowCopyTaskLink" icon="subcreate" link="javascript:showSopySharedTaskDialog();">Copy this task</tag:workflow-element>
</tag:workflow-panel>