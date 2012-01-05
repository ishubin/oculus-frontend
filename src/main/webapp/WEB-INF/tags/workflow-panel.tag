<%@ tag body-content="scriptless"%>
<%@ attribute name="title" required="false"%>
<%@ attribute name="width" required="false"%>
<%@ attribute name="sign" required="false"%>
<%@ attribute name="icon" required="false"%>
<%@ attribute name="id" required="true"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:doBody var="theBody" />
<%
    String body = jspContext.getAttribute("theBody").toString();
    String[] split = body.split("<split/>");
    java.util.List<String> elements = new java.util.ArrayList<String>();
    for (int i = 0; i < split.length; i++) {
        if (split[i].contains("<a ")) {
            elements.add(split[i]);
        }
    }
    jspContext.setAttribute("elements", elements);
%>


<div class="workflow">
    <div class="workflow-title" onclick="onWorkflowPanelToggle('${id}');">
        <table border="0" width="100%">
            <tr>
                <td width="20px"><span id="workflow_${id}_toggleButton" class="small-button-hide workflow-toggle-button"></span></td>
                <td><span class="title">${title}</span></td>
                <c:if test="${icon!=null && icon!=''}"><td width="20px"><img src="../images/workflow-icon-${icon}.png"/></td></c:if>
            </tr>
        </table>
    </div>
    <div class="workflow-body" id="workflow_${id}_body">
        <ul class="workflow-list">
        <c:forEach items="${elements}" var="link" varStatus="varStatus">
            <li class="workflow-item workflow-item-${(varStatus.index%2)==0?'odd':'even'}">
                ${link}
            </li>
        </c:forEach>
        </ul>
    </div>
    <div class="workflow-footer"></div>
</div>
