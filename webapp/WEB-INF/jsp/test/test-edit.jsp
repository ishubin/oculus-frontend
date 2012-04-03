<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>



<div class="breadcrump" align="center">
    
    <a href="../test/display?id=${test.id}"><img src="../images/iconTest.png"/> ${test.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Edit Test
</div>

<form  onsubmit="return onSubmitTest();"
    method="post">
    <%@ include file="/WEB-INF/jsp/test/test-create-content.jsp" %>
</form>
