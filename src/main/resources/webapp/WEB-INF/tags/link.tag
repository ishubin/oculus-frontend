<%@ tag body-content="scriptless" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="js" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a
	<c:choose>
	    <c:when test="${js==true}">
	        href="${url}" 
	    </c:when>
	    <c:otherwise>
	        href="..${url}" 
	    </c:otherwise>
	</c:choose>
        
    <c:if test="${onclick!=null}">onClick="${onclick}"</c:if>
    >
    ${name}	
</a>