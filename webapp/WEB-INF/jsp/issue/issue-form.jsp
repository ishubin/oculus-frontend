<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<tr>
    <td>
        Name:
        <br/>
        <tag:edit-field name="name" value="${issue.name}" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        External Link:
        <br/>
        <tag:edit-field name="link" value="${issue.link}" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Summary:
        <br/>
        <tag:edit-field name="summary" value="${issue.summary}" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Description:
        <br/>
        <textarea name="description" style="width:100%;" rows="7"><tag:escape text="${issue.description }"></tag:escape></textarea>
    </td>
</tr>
<tr>
    <td>
        <input type="hidden" name="projectId" value="${issue.projectId}"/>
        <c:choose>
            <c:when test="${subprojects!=null}">
                Subproject:
                <br/>
                <select name="subProjectId" size="7" style="width:100%;">
                    <option value="0" style="color:gray;">Not specified</option>
                    <c:forEach items="${subprojects}" var="subproject">
                        <tag:select-option value="${subproject.id}"  check="${issue.subProjectId}"><tag:escape text="${subproject.name}"/></tag:select-option>
                    </c:forEach>
                </select>
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