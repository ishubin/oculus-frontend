<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Choose Unit
</div>

<script language="javascript">
function onUnitChange(el)
{
	document.forms.formChooseUnit.submit();
}
</script>

<br/>
<br/>

<tag:panel align="center" title="Choose unit" width="400px">
    <div align="center">
        Unit: 
        <form name="formChooseUnit" method="get">
		    <select name="unit" onchange="onUnitChange(this);">
		        <option style="color:gray;" disabled="disabled" selected="selected">Select unit...</option>
		        <option value="project">Sub-Project</option>
		        <option value="test">Test</option>
		        <option value="test-case">Test-Case</option>
		        <option value="issue">Issue</option>
		        <option value="build">Build</option>
		    </select>
	    </form>
    </div>
    <br/>
    <br/>
</tag:panel>