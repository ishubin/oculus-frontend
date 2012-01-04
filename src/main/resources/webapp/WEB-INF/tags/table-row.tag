<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="id" required="false"  %>

<tr class="table-body-row" <c:if test="${id!=null}">id="${id}"</c:if>>
    <jsp:doBody></jsp:doBody>
</tr>

