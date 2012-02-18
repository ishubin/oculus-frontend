<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:workflow-panel title="Task Actions" width="100%" id="workflow_MyTasks">
    <tag:workflow-element id="workflowRunTaskLink" icon="run" link="javascript:submitRunTask();">Run Task On Server</tag:workflow-element>
    <tag:workflow-element id="workflowExportTaskLink" link="javascript:submitExportTask();">Export Task</tag:workflow-element>
    <tag:workflow-element icon="edit" link="../grid/edit-task?id=${task.id}">Edit Task</tag:workflow-element>
</tag:workflow-panel>

<br/>
<b>Agent Filter:</b>
<c:forEach items="${agentTags}" var="tag">
	<div class="agent-selected-tag-layout" agent-tag="${tag.value}" style="display:none;">
		<a href="#" class="agent-tag agent-tag-click-remove" agent-tag="${tag.value}">
			<c:if test="${tag.iconImage!=null}"><img src="${tag.iconImage}" <c:if test="${tag.iconSize!=null}">width="${tag.iconSize.width}px" height="${tag.iconSize.height}px"</c:if> /></c:if>
			<span><tag:escape text="${tag.value}"/></span>
		</a>
	</div>
</c:forEach>