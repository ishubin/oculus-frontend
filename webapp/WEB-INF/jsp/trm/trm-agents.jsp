<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="breadcrump">
    Agents
</div>

<c:if test="${fn:length(agents)==0}">
    There are no agents running. Please contact your administrator.
</c:if>
<c:forEach items="${agents}" var="agent">
    <tag:agent-layout agent="${agent}"/>
</c:forEach>