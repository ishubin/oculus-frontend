<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="left-panel">
    <b>Work-flow:</b><br/><br/>
    
    <c:if test="${test.id>0}">
	    <tag:workflow-panel title="Test Links" width="100%" id="workflow_TL">
	       <tag:workflow-element icon="report" link="../report/browse?testCaseName=${test.id}">Reports</tag:workflow-element>
	    </tag:workflow-panel>
    </c:if>
	
	<c:if test="${user.hasPermissions.test_managment == true}">
	    <%@ include file="/WEB-INF/jsp/test/test-workflow.jsp" %>       
	</c:if>
</div>