<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="issueId" required="true" %>
<%@ attribute name="issueName" required="true" %>
<%@ attribute name="parentPanel" required="false" %>
<%@ attribute name="parentPanelWidth" required="false" %>
<%@ attribute name="parentPanelHeight" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" name="${id}" id="${id}" value="${issueId}"/>
<div style="width:134px;height:19px;margin-bottom:5px;">
  <a class="pick-button" id="linkPickIssue${id}" href="javascript:onPickIssueClick('${id}','linkPickIssue${id}', '${parentPanel!=null ? parentPanel:''}','${parentPanelWidth}','${parentPanelHeight}');">
    <c:choose>
        <c:when test="${issueName!=null && issueId!=null && issueId!=''}">
            ${issueName}
        </c:when>
        <c:otherwise>
            Pick Issue...
        </c:otherwise>
    </c:choose>
  </a>
</div> 