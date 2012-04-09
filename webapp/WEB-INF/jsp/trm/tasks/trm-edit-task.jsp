<%@page import="java.util.List"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<%@ include file="/WEB-INF/jsp/trm/tasks/trm-agents-filter.jsp"%>

<tag:pickbuild-setup/>

<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmTaskDependency"%>


<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    
    <img src="../images/workflow-icon-task.png"/> 
    <tag:escape text="${task.name}"/>
</div>

<script>
function onTaskSaveFormSubmit() {
	$("#agentsFilterField").val(agentFilter.exportFilter());
	return true;
}
</script>
<form method="post" onsubmit="return onTaskSaveFormSubmit();">
	<input type="hidden" name="agentsFilter" id="agentsFilterField" value=""/>
    <tag:submit value="Save" name="Submit"></tag:submit>
    <tag:panel title="Task Details" id="panelTaskInfo" align="center" width="100%" disclosure="true" closed="false">
		    <p>
		    	Name:
		    	<br/>
		    	<tag:edit-field-simple name="taskName" id="editTaskName" width="100%" value="${task.name}"></tag:edit-field-simple>
		    </p>
		    <p>
		    	Description:
		    	<br/>
		    	<tag:textarea-simple name="taskDescription"  style="width:100%;" rows="6"  value="${task.description}"/>
		    </p>
		    <p>
                <input id="chkShareTask" type="checkbox" name="shared" <c:if test="${task.shared==true}">checked="checked"</c:if>/> 
                <label for="chkShareTask"> Share this task with other users</label>
		    </p>
		    
        		<table border="0" cellpadding="5px" cellspacing="0px">
                     <tr>
                         <td><img src="../images/workflow-icon-settings.png"/> Build:</td>
                         <td>
                             <tag:pickbuild-button build="${task.build}" id="build" projectId="${task.projectId}"></tag:pickbuild-button>
                         </td>
                     </tr>
                     <c:forEach items="${task.parameters}" var="parameter">
                     <tr>
                         <td><img src="../images/workflow-icon-settings.png"/> ${fn:escapeXml(parameter.name)}:</td>
                         <td>
                         <c:choose>
                             <c:when test="${parameter.subtype == 'text'}">
                                 <tag:edit-field-simple name="sp_${parameter.id}" id="sp_${parameter.id}" value="${parameter.taskValue}"/>
                             </c:when>
                             <c:when test="${parameter.subtype == 'list'}">
                                 <select name="sp_${parameter.id}" id="sp_${parameter.id}" style="width:100%;">
                                     <c:forEach items="${parameter.possibleValuesList}" var="possibleValue">
                                         <option value="${possibleValue}" <c:if test="${parameter.taskValue == possibleValue }">selected="selected"</c:if> >${possibleValue}</option>
                                     </c:forEach>
                                 </select>
                             </c:when>
                             <c:when test="${parameter.subtype == 'boolean'}">
                                 <input type="checkbox" name="sp_${parameter.id}" id="sp_${parameter.id}" <c:if test="${parameter.taskValue=='true' }">checked="checked"</c:if> />
                             </c:when>
                             <c:otherwise>Undefined Control</c:otherwise>
                         </c:choose>
                         </td>
                     </tr>
                     </c:forEach>
                 </table>
		    
	</tag:panel>
	     
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
	
	function checkAllSuites(chk)
	{
		for(var i=0;i<_taskSuites.length;i++)
		{
			var schk = document.getElementById("enableSuite"+_taskSuites[i].id);
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
	
	<div id="reportBody">
	    <c:if test="${fn:length(taskDependencies) > 0 && group == null}">
	        <script>
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
	        
	        
            
		    <h2>Included Tasks: </h2>
		    
		    <table id="taskDependenciesTable">
		        <tr>
		            <th> </th>
		            <th>Task</th>
		            <th>-</th>
		        </tr>
	            <tbody>
	            <c:forEach items="${taskDependencies}" var="td">
	            <tr>
	                <td></td>
	                <td><tag:remove-white-space>
	                    <input type="checkbox" id="chkTaskDependency${td.id}"/>
	                    <a href="../grid/${td.ownerId==user.id ? 'edit-task':'shared-task'}?id=${td.refTaskId}" class="big-link">
	                        <img src="../images/workflow-icon-task.png"/>
	                        <tag:escape text="${td.refTaskName}"/>
	                    </a>
	                </tag:remove-white-space></td>
	                <td width="100px"><tag:remove-white-space>
	                    <a class="button" href="../grid/delete-task-dependencies?taskId=${td.taskId}&dependencies=${td.id}" class="table-body-cell"><img src="../images/workflow-icon-delete.png"/> Remove</a>
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
		    <a class="button" href="javascript:deleteSelectedTaskDependecies();"><img src="../images/workflow-icon-delete.png"/> Delete selected tasks</a>
	        
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
		                       <a class="big-link" href="../grid/edit-task?id=${task.id}&groupId=${g.id}" class="table-body-cell">
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
                    <th>-</th>
		        </tr>
			    <c:forEach items="${suites}" var="s">
				   <tr>
				       <td><tag:remove-white-space>
				           <a class="big-link" href="../grid/edit-suite?id=${s.id}">
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
				          <a class="button" href="../grid/delete-suite?id=${s.id}"
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
    
    <c:if test="${agents!=null}">
    	<p>
    	<h2>Agents</h2>
    	<c:forEach items="${agents}" var="agent">
			<div class="run-task-agent-layout" agent-name="${agent.agentInformation.name}">
		    	<tag:agent-layout agent="${agent}"/>
		    </div>
		</c:forEach>
		</p>
    </c:if>
</form>

<form name="deleteTaskDependencies" action="../grid/delete-task-dependencies" method="post">
    <input name="taskId" type="hidden" value="${task.id}"/>
    <input name="dependencies" type="hidden"/>
</form>