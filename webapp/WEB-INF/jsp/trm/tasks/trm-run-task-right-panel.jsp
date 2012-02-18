<%@ include file="/include.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<b>Agents Tags:</b>
<br/>
<c:forEach items="${agentTags}" var="tag">
	<a href="#" class="agent-tag agent-tag-click-add" agent-tag="${tag.value}">
		<c:if test="${tag.iconImage!=null}"><img src="${tag.iconImage}" <c:if test="${tag.iconSize!=null}">width="${tag.iconSize.width}px" height="${tag.iconSize.height}px"</c:if> /></c:if>
		<span><tag:escape text="${tag.value}"/></span>
	</a>
	<br/>
</c:forEach>