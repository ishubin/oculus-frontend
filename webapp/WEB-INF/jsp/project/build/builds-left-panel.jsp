<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<script language="javascript">
function onRemoveBuild(buildName,buildId)
{
    if(confirm("Are you sure you want to delete this build: "+buildName+" ?"))
    {
        document.forms.removeBuild.buildId.value = buildId;
        document.forms.removeBuild.submit();
    }
}
</script>

<form name="removeBuild" action="../project/build-remove">
    <input type="hidden" name="buildId" value="-1" />
</form>

<table border="0" width="100%">
	<c:if test="${user.hasPermissions.build_managment == true && (project.parentId==null || project.parentId==0)}">
	    <tr>
	        <td>
	            <tag:workflow-panel title="Build Management" width="100%" id="workflow_PM">
	                <tag:workflow-element icon="create" link="../project/build-create?projectId=${project.id}">New Build</tag:workflow-element>
	                <c:if test="${build!=null}">
	                   <tag:workflow-element icon="edit" link="../project/build-edit?id=${build.id}">Edit Build</tag:workflow-element>
	                   <tag:workflow-element icon="delete" link="javascript:onRemoveBuild('${build.name}', ${build.id});">Remove Build</tag:workflow-element>
	                </c:if>
	            </tag:workflow-panel>
	        </td>
	    </tr>
	</c:if>
</table>