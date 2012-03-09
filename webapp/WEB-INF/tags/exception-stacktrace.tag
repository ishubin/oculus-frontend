<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ attribute name="exception" required="true" type="net.mindengine.oculus.experior.reporter.nodes.ExceptionInfo"%>

${exception.className }: <t:escape text="${exception.messageName }"/><br/>
<c:forEach items="${exception.stackTrace }" var="element">
	at ${element.declaringClass }.${element.methodName } (${element.fileName }:${element.lineNumber }) <br/>
	
	<c:if test="${exception.cause != null }">
		<br/>
		Caused by <t:exception-stacktrace exception="${exception.cause }"></t:exception-stacktrace>
	</c:if>
</c:forEach>
