<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

 <b>Work-flow:</b><br/><br/>
<table border="0" width="100%" cellspacing="5px">
    <c:if test="${user.hasPermissions.issue_managment == true}">
        <tr>
            <td>
                <tag:workflow-panel title="Issue Management" width="100%" id="workflow_IM">
                    <tag:workflow-element icon="bug-create" link="../issue/create">Create Issue</tag:workflow-element>
                    <c:if test="${issue.id > 0}">
                        <tag:workflow-element icon="bug-edit" link="../issue/edit?id=${issue.id}">Edit Issue</tag:workflow-element>
                    </c:if>
                </tag:workflow-panel>
            </td>
        </tr>
    </c:if>
</table>