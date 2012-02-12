<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>


<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../grid/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        ${task.name}
    </a> 
    <img src="../images/breadcrump-arrow.png"/>
    Create Suite Group
</div>


<form action="../grid/create-suite-group" 
    method="post">
    <%@ include file="/WEB-INF/jsp/trm/tasks/trm-create-suite-group-content.jsp" %>
</form>