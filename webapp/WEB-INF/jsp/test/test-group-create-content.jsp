<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" width="600px" 
        title="${testGroupTitle }"
    >
    <p>
        Name:<br/>
        <tag:edit-field name="name" width="100%" value="${testGroup.name }"></tag:edit-field>
        <input type="hidden" name="projectId" value="${testGroup.projectId }"/>
    </p>
    <p>
        Description:<br/>
        <textarea class="custom-edit-text" name="description" style="width:100%;" rows="10"><tag:escape text="${testGroup.description }"/></textarea>
    </p>
    <tag:submit value="${testGroupCommand}"></tag:submit>
    <div class="error">
        <tag:spring-form-error field="" command="createTestGroup"></tag:spring-form-error>
    </div>
</tag:panel>
