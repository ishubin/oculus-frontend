<%@ tag body-content="scriptless" %>
<%@ attribute name="customization" required="true" type="net.mind_engine.oculus.domain.customization.FetchedCustomizationParameter" %>
<%@ attribute name="possibleValues" required="false" type="java.util.List" %>
<%@ attribute name="assignedUser" required="false" type="net.mind_engine.oculus.domain.user.User" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<c:choose>
    <c:when test="${customization.type == 'text'}">
        <tag:escape text="${customization.value}"></tag:escape>
    </c:when>
    
    <c:when test="${customization.type == 'assignee'}">
        <c:if test="${assignedUser!=null && assignedUser.login!=null && assignedUser.login!=''}">
	        <a href="../user/profile-${assignedUser.login}">
	            <img src="../images/workflow-icon-user.png"/>
	            ${assignedUser.name}
	        </a>
	    </c:if>
    </c:when>
    
    <c:when test="${customization.type == 'list'}">
        <c:forEach items="${possibleValues}" var="pv">
            <c:if test="${pv.id == customization.value}">
                <tag:escape text="${pv.possibleValue}"/>
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
                        <tag:escape text="${pv.possibleValue}"/>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </c:when>
</c:choose>