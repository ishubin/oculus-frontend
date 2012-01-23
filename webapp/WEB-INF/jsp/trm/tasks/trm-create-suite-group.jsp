<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>


<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../test-run-manager/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        ${task.name}
    </a> 
    <img src="../images/breadcrump-arrow.png"/>
    Create Suite Group
</div>


<form action="../test-run-manager/create-suite-group" 
    method="post">
    <%@ include file="/WEB-INF/jsp/trm/tasks/trm-create-suite-group-content.jsp" %>
</form>