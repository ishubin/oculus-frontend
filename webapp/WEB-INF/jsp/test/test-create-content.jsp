<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<%
if(pageContext.findAttribute("createTest")==null)
{
    pageContext.setAttribute("testPanelTitle","Edit Test");
}
else pageContext.setAttribute("testPanelTitle","Create Test");
%>

<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" width="600px" 
        title="${testPanelTitle}"
    >
	<table border="0" cellpadding="10px" width="100%" cellspacing="0px">
	    <tr>
	        <td>
	            <div class="small-description">Name:</div>
	            <tag:edit-field path="name" width="100%"></tag:edit-field>
	            
	            <c:if test="${createTest!=null}">
	            <input type="hidden" name="projectId" value="${project.id}"/>
	            
	            </c:if>
	        </td>
	    </tr>
	    <tr>
	        <td>
	            <div class="small-description">Description:</div>
	            <form:textarea path="description" cssStyle="width:100%;" rows="10" cssErrorClass="error"/>
	        </td>
	    </tr>
	    <tr>
	        <td>
	            <div class="small-description">Mapping:</div>
	            <tag:edit-field path="mapping" width="100%"></tag:edit-field>
	        </td>
	    </tr>
	    <tr>
            <td>
                <div class="small-description">Test Group:</div>
                <select name="groupId">
                    <option value="0" style="color:gray;">No group...</option>
                    <c:forEach items="${groups}" var="g">
                        <option value="${g.id}" <c:if test="${group!=null &&group.id == g.id }">selected="selected"</c:if>><tag:escape text="${g.name}"/></option>
                    </c:forEach>
                </select>
            </td>
        </tr>
	    
	    <!-- A list of customization parameter groups -->
        <c:forEach items="${customizationGroups}" var="cgs">
            <tr>
                <td>
                    <tag:panel-border title="${cgs.name}" align="left" width="100%">
                       <table border="0" width="100%">
                           <c:forEach items="${cgs.customizations}" var="c">
                              <tr>
                                  <td>
                                      <b>${c.customization.name}</b>
                                      <br/>
                                      ${c.customization.description}
                                  </td>
                              </tr>
                              <tr>
                                  <td>
                                      <tag:customization-edit customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-edit>
                                      <br/>
                                      <br/>
                                  </td>
                              </tr>        
                           </c:forEach>
                       </table>
                    </tag:panel-border>
                </td>
            </tr>
        </c:forEach>
	    <tr>
	        <td>
	            <c:choose>
	                <c:when test="${createTest!=null}">
	                    <tag:submit value="Create"></tag:submit>
	                </c:when>
	                <c:otherwise>
	                    <tag:submit value="Save"></tag:submit>
	                </c:otherwise>
	            </c:choose>
	            
	            <form:errors path=""/>
	        </td>
	    </tr>
	</table>
</tag:panel>
