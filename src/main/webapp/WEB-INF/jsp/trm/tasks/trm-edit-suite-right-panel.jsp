<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%@page import="net.mindengine.oculus.frontend.domain.test.TestSearchFilter"%><b>Filter:</b>

<tag:pickuser-setup></tag:pickuser-setup>
<%
pageContext.setAttribute("pageLimitOptions",TestSearchFilter.PAGE_LIMITS);
%>


<form name="searchFilter" onsubmit="loadTestList(); return false;" action="" method="get">
      <input type="hidden" name="projectId" value="${task.projectId}"/>
      <table border="0" width="100%">
       <tr>
           <td>
               <tag:submit value="Apply Filter"/>
           </td>
       </tr>
       <tr>
           <td>
               <tag:panel align="center" id="common" title="Common" width="100%" disclosure="true">
                   Display Results:<br/>
                   <select name="pageLimit">
                       <c:forEach items="${pageLimitOptions}" var="limit" varStatus="limitVarStatus">
                           <option value="${limitVarStatus.index}" <c:if test="${limitVarStatus.index==2}">selected="selected"</c:if>>
                               ${limit}
                           </option>
                       </c:forEach>
                   </select>
                   <br/><br/>
                   Test name:
                   <br/>
                   <tag:edit-field-simple name="name" id="searchFilterName" width="100%"/>
                   <br/>
                   Sub-Project:
                   <br/>
                   <select name="subproject" style="width: 100%;">
                        <option value=""> </option>
                        <c:forEach items="${subprojects}" var="sp">
                            <option value="${sp.id}"><tag:escape text="${sp.name}"></tag:escape></option>
                        </c:forEach>
                   </select>
                   <br/>
                   <br/>
                   Group:
                   <br/>
                   <tag:edit-field-simple name="testGroup" id="searchFilterGroup" width="100%"/>
                   <br/>
                   Designer:
                   <br/>
                   <tag:edit-field-simple name="designer" id="searchFilterDesigner" width="100%"/>
               </tag:panel>
           </td>
       </tr>
       <c:forEach items="${customizationGroups}" var="cg" varStatus="cgVarStatus">
           <tr>
               <td>
                   <tag:panel align="left" id="customization_${cgVarStatus.index}" title="${cg.name}" width="100%" logo="../images/workflow-icon-settings.png" disclosure="true">
                       <c:forEach items="${cg.customizations}" var="c">
                           ${c.customization.name}
                           <br/>
                           <tag:customization-search fetchConditionType="${c.fetchConditionType}" useDefaultEmptyValues="true" customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-search>
                           <br/><br/>
                       </c:forEach>
                   </tag:panel>
               </td>
           </tr>
       </c:forEach>
       <tr>
           <td>
               <tag:submit value="Apply Filter"/>
           </td>
       </tr>
   </table>
</form>

