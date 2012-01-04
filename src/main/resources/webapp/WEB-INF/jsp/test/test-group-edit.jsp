<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <c:if test="${parentProject!=null}">
    <a href="../project/display-${parentProject.path}"><img src="../images/workflow-icon-project.png"/> ${parentProject.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../project/display-${project.path}?groupId=${editTestGroup.id}">
        ${editTestGroup.name}
    </a>
    <img src="../images/breadcrump-arrow.png"/>
    Edit
</div>


<br/>
<br/>
<form:form action="../test/edit-group" 
    commandName="editTestGroup" 
    method="post">
    <form:hidden path="id"/>
    <%@ include file="/WEB-INF/jsp/test/test-group-create-content.jsp" %>
</form:form>