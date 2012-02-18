<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<div class="breadcrump">
    Agents
</div>

<c:forEach items="${agents}" var="agent">
    <tag:agent-layout agent="${agent}"/>
</c:forEach>