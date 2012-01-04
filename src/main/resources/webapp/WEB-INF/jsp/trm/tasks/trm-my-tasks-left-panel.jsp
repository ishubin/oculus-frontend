<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmTask"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="left-panel">
    <b>Work-flow:</b><br/><br/>
    <table border="0" width="100%" cellspacing="5px">
            <tr>
                <td>
                    <tag:workflow-panel title="My Tasks" width="100%" id="workflow_MyTasks">
                        <tag:workflow-element icon="task" link="../test-run-manager/create-task">New Task</tag:workflow-element>
                        <tag:workflow-element icon="monitor" link="../test-run-manager/my-active-tasks">My Active Tasks</tag:workflow-element>
                        <tag:workflow-element icon="save" link="../test-run-manager/my-tasks">My Saved Tasks</tag:workflow-element>
                        <tag:workflow-element icon="schedule" link="../test-run-manager/scheduler">Scheduler</tag:workflow-element>
                    </tag:workflow-panel>
                </td>
            </tr>
            <c:if test="${task!=null && task.id>0}">
            <tr>
                <td>
                    <tag:workflow-panel title="Task Actions" width="100%" id="workflow_TaskActions">
                        <tag:workflow-element icon="prepare-run" link="../test-run-manager/run-task?taskId=${task.id}" onclick="return onPrepareTaskClicked();" title="Customize suite settings for running on server">Prepare task</tag:workflow-element>
                        <tag:workflow-element link="" onclick="openTaskDependenciesDialog();return false;">Add Dependencies</tag:workflow-element>
                        <tag:workflow-element icon="delete" link="../test-run-manager/delete-task?taskId=${task.id}" onclick="if(confirm('Are you sure you want to delete this task'))return true; else return false;">Delete</tag:workflow-element>
                    </tag:workflow-panel>
                    
                    <tag:workflow-panel title="Suite Groups" width="100%" id="workflow_SuiteGroupActions">
                        <c:if test="${group==null }">
                            <tag:workflow-element icon="create" link="../test-run-manager/create-suite-group?taskId=${task.id}" >Create Suite Group</tag:workflow-element>
                        </c:if>
                        <c:if test="${group!=null }">
                            <tag:workflow-element icon="edit" link="../test-run-manager/edit-suite-group?groupId=${group.id}" >Edit Suite Group</tag:workflow-element>
                            <tag:workflow-element icon="delete" link="../test-run-manager/delete-suite-group?groupId=${group.id}" onclick="if(confirm('Are you sure you want to delete this group'))return true; else return false;">Delete Suite Group</tag:workflow-element>
                        </c:if>
                    </tag:workflow-panel>
                    
                    <tag:workflow-panel title="Suite Actions" width="100%" id="workflow_SuiteActions">
                        <c:choose>
                            <c:when test="${group!=null }">
                                <tag:workflow-element icon="suite" link="../test-run-manager/create-suite?taskId=${task.id}&groupId=${group.id}" >Create Suite</tag:workflow-element>
                            </c:when>
                            <c:otherwise>
                                <tag:workflow-element icon="suite" link="../test-run-manager/create-suite?taskId=${task.id}" >Create Suite</tag:workflow-element>
                            </c:otherwise>
                        </c:choose>
                        
                        <tag:workflow-element icon="subcreate" link="" onclick="openCopySuitesDialog();return false;">Copy Suites</tag:workflow-element>
                    </tag:workflow-panel>
                    
                    <script language="javascript">
                    var tree = null;
                    function loadFolderTree()
                    {
                        tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
                        tree.setSkin('dhx_skyblue');
                        tree.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
                        tree.enableDragAndDrop(0);
                        tree.enableTreeLines(true);
                        tree.enableCheckBoxes(1);
                        tree.setXMLAutoLoading("../test-run-manager/ajax-suite-search");
                        
                        var d = new Date();
                        var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
                        tree.loadXML("../test-run-manager/ajax-suite-search?id=mytasks0&tmstp="+str);

                        tree.attachEvent("onSelect", onTreeElementSelect);
                    }
                    function onTreeElementSelect()
                    {
                        var selectId = tree.getSelectedItemId();
                        
                    }
                    function openCopySuitesDialog()
                    {
                        loadFolderTree();
                        showPopup("divCopySuites",400,500);
                    }
                    function copySelectedSuites()
                    {
                    	var str = tree.getAllChecked();
                    	if(str!=null && str!="")
                    	{
                    		document.forms.copySuites.taskId.value = <%out.print(((TrmTask)pageContext.findAttribute("task")).getId());%>;
                    		document.forms.copySuites.suiteIds.value = str;
                    		document.forms.copySuites.submit();
                    	}
                    }

                    var treeTaskDependencies = null;
                    function loadTaskDependenciesTree()
                    {
                    	treeTaskDependencies = new dhtmlXTreeObject("treebox_taskDependency", "100%", "100%", 0);
                    	treeTaskDependencies.setSkin('dhx_skyblue');
                    	treeTaskDependencies.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
                    	treeTaskDependencies.enableDragAndDrop(0);
                    	treeTaskDependencies.enableTreeLines(true);
                    	treeTaskDependencies.enableCheckBoxes(1);
                    	treeTaskDependencies.setXMLAutoLoading("../test-run-manager/ajax-task-search");
                        
                        var d = new Date();
                        var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
                        treeTaskDependencies.loadXML("../test-run-manager/ajax-task-search?id=0&tmstp="+str);
                    }
                    
                    function openTaskDependenciesDialog()
                    {
                    	loadTaskDependenciesTree();
                        showPopup("divTaskDependency",400,500);
                    }
                    function addTaskDependencies()
                    {
                        var str = treeTaskDependencies.getAllChecked();
                        if(str!=null && str!="")
                        {   
                            document.forms.addDependencies.taskId.value = <%out.print(((TrmTask)pageContext.findAttribute("task")).getId());%>;
                            document.forms.addDependencies.refTaskIds.value = str;
                            document.forms.addDependencies.submit();
                        }
                    }
                    
                    </script>
                    <form name="copySuites" action="../test-run-manager/copy-suites" method="post">
                        <input type="hidden" name="taskId" value=""/>
                        <c:choose>
                            <c:when test="${group!=null }">
                                <input type="hidden" name="groupId" value="${group.id }"/>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="groupId" value="0"/>
                            </c:otherwise>
                        </c:choose>
                        
                        <input type="hidden" name="suiteIds" value=""/>
                    </form>
                    
                    
                    <form name="addDependencies" action="../test-run-manager/add-task-dependencies" method="post">
                        <input type="hidden" name="taskId" value=""/>
                        <input type="hidden" name="refTaskIds" value=""/>
                    </form>
                    
                    <div id="divCopySuites" style="display:none;">
                        <tag:panel align="center" title="Copy Suites" width="400px" height="500px" closeDivName="divCopySuites">
                            <div id="treeboxbox_tree" style="border:1px solid #cccccc;width:350; height:400; overflow:auto;"></div>
                            <tag:submit value="Copy" onclick="copySelectedSuites();return false;"></tag:submit>
                        </tag:panel>
                    </div>
                    
                    <div id="divTaskDependency" style="display:none;">
                        <tag:panel align="center" title="Copy Suites" width="400px" height="500px" closeDivName="divTaskDependency">
                            <div id="treebox_taskDependency" style="border:1px solid #cccccc;width:350; height:400; overflow:auto;"></div>
                            <tag:submit value="Add" onclick="addTaskDependencies();return false;"></tag:submit>
                        </tag:panel>
                    </div>
                </td>
            </tr>
            </c:if>  
    </table>
    
</div>