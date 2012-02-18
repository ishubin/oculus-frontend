<%@page import="java.io.StringWriter"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="net.mindengine.oculus.frontend.utils.JSONUtils"%>
<%@page import="net.mindengine.oculus.grid.domain.agent.AgentStatus"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmProperty"%>
<%@page import="java.util.Map"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@page import="java.util.List"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../grid/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <img src="../images/breadcrump-arrow.png"/> 
    Prepare Task for Run
</div>     
            
<div class="small-description">
    Customize task parameters and when you are done click the "Run Task"
    
</div>

<script>

<%
AgentStatus[] agents = (AgentStatus[])pageContext.findAttribute("agents");
ObjectMapper mapper = new ObjectMapper();
out.print("var agents = ");
StringWriter writer = new StringWriter();
mapper.writeValue(writer, agents);
out.print(writer.getBuffer());
out.println(";");
%>

function findAgentByName(name) {
	for(var i=0; i<agents.length; i++) {
		if(agents[i].agentInformation.name == name) {
			return agents[i];
		}
	}
	return null;
}
function containsAgentTag(agent, tagName) {
	var tags = agent.agentInformation.tags;
	
	for(var i=0; i<tags.length; i++) {
		if(tags[i].type == "string") {
			if(tags[i].wrappedValue.value.toLowerCase() == tagName.toLowerCase()) {
				return true;
			}
		}
		else if(tags[i].type == "list") {
			for(var j=0; j<tags[i].wrappedValues.length; j++) {
				if(tags[i].wrappedValues[j].value.toLowerCase() == tagName.toLowerCase()) {
					return true;
				}
			}
		}
	}
	return false;
} 


var agentFilter = {
	selectedTags:[],
	agentNames:[],
	
	selectTag: function (tag) {
		if(!this.isTagSelected(tag)) {
			this.selectedTags[this.selectedTags.length] = tag;
			this.updateAgentsLayout();
			this.updateSelectedTagsLayout();
		}
	},
	
	isTagSelected: function (tag) {
		for( var i=0; i<this.selectedTags.length; i++) {
			if(this.selectedTags[i].toLowerCase() == tag.toLowerCase()) {
				return true;
			}
		}
		return false;
	},
	
	removeSelectedTag: function (tag) {
		for( var i=0; i<this.selectedTags.length; i++) {
			if(this.selectedTags[i].toLowerCase() == tag.toLowerCase()) {
				this.selectedTags.splice(i, 1);
			}
		}
		this.updateAgentsLayout();
		this.updateSelectedTagsLayout();
	},
	
	updateAgentsLayout: function() {
		this.agentNames = [];
		
		$(".run-task-agent-layout").each(function(index){
			var agentName = $(this).attr("agent-name");
			var agent = findAgentByName(agentName);
			if(agent!=null) {
				var tags = agentFilter.selectedTags;
				var showAgent = true;
				for (var i=0; i<tags.length; i++) {
					if(!containsAgentTag(agent, tags[i])) {
						showAgent = false;
						break;
					}
				}
				if(showAgent) {
					$(this).show();
					agentFilter.agentNames[agentFilter.agentNames.length] = agentName;  
				}
				else {
					$(this).hide();
				}
			}
		});
	},
	
	updateSelectedTagsLayout: function () {
		$(".agent-selected-tag-layout").each(function(index){
			var tag = $(this).attr("agent-tag");
			if(agentFilter.isTagSelected(tag)) {
				$(this).show();
			}
			else {
				$(this).hide();
			}
		});
	}
};


$(document).ready(function () {
	$('.agent-tag-click-add').click(function (){
		var tag = $(this).attr('agent-tag');
    	agentFilter.selectTag(tag);
    	return false;
    });
	
	$('.agent-tag-click-remove').click(function (){
		var tag = $(this).attr('agent-tag');
    	agentFilter.removeSelectedTag(tag);
    	return false;
    });
});

</script>


<script>
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
	if(agentFilter.agentNames.length>0) {
		var str = "";
		for(var i=0; i<agentFilter.agentNames.length; i++) {
			if(i>0)str+=",";
			str+=agentFilter.agentNames[i];
		}
		
		document.forms.formTasks.selectedAgents.value = str;
		document.forms.formTasks.Submit.value = "Run Task";
		document.forms.formTasks.submit();	
	}
	else alert("There are no agents that match your filter");
	
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
    <input type="hidden" name="selectedAgents" value=""/>
    
    
    <c:forEach items="${tasks}" var="task" varStatus="taskVarStatus">
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
       <br/>
    </c:forEach>
    
    <h2>Agents:</h2>
    
	<c:forEach items="${agents}" var="agent">
		<div class="run-task-agent-layout" agent-name="${agent.agentInformation.name}">
	    	<tag:agent-layout agent="${agent}"/>
	    </div>
	</c:forEach>
</form>
