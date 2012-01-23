<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuiteGroup"%>
<%@page import="java.util.List"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmTaskDependency"%>

<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    
    <c:choose>
        <c:when test="${group==null}">
            <img src="../images/workflow-icon-task.png"/> 
            <tag:escape text="${task.name}"/>
        </c:when>
        <c:otherwise>
            <a href="../test-run-manager/edit-task?id=${task.id}">
                <img src="../images/workflow-icon-task.png"/> 
                <tag:escape text="${task.name}"/>
            </a>
            <img src="../images/breadcrump-arrow.png"/>
            <img src="../images/workflow-icon-test-group.png"/> 
            
            <tag:escape text="${group.name}"/>
        </c:otherwise>
    </c:choose>
     
    
</div>
<form method="post">
    <c:if test="${group==null}">
        <tag:submit value="Save" name="Submit"></tag:submit>
    </c:if>
    <c:if test="${group==null}">
		<tag:panel title="Task Details" id="panelTaskInfo" align="center" width="100%" disclosure="true" closed="false">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		        <tbody> 
		            <tr>
		                <td class="small-description">Name:</td>
		            </tr>
		            <tr>  
		                <td>
		                    <tag:edit-field-simple name="taskName" id="editTaskName" width="100%" value="${task.name}"></tag:edit-field-simple>
		                </td>
		            </tr>
		            <tr>
		                <td class="small-description"><br/>Description:</td>
		            </tr>
		            <tr>
		                <td>
		                   <tag:textarea-simple name="taskDescription"  style="width:100%;" rows="6"  value="${task.description}"/>
		                </td>
		            </tr>
		            <tr>
		               <td>
		                   <input id="chkShareTask" type="checkbox" name="shared" <c:if test="${task.shared==true}">checked="checked"</c:if>/>
		                   <label for="chkShareTask"> Share this task with other users</label>
		               </td>
		            </tr>
		        </tbody>
		    </table>
		</tag:panel>
	</c:if>	
	     
	<script>
	var _taskSuites = [
	<%
	List<TrmSuite> suites = (List<TrmSuite>)pageContext.findAttribute("suites");
	if(suites!=null)
	{
	    boolean bSep = false;
	    for(TrmSuite suite : suites)
	    {
	        if(bSep)out.print(",");
	        bSep = true;
	        out.print("{id:"+suite.getId()+", enabled:"+suite.getEnabled().toString()+"}");
	    }
	}
	%>
	];
	
	var _taskSuiteGroups = [
    <%
    List<TrmSuiteGroup> groups = (List<TrmSuiteGroup>)pageContext.findAttribute("groups");
    if(groups!=null)
    {
        boolean bSep = false;
        for(TrmSuiteGroup group : groups)
        {
            if(bSep)out.print(",");
            bSep = true;
            out.print("{id:"+group.getId()+", enabled:"+group.getEnabled().toString()+"}");
        }
    }
    %>
    ];
	
	function checkAllSuites(chk)
	{
		for(var i=0;i<_taskSuites.length;i++)
		{
			var schk = document.getElementById("enableSuite"+_taskSuites[i].id);
			schk.checked = chk.checked;
		}
	}
	function checkAllSuiteGroups(chk)
    {
        for(var i=0;i<_taskSuiteGroups.length;i++)
        {
            var schk = document.getElementById("enableSuiteGroup"+_taskSuiteGroups[i].id);
            schk.checked = chk.checked;
        }
    }
	function onPrepareTaskClicked()
	{
		for(var i=0;i<_taskSuites.length;i++)
		{
			//Checking if the suite settings were changed
			var chk = document.getElementById("enableSuite"+_taskSuites[i].id);
			if(_taskSuites[i].enabled != chk.checked)
			{
				return confirm("You haven't saved your suites settings. Are you sure you wan't to run this task?");
			}
		}
		return true;
	}
	</script>
	
	<div id="reportBody" style="position:relative; display:block;">
	    <c:if test="${fn:length(taskDependencies) > 0 && group == null}">
	        <script language="javascript">
	        var _taskDependencyIds = [
	        <%
	        //Printing depencies ids so later this could be used for checkboxes in table
	        Collection<TrmTaskDependency> dependencies = (Collection<TrmTaskDependency>)pageContext.findAttribute("taskDependencies");
	        boolean comma = false;
	        for(TrmTaskDependency dependency : dependencies){
	            if(comma)out.print(",");
	            comma = true;
	            out.print(dependency.getId());
	        }
	        %>
	        ];

	        function checkAllTaskDependencies(chkAll){
	        	for(var i=0;i<_taskDependencyIds.length;i++) {
	                var chk = document.getElementById("chkTaskDependency"+_taskDependencyIds[i]);
	                chk.checked = chkAll.checked;
	            }
	        }

	        function deleteSelectedTaskDependecies(){
		        var ids = "";
		        var comma = false;
		        for(var i=0; i<_taskDependencyIds.length; i++){
		        	var chk = document.getElementById("chkTaskDependency"+_taskDependencyIds[i]); 
			        if(chk.checked) {
				        if(comma)ids+=",";
				        comma = true;
				        ids+=_taskDependencyIds[i];
			        }
		        }
		        if(comma){
			        document.forms.deleteTaskDependencies.dependencies.value = ids;
			        document.forms.deleteTaskDependencies.submit();
		        }
	        }
	        </script>
	        
	        
            
		    <h2>Task Dependencies: </h2>
		    
		    <table id="taskDependenciesTable">
		        <tr>
		            <th> </th>
		            <th>Task</th>
		            <th>Remove</th>
		        </tr>
	            <tbody>
	            <c:forEach items="${taskDependencies}" var="td">
	            <tr>
	                <td></td>
	                <td><tag:remove-white-space>
	                    <input type="checkbox" id="chkTaskDependency${td.id}"/>
	                    <a href="../test-run-manager/${td.ownerId==user.id ? 'edit-task':'shared-task'}?id=${td.refTaskId}" class="table-body-cell">
	                        <img src="../images/workflow-icon-task.png"/>
	                        <tag:escape text="${td.refTaskName}"/>
	                    </a>
	                </tag:remove-white-space></td>
	                <td width="100px"><tag:remove-white-space>
	                    <a href="../test-run-manager/delete-task-dependencies?taskId=${td.taskId}&dependencies=${td.id}" class="table-body-cell"><img src="../images/workflow-icon-delete.png"/> Remove</a>
	                </tag:remove-white-space></td>
	            </tr>
	            </c:forEach>
	            </tbody>
	        </table>
	        <script>
		    $(document).ready(function(){
		        tableToGrid("#taskDependenciesTable",{
		            height:'auto',
		            width:'auto',
		            hidegrid:true,
		            caption:'Task Dependencies'
		        });
		    });
		    </script>
	        <div style="text-align:right;width:100%;">
	           <a href="javascript:deleteSelectedTaskDependecies();">Delete selected dependencies</a>
	        </div>
	    </c:if>
	    
	    
	    <c:if test="${group==null}">
	       <c:if test="${fn:length(groups) > 0}">
		       <h2>Groups:</h2>
		       <table id="suiteGroupsTable">
		           <tr>
		               <th> </th>
		               <th>Suite group</th>
		               <th>Enabled</th>
		           </tr>
		           <c:forEach items="${groups}" var="g">
		               <tr>
		                   <td></td>
		                   <td><tag:remove-white-space>
		                       <a class="big-link" href="../test-run-manager/edit-task?id=${task.id}&groupId=${g.id}" class="table-body-cell">
		                           <img src="../images/iconTestGroup.png"/>
		                           <tag:escape text="${g.name}"/>
		                       </a>
		                   </tag:remove-white-space></td>
		                   <td width="50px"><tag:remove-white-space>
                               <input type="checkbox" id="enableSuiteGroup${g.id}" name="enableSuiteGroup${g.id}" <c:if test="${g.enabled==true}">checked="checked"</c:if>/>
                           </tag:remove-white-space></td>
		               </tr>
		           </c:forEach>
		       </table>
		       <script>
	            $(document).ready(function(){
	                tableToGrid("#suiteGroupsTable",{
	                    height:'auto',
	                    width:'auto',
	                    hidegrid:true,
	                    caption:'Suite Groups'
	                });
	            });
	            </script>
		   </c:if>
	    </c:if>
	    
	    <c:if test="${group!=null}">
	       <h2><img src="../images/iconTestGroup.png"/> <tag:escape text="${group.name}"></tag:escape> </h2>
	       <tag:nl2br><tag:escape text="${group.description}"/></tag:nl2br>
	    </c:if>
	
	    
	    <input type="hidden" name="taskId" value="${task.id}"/>
	    
	    <c:if test="${fn:length(suites) > 0}">
		    <h2>Suites:</h2>
		    <table id="suitesTable">
		        <tr>
		            <th>Name</th>
		            <th>Enabled</th>
                    <th>Remove</th>
		        </tr>
			    <c:forEach items="${suites}" var="s">
				   <tr>
				       <td><tag:remove-white-space>
				           <a href="../test-run-manager/edit-suite?id=${s.id}">
				              <img src="../images/workflow-icon-suite.png"/>
				              <tag:escape text="${s.name}"/>
				              <br/>
				              <span class="small-description">
			                      <tag:cut-text text="${s.description}" maxSymbols="50" escapeHTML="true"/>
			                   </span>
				           </a>
				       </tag:remove-white-space></td>
				       <td width="100px"><tag:remove-white-space>
                           <input type="checkbox" id="enableSuite${s.id}" name="enableSuite${s.id}" <c:if test="${s.enabled==true}">checked="checked"</c:if>/>
                       </tag:remove-white-space></td>
                       <td width="100px"><tag:remove-white-space>
				          <a href="../test-run-manager/delete-suite?id=${s.id}"
			                                         onclick="return confirm('Are you sure you want to delete the suite?');"><img src="../images/workflow-icon-delete.png"/> Remove</a>
			           </tag:remove-white-space></td>
				   </tr>
				</c:forEach>
			</table>
			<script>
            $(document).ready(function(){
                tableToGrid("#suitesTable",{
                    height:'auto',
                    width:'auto',
                    hidegrid:true,
                    caption:'Suites'
                });
            });
            </script>
	    </c:if>
    </div>
    <br/>
    <tag:submit value="Save" name="Submit"></tag:submit>
</form>

<form name="deleteTaskDependencies" action="../test-run-manager/delete-task-dependencies" method="post">
    <input name="taskId" type="hidden" value="${task.id}"/>
    <input name="dependencies" type="hidden"/>
</form>