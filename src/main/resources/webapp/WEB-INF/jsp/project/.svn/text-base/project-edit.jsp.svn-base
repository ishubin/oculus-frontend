<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Edit Project
</div>

<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" title="Edit Project" width="600px">
<form:form method="post" commandName="project" enctype="multipart/form-data">
    <table border="0" width="100%">
        <tr>
            <td>
                Name:<br/>
                <tag:edit-field path="name" width="100%"/>
                <form:errors path="name" cssClass="error"/>
                <form:hidden path="parentId"/>
            </td>
        </tr>
        <tr>
            <td>
                Path:<br/>
                <tag:edit-field path="path" width="100%"/>
                <form:errors path="path" cssClass="error"/>
            </td>
        </tr>
        <tr>
            <td>
                Description:<br/>
                <form:textarea path="description" cssStyle="width:100%;" cols="30"  rows="6" cssClass="border-textarea" cssErrorClass="error"/>
            </td>
        </tr>
        <tr>
            <td>
                Icon:
                <input type="file" name="iconFile"/><br/>
                <c:if test="${project.icon!=null}">
                    
                </c:if>
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
                <tag:submit value="Change"/>
                <br/>
                <form:errors path=""/>
            </td>
        </tr>
    </table>
</form:form>
</tag:panel>