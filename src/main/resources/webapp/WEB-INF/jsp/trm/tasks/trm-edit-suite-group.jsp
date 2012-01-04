<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>


<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../test-run-manager/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"></tag:escape> 
    </a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../test-run-manager/edit-task?id=${task.id}&groupId=${editSuiteGroup.id}">
        <img src="../images/workflow-icon-test-group.png"/> 
        <tag:escape text="${editSuiteGroup.name}"></tag:escape>
    </a>  
    <img src="../images/breadcrump-arrow.png"/>
    Edit Suite Group
</div>


<form:form action="../test-run-manager/edit-suite-group" 
    commandName="editSuiteGroup" 
    method="post">
    <%@ include file="/WEB-INF/jsp/trm/tasks/trm-create-suite-group-content.jsp" %>
</form:form>