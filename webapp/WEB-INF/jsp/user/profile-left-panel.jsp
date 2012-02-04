<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<c:if test="${user!=null && user.hasPermissions.user_managment}">
    <tag:workflow-panel id="userManagement" title="User Management" width="100%">
        <tag:workflow-element link="../admin/user-create" icon="add-user">Create User</tag:workflow-element>
        <c:if test="${choosenUser!=null}">
            <tag:workflow-element link="../admin/edit-user?id=${choosenUser.id}" icon="edit-user">Edit User</tag:workflow-element>
            <tag:workflow-element onclick="return confirm('Are you sure you want to delete this user?');" link="../admin/delete-user?id=${choosenUser.id}" icon="delete-user">Remove User</tag:workflow-element>
        </c:if>
    </tag:workflow-panel>
    
    
</c:if>
<c:if test="${user!=null}">
    <tag:workflow-panel id="userActions" title="Actions" width="100%">
        <tag:workflow-element link="../user/change-password" icon="lock">Change Password</tag:workflow-element>
    </tag:workflow-panel>
</c:if>