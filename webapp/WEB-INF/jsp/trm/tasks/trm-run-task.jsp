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

<tag:pickbuild-setup/>

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
			this.updateTagsLayout();
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
		this.updateTagsLayout();
	},
	
	isAgentIncluded: function (name) {
		var agent = findAgentByName(name);
		if(agent!=null) {
			var tags = this.selectedTags;
			for (var i=0; i<tags.length; i++) {
				if(!containsAgentTag(agent, tags[i])) {
					return false;
				}
			}	
		}
		return true;
	},
	
	updateAgentsLayout: function() {
		this.agentNames = [];
		$(".run-task-agent-layout").sortElements(function(a,b){
			var nameA = $(a).attr("agent-name");
			var nameB = $(b).attr("agent-name");
			
			var isIncludedA = agentFilter.isAgentIncluded(nameA);
			var isIncludedB = agentFilter.isAgentIncluded(nameB);
			
			if(isIncludedA == isIncludedB) {
				return nameA > nameB ? 1 : -1;	
			}
			else {
				if(isIncludedB) {
					return 1;
				}
				else return -1;
			}
		});
		
		$(".run-task-agent-layout").each(function(index){
			var agentName = $(this).attr("agent-name");
			if(agentFilter.isAgentIncluded(agentName)) {
				$(this).fadeTo("fast", 1.0);
				agentFilter.agentNames[agentFilter.agentNames.length] = agentName;  
			}
			else {
				$(this).fadeTo("fast", 0.2);
			}
		});
		
		if(this.agentNames.length==0) {
			$("#selectedAgentsLayout_No_Agents").show();
			$("#selectedAgentsList li").remove();
		}
		else {
			$("#selectedAgentsLayout_No_Agents").hide();
			$("#selectedAgentsList li").remove();
			for(var i=0; i<this.agentNames.length; i++) {
				$("#selectedAgentsList").append("<li>" + escapeHTML(this.agentNames[i]) + "</li>");	
			}
		}
	},
	
	updateTagsLayout: function () {
		$(".agent-tag-click").each(function(index) {
			var tag = $(this).attr("agent-tag");
			if(agentFilter.isTagSelected(tag)) {
				$(this).removeClass("agent-tag").addClass("agent-tag-selected");
			}
			else {
				$(this).removeClass("agent-tag-selected").addClass("agent-tag");
			}
		});
	}
};


$(document).ready(function () {
	$('.agent-tag-click').click(function (){
		var tag = $(this).attr('agent-tag');
		if(agentFilter.isTagSelected(tag)){
			agentFilter.removeSelectedTag(tag);
		}
		else {
			agentFilter.selectTag(tag);
		}
    	return false;
    });
	
	agentFilter.updateAgentsLayout();
	agentFilter.updateTagsLayout();
});

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
                                       <tag:pickbuild-button build="Current Version" id="task_${task.id}_build" projectId="${task.projectId}"></tag:pickbuild-button>
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
