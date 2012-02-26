<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:workflow-panel title="Task Actions" width="100%" id="workflow_MyTasks">
    <tag:workflow-element id="workflowRunTaskLink" icon="run" link="javascript:submitRunTask();">Run Task On Server</tag:workflow-element>
    <tag:workflow-element id="workflowExportTaskLink" link="javascript:submitExportTask();">Export Task</tag:workflow-element>
    <tag:workflow-element icon="edit" link="../grid/edit-task?id=${task.id}">Edit Task</tag:workflow-element>
</tag:workflow-panel>

<b>Selected agents:</b>
<div id="selectedAgentsLayout_No_Agents" style="display:none;">
	There are no available agents that match your filter
</div>

<ul id="selectedAgentsList">
	<c:forEach items="${agents}" var="agent">
		<li agent-name="<tag:escape text="${agent.agentInformation.name}"/>"><tag:escape text="${agent.agentInformation.name}"/></li>
	</c:forEach>
</ul>