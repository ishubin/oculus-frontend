<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<c:if test="${user.hasPermissions.dashboard_edit == true}">
    <tag:workflow-panel id="dashboard" title="Dashboard" width="100%">
        <tag:workflow-element icon="edit" link="../dashboard/edit?projectId=${project.id}">Edit Dashboard</tag:workflow-element>
    </tag:workflow-panel>
</c:if>

<tag:workflow-panel id="subprojects" title="Sub-Projects" width="100%">
    <c:forEach items="${subprojectCharts}" var="subprojectChart">
        <tag:workflow-element icon="subproject" link="\#${subprojectChart.project.path}">${subprojectChart.project.name}</tag:workflow-element>
    </c:forEach>
</tag:workflow-panel>

