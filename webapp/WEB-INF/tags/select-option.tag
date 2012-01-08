<%@tag import="java.util.Enumeration"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="style" required="false"%>
<%@ attribute name="check" required="false"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<option value="${value}" style="${style}" <c:if test="${check!=null && check==value }">selected="selected"</c:if>><jsp:doBody></jsp:doBody></option>
