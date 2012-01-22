<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../customization/project-${project.path}">Choose Unit</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Customize
    <c:choose>
        <c:when test="${unit == 'project'}">
            Sub-Project
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'test'}">
            Test
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'test-case'}">
            Test-Case
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'issue'}">
            Issue
        </c:when>
    </c:choose>
</div>

<form action="../customization/add-parameter" method="get">
    <input type="hidden" name="projectId" value="${project.id}"/>
    <input type="hidden" name="unit" value="${unit}"/>
    <tag:submit onclick="" value="Add Parameter"></tag:submit>
</form>
<script language="javascript">
function onRemoveParameterClick(id,projectPath, unit){
	if(confirm("Are you sure you want to delete this parameter? All related values of the unit will be lost")){
		document.forms["formDeleteCustomization"].id.value = id;
		document.forms["formDeleteCustomization"].projectPath.value = projectPath;
		document.forms["formDeleteCustomization"].unit.value = unit;
		document.forms["formDeleteCustomization"].submit();
	}
}
</script>
<form name="formDeleteCustomization" action="../customization/delete-parameter" method="post">
    <input type="hidden" name="id" value="${c.customization.id}"/>
    <input type="hidden" name="projectPath" value="${project.path}"/>
    <input type="hidden" name="unit" value="${unit}"/>
</form>
<table id="customizationsTable" border="0">
    <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Values</th>
        <th>Group</th>
        <th>Edit</th>
        <th>Remove</th>
    </tr>
    <tbody>
	    <c:forEach items="${customizations}" var="c">
	        <tr>
	            <td><span style="font-size:10pt; font-weight:bold;"><tag:escape text="${c.customization.name}"/></span><br/><tag:escape text="${c.customization.description}"></tag:escape></td>
	            <td>${c.customization.type}<c:if test="${c.customization.type=='list'}"> (${c.customization.subtype}) </c:if></td>
	            <td><c:if test="${c.customization.type=='list' || c.customization.type=='checklist'}"><ul><c:forEach items="${c.possibleValues}" var="pv"><li><tag:escape text="${pv.possibleValue}"/></li></c:forEach></ul></c:if></td>
	            <td><tag:escape text="${c.customization.groupName}"/></td>
	            <td><a href="../customization/edit-parameter?id=${c.customization.id}">Edit</a></td>
	            <td><a href="javascript:onRemoveParameterClick(${c.customization.id},'${project.path}','${unit}');">Remove</a></td>
	        </tr>
	    </c:forEach>
    </tbody>
</table>
<script>
$(document).ready(function(){
	tableToGrid("#customizationsTable",{
		height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'Customizations'
	});
});
</script>
<br/>
<form action="../customization/add-parameter" method="get">
    <input type="hidden" name="projectId" value="${project.id}"/>
    <input type="hidden" name="unit" value="${unit}"/>
    <tag:submit onclick="" value="Add Parameter"></tag:submit>
</form>