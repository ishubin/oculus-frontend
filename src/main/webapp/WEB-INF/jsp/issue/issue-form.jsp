<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<tr>
    <td>
        Name:
        <br/>
        <tag:edit-field path="name" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        External Link:
        <br/>
        <tag:edit-field path="link" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Summary:
        <br/>
        <tag:edit-field path="summary" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Description:
        <br/>
        <form:textarea path="description" cssStyle="width:100%;" rows="7"/>
    </td>
</tr>
<tr>
    <td>
        <form:hidden path="projectId"/>
        <c:choose>
            <c:when test="${subprojects!=null}">
                Subproject:
                <br/>
                <form:select path="subProjectId" size="7" cssStyle="width:100%;">
                    <form:option value="0" cssStyle="color:gray;">Not specified</form:option>
                    <c:forEach items="${subprojects}" var="subproject">
                        <form:option value="${subproject.id}"><tag:escape text="${subproject.name}"/></form:option>
                    </c:forEach>
                </form:select>
            </c:when>
        </c:choose>
        
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