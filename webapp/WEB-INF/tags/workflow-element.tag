<%@ tag body-content="scriptless" %>
<%@ attribute name="link" required="true" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="icon" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="title" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<split/>
<a class="workflow-panel-link" href="${link}" 
    <c:if test="${onclick!=null}">onclick="${onclick}"</c:if>
    <c:if test="${id!=null}">id="${id}"</c:if>
    <c:if test="${title!=null}">title="${title}"</c:if>
    >
    <c:choose>
	    <c:when test="${icon!=null}">
	       <img class="workflow-icon" src="../images/workflow-icon-${icon}.png"/>
	    </c:when>
	    <c:otherwise>
	       <img class="workflow-icon" src="../images/workflow-icon-standard.png"/> 
	    </c:otherwise>
	</c:choose>
    <jsp:doBody/>
</a>