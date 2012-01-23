<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:workflow-panel title="Task Actions" width="100%" id="workflow_MyTasks">
    <tag:workflow-element id="workflowRunTaskLink" icon="run" link="javascript:submitRunTask();">Run Task On Server</tag:workflow-element>
    <tag:workflow-element id="workflowExportTaskLink" link="javascript:submitExportTask();">Export Task</tag:workflow-element>
    <tag:workflow-element icon="edit" link="../test-run-manager/edit-task?id=${task.id}">Edit Task</tag:workflow-element>
</tag:workflow-panel>