<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>



<div class="breadcrump" align="center">
    
    <a href="../test/display?id=${editTest.id}"><img src="../images/iconTest.png"/> ${editTest.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Edit Test
</div>

<form:form 
    commandName="editTest" 
    method="post">
    <%@ include file="/WEB-INF/jsp/test/test-create-content.jsp" %>
</form:form>
