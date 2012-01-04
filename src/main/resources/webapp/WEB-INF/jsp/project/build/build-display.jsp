<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <c:if test="${build.projectPath!=null && build.projectPath!=''}">
    <a href="../project/display-${build.projectPath}"><img src="../images/workflow-icon-project.png"/>  <tag:escape text="${build.projectName}"/></a>
    <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    <img src="../images/workflow-icon-build.png"/>  <tag:escape text="${build.name}"/> 
</div>

<table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <td class="issue-table" colspan="2">
                <img src="../images/workflow-icon-build.png"/> Build -
                <b>${build.name}</b>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr class="even">
            <td class="issue-table-left">
                Project:
            </td>
            <td class="issue-table">
                <a href="../project/display-${build.projectPath}"><img src="../images/workflow-icon-project.png"/>  <tag:escape text="${build.projectName}"/></a>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Name:
            </td>
            <td class="issue-table">
                <tag:escape text="${build.name}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Description:
            </td>
            <td class="issue-table">
                <tag:escape text="${build.description}"></tag:escape>   
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Created:
            </td>
            <td class="issue-table">
                <tag:date date="${build.date}"></tag:date>
            </td>
        </tr>
        <c:forEach items="${customizationGroups}" var="cg">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <c:choose>
                        <c:when test="${cg.isMain==true}">
                             Main
                        </c:when>
                        <c:otherwise>
                             ${cg.name}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:forEach items="${cg.customizations}" var="c" varStatus="cVarStatus">
                 <tr class="${cVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         ${c.customization.name}
                     </td>
                     <td class="issue-table">
                         <tag:customization-display customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"/>
                     </td>
                 </tr>
            </c:forEach>
        </c:forEach>
    </tbody>
</table>

<!-- Comments layout -->
<br/>
<tag:comments-layout user="${user}" comments="${comments}" redirect="../project/build-display?id=${build.id}" unitId="${build.id}" unit="build"></tag:comments-layout>
