<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    
    <a href="../grid/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <c:if test="${group!=null }">
        <img src="../images/breadcrump-arrow.png"/>
        <a href="../grid/edit-task?id=${task.id}&groupId=${group.id}">
            <img src="../images/workflow-icon-test-group.png"/> 
            <tag:escape text="${group.name}"/>
        </a>
    </c:if>
    
    <img src="../images/breadcrump-arrow.png"/>
    <img src="../images/workflow-icon-suite.png"/>
    Create Suite
</div>

<tag:panel title="Create suite" align="center">
    <form action="../grid/create-suite?taskId=${task.id}" method="post">
        <p>
            Name:<br/>
            <tag:edit-field name="name" width="100%" value="${suite.name}"/>
        </p>
        <p>
            Description:<br/>
            <textarea name="description" cols="40"  style="width:100%;" rows="7"><tag:escape text="${suite.description}"/></textarea>
        </p>
        <tag:submit value="Create"></tag:submit>
        <div class="error">
        	<tag:spring-form-error field="" command="suite"></tag:spring-form-error>
        </div>
    </form>
</tag:panel> 