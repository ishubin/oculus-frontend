<%@page import="net.mindengine.oculus.frontend.domain.test.Test"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tag:workflow-panel title="Test Management" width="100%" id="workflow_TM">
    <tag:workflow-element icon="create" link="../test/create?projectId=${project.id}&groupId=${group!=null?group.id:'0'}">New Test</tag:workflow-element>
    <c:if test="${test.id>0}">
        <tag:workflow-element icon="edit" link="../test/edit?id=${test.id}">Edit Test</tag:workflow-element>
        <tag:workflow-element icon="settings" link="../test/customize?id=${test.id}">Test Parameters</tag:workflow-element>
        <tag:workflow-element icon="subcreate" link="" onclick="openCopyParametersDialog();return false;" title="Copy parameters from other tests">Copy Parameters</tag:workflow-element>
        <tag:workflow-element icon="delete" onclick="return confirm('Are you sure you want to delete this test');" link="../test/delete?id=${test.id}">Delete Test</tag:workflow-element>
        <tag:workflow-element icon="link-to-file" link="javascript:onLinkTestWithTestCaseClick();">Link with Test-Case</tag:workflow-element>
    </c:if>
</tag:workflow-panel>

<tag:workflow-panel title="Test Groups" width="100%" id="workflow_TG">
    <c:choose>
        <c:when test="${group==null}">
            <tag:workflow-element icon="create" link="../test/create-group?projectId=${project.id}">New Group</tag:workflow-element>
        </c:when>
        <c:otherwise>
            <tag:workflow-element icon="edit" link="../test/edit-group?groupId=${group.id}">Edit Group</tag:workflow-element>
            <tag:workflow-element icon="delete" link="../test/delete-group?groupId=${group.id}" onclick="return confirm('Are you sure you want to delete this group?');">Remove Group</tag:workflow-element>
        </c:otherwise>
    </c:choose>
</tag:workflow-panel>



<c:if test="${test!=null && test.id>0 }">	
	<script language="javascript">
	var treeParameters = null;
	function loadParametersTree()
	{
		treeParameters = new dhtmlXTreeObject("treeboxbox_treeParameters", "100%", "100%", 0);
		treeParameters.setSkin('dhx_skyblue');
		treeParameters.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
		treeParameters.enableDragAndDrop(0);
		treeParameters.enableTreeLines(true);
		treeParameters.enableCheckBoxes(1);
		treeParameters.setXMLAutoLoading("../test/ajax-parameters-search");
	    
	    var d = new Date();
	    var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
	    treeParameters.loadXML("../test/ajax-parameters-search?id=0&tmstp="+str);
	
	}
	function openCopyParametersDialog()
	{
	    loadParametersTree();
	    showPopup("divCopyParameters",400,500);
	}
	function copySelectedParameters()
	{
	    var str = treeParameters.getAllChecked();
	    if(str!=null && str!="")
	    {
	        document.forms.copyParameters.testId.value = <%out.print(((Test)pageContext.findAttribute("test")).getId());%>;
	        document.forms.copyParameters.parameterIds.value = str;
	        document.forms.copyParameters.submit();
	    }
	}
	</script>
	<form name="copyParameters" action="../test/copy-parameters" method="post">
	    <input type="hidden" name="testId" value=""/>
	    <input type="hidden" name="parameterIds" value=""/>
	</form>
	<div id="divCopyParameters" style="display:none;">
	    <tag:panel align="center" title="Copy Parameters" width="400px" height="500px" closeDivName="divCopyParameters">
	        <div id="treeboxbox_treeParameters" style="border:1px solid #cccccc;width:350; height:400; overflow:auto;"></div>
	        <tag:submit value="Copy" onclick="copySelectedParameters();return false;"></tag:submit>
	    </tag:panel>
	</div>
</c:if>