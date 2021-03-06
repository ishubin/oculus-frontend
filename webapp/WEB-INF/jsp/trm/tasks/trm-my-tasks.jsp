<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<div class="breadcrump">
     My Tasks
</div>

<table id="myTasksTable" border="0" style="display:none">
    <tr>
        <th>Name</th>
        <th>Created</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${tasks}" var="task">
        <tr>
            <td><a href="../grid/edit-task?id=${task.id}"><img src="../images/workflow-icon-task.png" width="16px"/> <tag:escape text="${task.name}"/></a><c:if test="${task.description!=null && task.description!=''}"><br/><span class="small-description"><tag:escape text="${task.description}"></tag:escape></span></c:if></td>
            <td width="120px"><tag:date date="${task.date}"/></td>
            <td width="120px"><a class="button" href="../grid/run-task?taskId=${task.id}"><img src="../images/workflow-icon-prepare-run.png" width="16px"/> Run Task</a></td>
        </tr>
    </c:forEach>
</table>
<script>

$(document).ready(function (){
	$("#myTasksTable").show();
	tableToGrid("#myTasksTable",{
        height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'My Tasks'
    });
});
</script>