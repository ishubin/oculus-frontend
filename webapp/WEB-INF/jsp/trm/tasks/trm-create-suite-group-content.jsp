<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<tag:panel align="center" width="600px" 
        title="Suite Group"
    >
    <table border="0" cellpadding="10px" width="100%" cellspacing="0px">
        <tr>
            <td>
                <div class="small-description">Name:</div>
                <tag:edit-field name="name" value="${suiteGroup.name}" width="100%"></tag:edit-field>
                
                <input type="hidden" name="taskId"  value="${suiteGroup.taskId}"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="small-description">Description:</div>
                <textarea name="description" style="width:100%;" rows="10"><tag:escape text="${suiteGroup.description }"/></textarea>
            </td>
        </tr>
        <tr>
            <td>
            	<tag:submit value="${suiteGroupCommand}"></tag:submit>
             	
             	<div class="error">   
                	<tag:spring-form-error field="" command="suiteGroup"></tag:spring-form-error>
                </div>
            </td>
        </tr>
    </table>
</tag:panel>
