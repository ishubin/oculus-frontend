<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="title" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<table class="workflow" border="0" cellpadding="0" cellspacing="0" 
        <c:if test="${width!=null}">width="${width}"</c:if>
        <c:if test="${align!=null}">align="${align}"</c:if>
        >
    <thead>
        <tr>
	        <td class="workflow">
	            <div class="workflow-title">${title}</div>
	        </td>
	    </tr>
    </thead>
    <tbody>
        <tr>
            <td class="workflow">
                <jsp:doBody/>
            </td>
        </tr>
    </tbody>
</table>

