<%@ tag body-content="scriptless" %>
<%@ attribute name="style" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="colspan" required="false" %>
<%@ attribute name="rowspan" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<td class="table-cell table-body-cell"
    <c:if test="${style!=null}">style="${style}"</c:if>
    <c:if test="${width!=null}">width="${width}"</c:if>
    <c:if test="${colspan!=null}">colspan="${colspan}"</c:if>
    <c:if test="${rowspan!=null}">rowspan="${rowspan}"</c:if>  
    >
    <jsp:doBody></jsp:doBody>
</td>

