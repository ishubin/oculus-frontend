<%@ include file="/include.jsp" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

${exception.className }: <tag:escape text="${exception.messageName }"/><br/>
<c:forEach items="${exception.stackTrace }" var="element">
	at ${element.className }.${element.methodName } (${element.fileName }:${element.lineNumber }) <br/>
</c:forEach>
<c:if test="${exception.moreStackTraceElements > 0 }"> ${exception.moreStackTraceElements} more...</c:if>

<c:if test="${exception.cause != null }">
	<br/>
	Caused by 
	<c:set var="exception" value="${exception.cause}" scope="request"/>
	<jsp:include page="/WEB-INF/jsp/report/exception-render.jsp"></jsp:include>
	
</c:if>

