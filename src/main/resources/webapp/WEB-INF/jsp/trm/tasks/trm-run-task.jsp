<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmProperty"%>
<%@page import="java.util.Map"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@page import="java.util.List"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../test-run-manager/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <img src="../images/breadcrump-arrow.png"/> 
    Prepare Task for Run
</div>     
            
<div class="small-description">
    Customize task parameters and when you are done click the "Run Task"
    
</div>



<script language="javascript">
var pickBuild_taskId = null;
var pickBuild_projectId = null;
function onPickBuildClick(taskId, projectId)
{
    pickBuild_taskId = taskId;
    pickBuild_projectId = projectId;

    updateBuilds(1, projectId,"");
}
function updateBuilds(page, projectId, name)
{
    var divBuilds = document.getElementById("divPickBuildBuilds");
    var divNavigation = document.getElementById("divPickBuildNavigation");
    var divLoading = document.getElementById("divPickBuildLoadingIcon");
    divBuilds.innerHTML = "";
    divNavigation.innerHTML = "";
    divLoading.style.display = "block";
    showPopup("divPickBuild",400,400);
    
    dhtmlxAjax.post("../project/ajax-build-fetch", "projectId="+projectId+"&page="+page+"&name="+escape(name), onAjaxBuildFetchResponse);
}
function onAjaxBuildFetchResponse(loader)
{
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result != "error")
    {
        var namePattern = document.getElementById("textPickBuildNamePattern").value;
        var result = obj.object;
        var pages = Math.round(result.numberOfResults/result.limit);

        
        //Rendering the navigation panel
        var divLoading = document.getElementById("divPickBuildLoadingIcon");
        divLoading.style.display = "none";
        var divNavigation = document.getElementById("divPickBuildNavigation");
        var html = "";

        html+="<div class=\"small-description\">Found Results: "+result.numberOfResults+"</div>";
        html+="<table border=\"0\">";
        html+="<tr>";
        html+="<td width=\"15px\">";
        
        if(result.page>1)
        {
            var prevPage = result.page-1;
            html+="<a href=\"javascript:updateBuilds("+prevPage+","+pickBuild_projectId+",'"+namePattern+"');\">&lt;</a>";
        }

        html+="</td>";
        html+="<td>";
        html+=result.page;
        html+="</td>";
        html+="<td width=\"15px\">";
        if(result.page<pages)
        {
            var nextPage = result.page+1;
            html+="<a href=\"javascript:updateBuilds("+nextPage+","+pickBuild_projectId+",'"+namePattern+"');\">&gt;</a>";
        }
        html+="</td>";
        html+="</tr>";
        html+="</table>";
        divNavigation.innerHTML = html;

        var builds = result.results;
        html="";
        html+="<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" style=\"border:1px solid #909090;\">";
        var color = "";
        html+="<tr><td style=\"width:100%;background:#e0e0e0;\">";
        html+="<a class=\"disclosure\" href=\"javascript:onPickBuildPicked('Current Version');\">Current Version</a>";
        html+="</td></tr>";
        for(var i=0;i<builds.length;i++)
        {
            if(i%2==0)
            {
                color="#d0d0d0";
            }
            else color="#a0a0a0";
            html+="<tr><td style=\"width:100%;background:"+color+";\">";
            html+="<a class=\"disclosure\" href=\"javascript:onPickBuildPicked('"+builds[i].name+"');\">"+builds[i].name+"</a>";
            html+="</td></tr>";
        }
        html+="</table>";
        var divBuilds = document.getElementById("divPickBuildBuilds");
        divBuilds.innerHTML = html;
    }
    else alert(str);
}
function onPickBuildPicked(name)
{
    var buildLink = document.getElementById("linkPickBuild_task_"+pickBuild_taskId);
    buildLink.innerHTML = name;
    document.forms.formTasks.elements["task_"+pickBuild_taskId+"_build"].value = name;
    closePopup("divPickBuild");
}
var pickBuild_timer = null;
function onPickBuildNameChange(control)
{
    if(pickBuild_timer!=null)
    {
        clearTimeout(pickBuild_timer);
    }
    updateBuilds(1, pickBuild_projectId, control.value);
}
function onChangeCheckboxChooseAgents(checkbox,agentsCount, taskId)
{
    var divAgents = document.getElementById("divTask_"+taskId+"_agents");
    if(checkbox.checked)
    {
        divAgents.className="element-high-opacity";
    }
    else divAgents.className="element-low-opacity";

    for(var i=0;i<agentsCount;i++)
    {
        var chk = document.getElementById("task_"+taskId+"_agent_chk_"+i);
        chk.disabled = !checkbox.checked;
    }
}
function onUseSchedulerClick(checkbox)
{
	var div = document.getElementById("divScheduler");
	var link = document.getElementById("workflowRunTaskLink");
	if(checkbox.checked)
	{
		div.style.display = "block";
		link.innerHTML="<img class=\"workflow-icon\" src=\"../images/workflow-icon-schedule.png\"/> Send To Scheduler";
	}
	else
	{
		div.style.display = "none";
		link.innerHTML="<img class=\"workflow-icon\" src=\"../images/workflow-icon-run.png\"/> Run Task On Server ";
	}
	 
}

function submitRunTask(){
	document.forms.formTasks.Submit.value = "Run Task";
	document.forms.formTasks.submit();
}
function submitExportTask(){
	document.forms.formTasks.Submit.value = "Export Task";
	document.forms.formTasks.submit();
}
</script>


<div id="divPickBuild" class="popup" style="position:absolute;display:none;width:400px;height:500px;">
    <tag:panel title="Pick build" 
                align="center"
                closeDivName="divPickBuild" 
                width="400px" height="400px">
        <input id="textPickBuildNamePattern" type="text" onchange="onPickBuildNameChange(this);"/>
        <div id="divPickBuildNavigation" style="width:360px;height:30px;">
        </div>
        <div style="overflow:auto;width:360px;height:330px;">
            <div id="divPickBuildLoadingIcon" style="display:none;">
                <img src="../images/loading.gif"/>
            </div>
            <div id="divPickBuildBuilds">
            </div>
        </div>
    </tag:panel>
</div>
<form method="post" name="formTasks">
    <input type="hidden" name="Submit" value="Run Task"/>
    <input id="useScheduler" type="checkbox" name="useScheduler" onchange="onUseSchedulerClick(this);"/>
    <label for="useScheduler">Use Scheduler</label>
    <br/><br/>
	<div id="divScheduler" align="center" style="display:none;margin:4px" class="small-description">
	    <tag:panel-border title="Scheduler" align="center" width="670px" height="400px">
		    <div align="center">
			    <script language="javascript">
			    
			    
			    var lastOccurrenceDivName = "divWeeklyOccurrence";
			    function showWeeklyOccurrenceMatrixDisplay(text)
			    {
			        var div = document.getElementById("divWeeklyOccurrenceMatrix_display");
			        div.innerHTML = text;
			    }
			    function changeWeeklyOccurrenceMatrixCell(week,hour)
			    {
			        var div = document.getElementById("divWeeklyOccurrenceMatrix_cell_"+week+"_"+hour);
			        var input = document.getElementById("weeklyOccurrenceMatrix_cell_"+week+"_"+hour);
			        if(input.value==0)
			        {
			            div.className = "weekly-occurrence-matrix-selected";
			            input.value = 1;
			        }
			        else
			        {
			            div.className = "weekly-occurrence-matrix";
			            input.value = 0;
			        }
			    }
			    function onChangeOccurrenceType(type)
			    {
			        var div = document.getElementById(lastOccurrenceDivName);
			        div.style.display = "none";
			        if(type==0)
			        {
			            lastOccurrenceDivName = "divWeeklyOccurrence";
			        }
			        else if(type==1)
			        {
			            lastOccurrenceDivName = "divDateOccurrence";
			        }
			        div = document.getElementById(lastOccurrenceDivName);
			        div.style.display = "block";
			    }
			    function onDateChanged(date)
			    {
			    }
			    
			    </script>
			    
			    
			    Choose Schedule Occurrence: 
			    
			    <select class="small-select" name="occurrenceType" id="occurrenceType" onchange="onChangeOccurrenceType(this.selectedIndex);">
			        <option selected="selected" value="0">Weekly Occurrence</option>
			        <option value="1">Date</option>
			    </select>
			    
			    <div id="divWeeklyOccurrence" style="display:block;">
		           <tag:panel align="center" title="Weekly Occurrence">
		                <div class="small-description">
		                    Description block 
		                </div>
		                <br/>
		                <table border="0" class="weekly-occurrence-matrix" cellpadding="0" cellspacing="0">
		                    <tr>
		                      <td></td>
		                      <c:forEach items="${dayHours}" var="hour">
			                      <td class="small-description" style="overflow: hidden;">
			                          ${hour}
			                      </td>
		                      </c:forEach>
		                    </tr>
		                    <c:forEach items="${weekDays}" var="day" varStatus="weekVarStatus">
		                        <tr>
		                            <td class="small-description">${day}</td>
		                            <c:forEach items="${dayHours}" var="hour" varStatus="hourVarStatus">
		                               <td class="weekly-occurrence-matrix">
		                                   <input type="hidden" id="weeklyOccurrenceMatrix_cell_${weekVarStatus.index}_${hourVarStatus.index}" name="weeklyOccurrenceMatrix_cell_${weekVarStatus.index}_${hourVarStatus.index}" value="0"/>
		                                   <div class="weekly-occurrence-matrix" 
		                                       id="divWeeklyOccurrenceMatrix_cell_${weekVarStatus.index}_${hourVarStatus.index}"
		                                       onmouseover="showWeeklyOccurrenceMatrixDisplay('${day}   ${hour}:00');"
		                                       onmouseout="showWeeklyOccurrenceMatrixDisplay('');"
		                                       onclick="changeWeeklyOccurrenceMatrixCell(${weekVarStatus.index},${hourVarStatus.index});"
		                                       >
		                                       <i> </i>
		                                   </div>
		                               </td>
		                            </c:forEach>
		                        </tr>
		                    </c:forEach>
		                </table>
		                <div class="small-description" id="divWeeklyOccurrenceMatrix_display" style="height:30px;">
		                   
		                </div>
		            </tag:panel>
			    </div>
			    
			    <div id="divDateOccurrence" style="display:none;">
		           <tag:panel align="center" width="300px" title="Date">
		                <div class="small-description">
		                    Description block
		                    
		                    <br/>
		                    Date: 
		                </div>
		                
		                <table border="0">
		                    <tr>
		                        <td>
		                            <tag:edit-field-simple width="200px" name="dateOccurrence_date" id="dateOccurrence_date"></tag:edit-field-simple>
		                        </td>
		                        <td>
		                            <a href="javascript:NewCal('dateOccurrence_date','yyyymmdd',true,24,onDateChanged);"><img src="../images/calendar.gif"/></a>
		                        </td>
		                    </tr>
		                </table>
		            </tag:panel>
			    </div>
		    </div>
	    </tag:panel-border>
	</div>
    
    <input type="hidden" id="agentsCount" name="agentsCount" value="${agentsCount}"/>
    <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100px">
        <c:forEach items="${tasks}" var="task" varStatus="taskVarStatus">
            <tr>
                <td width="50%">
                    <tag:panel align="left" title="${task.name}" width="100%" height="100%" logo="../images/workflow-icon-task.png">
                        <table border="0" width="100%" cellpadding="0px" cellspacing="0px">
                            <tr>
                                <td valign="top" align="left">
                                    <div class="small-description">
                                        ${task.description}
                                    </div>
                                    <br/>
                                    <tag:panel-border title="Parameters" align="left" width="100%">
                                        <table border="0" cellpadding="5px" cellspacing="0px">
                                            <tr>
                                                <td class="small-description"><img src="../images/workflow-icon-settings.png"/> Build:</td>
                                                <td>
                                                    <input type="hidden" name="task_${task.id}_build" id="task_${task.id}_build_control" value="Current Version"/>
                                                    <div style="width:134px;height:19px;margin-bottom:5px;">
                                                      <a class="pick-button" id="linkPickBuild_task_${task.id}" href="javascript:onPickBuildClick('${task.id}',${task.projectId});">Current Version</a>
                                                    </div> 
                                                    
                                                </td>
                                            </tr>
                                            <c:forEach items="${task.parameters}" var="parameter">
                                            <tr>
                                                <td class="small-description"><img src="../images/workflow-icon-settings.png"/> ${parameter.name}:</td>
                                                <td>
                                                <c:choose>
                                                    <c:when test="${parameter.subtype == 'text'}">
                                                        <tag:edit-field-simple name="task_${task.id}_parameter_${parameter.id}" id="task_${task.id}_parameter_${parameter.id}"/>
                                                    </c:when>
                                                    <c:when test="${parameter.subtype == 'list'}">
                                                        <select name="task_${task.id}_parameter_${parameter.id}" id="task_${task.id}_parameter_${parameter.id}" style="width:100%;">
                                                            <c:forEach items="${parameter.valuesAsList}" var="possibleValue">
                                                                <option value="${possibleValue}">${possibleValue}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:when test="${parameter.subtype == 'checkbox'}">
                                                        <input type="checkbox" name="task_${task.id}_parameter_${parameter.id}" id="task_${task.id}_parameter_${parameter.id}"/>
                                                    </c:when>
                                                    <c:otherwise>Undefined Control</c:otherwise>
                                                </c:choose>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                        </table>
                                    </tag:panel-border>
                                    <br/>
                                </td>
                            </tr>
                        </table>
                    </tag:panel>
                </td>
                <td valign="middle" width="50px">
                    <img src="../images/arrow-blue.png"/>
                </td>
                <td width="50%">
                    <tag:panel align="left" title="Available Agents" width="100%" height="100%" id="availableAganetsFor_${task.id}">
                        <c:choose>
                            <c:when test="${agentsCount>0}">
                                <input type="checkbox"  onchange="onChangeCheckboxChooseAgents(this,${agentsCount},'${task.id}');" name="task_${task.id}_choose_agents" id="task_${task.id}_choose_agents"/>
                                <label for="task_${task.id}_choose_agents">Choose Agents</label>
                                <div id="divTask_${task.id}_agents" class="element-low-opacity">
                                    <tag:table columns=" ,Name,Status" width="100%">
                                        <c:forEach items="${agents}" var="agent" varStatus="agentVarStatus">
                                            <tag:table-row>
                                                <tag:table-cell width="40px">
                                                    <input type="checkbox" id="task_${task.id}_agent_chk_${agentVarStatus.index}" name="task_${task.id}_agent_chk_${agentVarStatus.index}" checked="checked" disabled="disabled"/>
                                                    <input type="hidden" name="task_${task.id}_agent_name_${agentVarStatus.index}" value="${agent.agentInformation.name}"/>
                                                </tag:table-cell>
                                                <tag:table-cell>
                                                    ${agent.agentInformation.name}
                                                </tag:table-cell>
                                                <tag:table-cell width="70px">
                                                    <c:choose>
                                                        <c:when test="${agent.state==1}">
                                                            <div style="color:#009900;">Free</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div style="color:#990000;">Busy</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tag:table-cell>
                                            </tag:table-row>
                                        </c:forEach>
                                    </tag:table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="small-description">
                                    No agents available
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </tag:panel>
                </td>
            </tr>
            <tr>
                <td colspan="3" height="30px">
                    <p></p>
                </td>
            </tr>
        </c:forEach>
    </table>
</form>