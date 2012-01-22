<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" width="600px" 
        title="${testGroupTitle }"
    >
    <table border="0" cellpadding="10px" width="100%" cellspacing="0px">
        <tr>
            <td>
                <div class="small-description">Name:</div>
                <tag:edit-field name="name" width="100%" value="${testGroup.name }"></tag:edit-field>
                <input type="hidden" name="projectId" value="${testGroup.projectId }"/>
                
            </td>
        </tr>
        <tr>
            <td>
                <div class="small-description">Description:</div>
                <textarea name="description" style="width:100%;" rows="10"><tag:escape text="${testGroup.description }"/></textarea>
            </td>
        </tr>
        <tr>
            <td>
                <tag:submit value="${testGroupCommand}"></tag:submit>
                <div class="error">
                	<tag:spring-form-error field="" command="createTestGroup"></tag:spring-form-error>
                </div>
            </td>
        </tr>
    </table>
</tag:panel>
