<%@ include file="/include.jsp" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<%
if(pageContext.findAttribute("parentProject")==null)
{
    pageContext.setAttribute("createProjectTitle","Create Project");
}
else pageContext.setAttribute("createProjectTitle","Create Sub-Project");
%>


<tag:pickuser-setup></tag:pickuser-setup>


<c:if test="${parentProject!=null}">
    <table class="issue-table" border="0" align="center" width="100%" cellpadding="0" cellspacing="0">
        <tbody>
            <tr>
                <td class="issue-table" rowspan="5" width="100px">
                    <c:choose>
                        <c:when test="${parentProject.icon!=null && parentProject.icon!=''}">
                            <img width="120px" src="../display/file?type=project-icon&projectId=${parentProject.id}&object=${parentProject.icon}"/>
                        </c:when>
                        <c:otherwise>
                            <img width="120px" src="../images/project-no-icon.png"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="issue-table issue-table-head" colspan="2">
                    Project - <a class="nice-link" href="../project/display-${parentProject.path}">${parentProject.name}</a>
                </td>
            </tr>
            <tr class="odd">
                <td class="issue-table-left">
                    Name:
                </td>
                <td class="issue-table">
                    ${parentProject.name}
                </td>
            </tr>
            <tr class="even">
                <td class="issue-table-left">
                    Description:
                </td>
                <td class="issue-table">
                    ${parentProject.description}
                </td>
            </tr>
            <tr class="odd">
                <td class="issue-table-left">
                    Subprojects:
                </td>
                <td class="issue-table">
                    ${parentProject.subprojectsCount}
                </td>
            </tr>
            <tr class="even">
                <td class="issue-table-left">
                    Tests:
                </td>
                <td class="issue-table">
                    ${parentProject.testsCount}
                </td>
            </tr>
        </tbody>
    </table>
</c:if>
<tag:panel title="${createProjectTitle}" align="center" width="600px">
	<form method="post" action="../project/create">
	    <table border="0" width="100%">
	        <tr>
	            <td>
	                <div class="small-description">Name:</div>
	                <tag:edit-field name="name" value="${createProject.name}" width="100%"/>
	                
	                <c:choose>
	                    <c:when test="${parentProject!=null}">
	                        <input type="hidden" name="parentId" value="${parentProject.id}"/>
	                    </c:when>
	                    <c:otherwise>
	                        <input type="hidden" name="parentId" value="0"/>
	                    </c:otherwise>
	                </c:choose>
	            </td>
	        </tr>
	        <tr>
	            <td>
	                <div class="small-description">Path:</div>
	                <tag:edit-field name="path" value="${createProject.path}" width="100%"/>
	                
	            </td>
	        </tr>
	        <tr>
	            <td>
	                <div class="small-description">Description:</div>
	                <textarea name="description" rows="10" style="width:100%"><tag:escape text="${createProject.description}"></tag:escape></textarea>
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
	            <td align="center">
	                <tag:submit value="Create"/>
	                <br/>
	                <div class="error">
	                	<tag:spring-form-error field="" command="createProject"></tag:spring-form-error>
	                </div>
	            </td>
	        </tr>
	    </table>
	</form>
</tag:panel>
