<%@ tag body-content="scriptless" %>
<%@ attribute name="customization" required="true" type="net.mindengine.oculus.frontend.domain.customization.FetchedCustomizationParameter" %>
<%@ attribute name="possibleValues" required="false" type="java.util.List" %>
<%@ attribute name="assignedUser" required="false" type="net.mindengine.oculus.frontend.domain.user.User" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:choose>
    <c:when test="${customization.type == 'text'}">
        <tag:escape text="${customization.value}"></tag:escape>
    </c:when>
    
    <c:when test="${customization.type == 'assignee'}">
        <c:if test="${assignedUser!=null && assignedUser.login!=null && assignedUser.login!=''}">
	        <a href="../user/profile-${assignedUser.login}">
	            <img src="../images/workflow-icon-user.png"/>
	            ${fn:escapeXml(assignedUser.name)}
	        </a>
	    </c:if>
    </c:when>
    
    <c:when test="${customization.type == 'list'}">
        <c:forEach items="${possibleValues}" var="pv">
            <c:if test="${pv.id == customization.value}">
                ${fn:escapeXml(pv.possibleValue)}"
            </c:if>
        </c:forEach>
    </c:when>
    <c:when test="${customization.type == 'checkbox'}">
        <c:choose>
            <c:when test="${customization.value == 'true' }">
                Yes
            </c:when>
            <c:otherwise>
                No
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:when test="${customization.type == 'checklist'}">
        <ul>
            <c:forEach items="${possibleValues}" var="pv">
                <c:if test="${pv.isSet==true}">
                    <li>
                        ${fn:escapeXml(pv.possibleValue)}
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </c:when>
</c:choose>