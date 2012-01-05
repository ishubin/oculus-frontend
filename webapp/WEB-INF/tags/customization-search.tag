<%@ tag body-content="scriptless" %>
<%@ attribute name="customization" required="true" type="net.mindengine.oculus.frontend.domain.customization.FetchedCustomizationParameter" %>
<%@ attribute name="possibleValues" required="false" type="java.util.List" %>
<%@ attribute name="useDefaultEmptyValues" required="false" type="java.lang.Boolean" %>
<%@ attribute name="fetchConditionType" required="false" %>
<%@ attribute name="assignedUser" required="false" type="net.mindengine.oculus.frontend.domain.user.User" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<c:choose>
    <c:when test="${customization.type == 'text'}">
        <textarea class="border-textarea" rows="7" style="width:100%;" name="customization_${customization.id}" id="customization_${customization.id}"><tag:escape text="${customization.value}"></tag:escape></textarea>
    </c:when>
    
    <c:when test="${customization.type == 'assignee'}">
        <tag:pickuser-button userId="${customization.value}" userName="${assignedUser.name}" id="customization_${customization.id}"></tag:pickuser-button>
    </c:when>
    
    <c:when test="${customization.type == 'list'}">
        <select name="customization_${customization.id}" 
            <c:if test="${customization.subtype == 'list' }">size="7" style="width:100%;"</c:if>
        >
            <c:if test="${useDefaultEmptyValues==true}">
                <option value="" style="color:gray;font-style: italic;">Not selected</option>
            </c:if>
            <c:forEach items="${possibleValues}" var="pv">
                <option value="${pv.id}" <c:if test="${pv.id == customization.value}">selected="selected"</c:if>>
                    <tag:escape text="${pv.possibleValue}"/>
                </option>
            </c:forEach>
        </select>
    </c:when>
    <c:when test="${customization.type == 'checkbox'}">
        <select name="customization_${customization.id}" id="customization_${customization.id}">
            <option value=""></option>
            <option value="true" <c:if test="${customization.value == 'true' }">selected="selected"</c:if> >true</option>
            <option value="false" <c:if test="${customization.value == 'false' }">selected="selected"</c:if>>false</option>
        </select>
    </c:when>
    <c:when test="${customization.type == 'checklist'}">
        <select name="customization_${customization.id}_fetch_type">
            <option value="2">at least one</option>
            <option value="1" <c:if test="${fetchConditionType=='1'}">selected="selected"</c:if>>strict</option>
        </select>
        <div style="padding-left:10px;"
            <c:if test="${fn:length(possibleValues)>10}"> class="customization-checklist"</c:if> 
        >
            <table border="0">
                <c:forEach items="${possibleValues}" var="pv">
                    <tr>
                        <td><input type="checkbox" name="customization_${customization.id}_pv_${pv.id}" id="customization_${customization.id}_pv_${pv.id}" <c:if test="${pv.isSet==true}">checked="checked"</c:if> /></td>
                        <td><label for="customization_${customization.id}_pv_${pv.id}"><tag:escape text="${pv.possibleValue}"/></label></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:when>
</c:choose>