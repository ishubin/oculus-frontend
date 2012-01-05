<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div class="breadcrump" align="center">
    <a href="../project/display-${project.path }"><img src="../images/workflow-icon-project.png"/> ${project.name }</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../project/build-display?id=${build.id}"><img src="../images/workflow-icon-build.png"/>  ${build.name }</a>
    <img src="../images/breadcrump-arrow.png"/> Edit
</div>

<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" title="Create Build" width="500px">
    <form:form method="post" commandName="build">
       <form:hidden path="projectId"/>
       <table border="0" width="100%">
           <tr>
               <td>
                   Name:
                   <br/>
                   <tag:edit-field path="name" width="100%"/>
               </td>
           </tr>
           <tr>
               <td><form:errors path="name" cssClass="error"/></td>
           </tr>
           <tr>
               <td>
                   Description:
                   <br/>
                   <tag:textarea path="description" width="100%"></tag:textarea>
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
                   <tag:submit value="Create"></tag:submit>
               </td>
           </tr>
           <tr>
               <td><form:errors cssClass="error"/></td>
           </tr>
       </table>
    </form:form>
</tag:panel>