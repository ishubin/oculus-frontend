<%@page import="net.mindengine.oculus.frontend.domain.test.Test"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<tag:workflow-panel title="Test Management" width="100%" id="workflow_TM">
    <tag:workflow-element icon="create" link="../test/create?projectId=${project.id}&groupId=${group!=null?group.id:'0'}">New Test</tag:workflow-element>
    <c:if test="${test.id>0}">
        <tag:workflow-element icon="edit" link="../test/edit?id=${test.id}">Edit Test</tag:workflow-element>
        <tag:workflow-element icon="delete" onclick="return confirm('Are you sure you want to delete this test');" link="../test/delete?id=${test.id}">Delete Test</tag:workflow-element>
        
    </c:if>
</tag:workflow-panel>

<tag:workflow-panel title="Test Groups" width="100%" id="workflow_TG">
    <c:choose>
        <c:when test="${group==null}">
            <tag:workflow-element icon="create" link="../test/create-group?projectId=${project.id}">New Group</tag:workflow-element>
        </c:when>
        <c:otherwise>
            <tag:workflow-element icon="edit" link="../test/edit-group?groupId=${group.id}">Edit Group</tag:workflow-element>
            <tag:workflow-element icon="delete" link="../test/delete-group?groupId=${group.id}" onclick="return confirm('Are you sure you want to delete this group?');">Remove Group</tag:workflow-element>
        </c:otherwise>
    </c:choose>
</tag:workflow-panel>

