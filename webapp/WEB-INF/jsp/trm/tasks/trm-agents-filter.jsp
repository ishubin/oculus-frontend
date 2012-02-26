<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@page import="java.io.StringWriter"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="net.mindengine.oculus.frontend.utils.JSONUtils"%>
<%@page import="net.mindengine.oculus.grid.domain.agent.AgentStatus"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmProperty"%>
<%@page import="java.util.Map"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmTask"%>
<%@page import="java.util.List"%>


<script>
<%
AgentStatus[] agents = (AgentStatus[])pageContext.findAttribute("agents");
ObjectMapper mapper = new ObjectMapper();
out.print("var agents = ");
StringWriter writer = new StringWriter();
mapper.writeValue(writer, agents);
out.print(writer.getBuffer());
out.println(";");

TrmTask task = (TrmTask) pageContext.findAttribute("task");

out.print("var taskAgentFilter = ");
if(task!=null) {
    out.print(task.getAgentsFilter());
}
else {
    out.print("null");
}
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
	strictAgentNames:[],
	agentNames:[],
	
	exportFilter: function() {
		var filter = null;
		if(this.strictAgentNames!=null && this.strictAgentNames.length > 0) {
			filter = {
			    strictAgentNames:this.strictAgentNames
			};
		}
		else if(this.selectedTags.length > 0) {
			filter = {
			    selectedTags:this.selectedTags
			};
		}
		return JSON.stringify(filter);
	},
	
	findStrictAgent: function (name) {
		for(var i=0; i<this.strictAgentNames.length; i++) {
			if(this.strictAgentNames[i] == name) {
				return i;
			}
		}	
		return -1;
	},
	
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
			if(this.strictAgentNames.length > 0) {
				for(var i=0; i<this.strictAgentNames.length; i++) {
					if(this.strictAgentNames[i] == name){
						return true;
					}
				}
				return false;
			}
			else {
				var tags = this.selectedTags;
				for (var i=0; i<tags.length; i++) {
					if(!containsAgentTag(agent, tags[i])) {
						return false;
					}
				}	
			}	
		}
		return true;
	},
	
	updateAgentsLayout: function() {
		this.agentNames = [];
		var sortFunction = function(a,b){
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
		};
		
		$(".run-task-agent-layout").sortElements(sortFunction);
		
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
		
		$(".agent-click").each(function (){
			var agentName = $(this).attr("agent-name");
			if(agentFilter.findStrictAgent(agentName)>=0) {
				$(this).removeClass("agent-tag").addClass("agent-tag-selected");	
			}
			else {
				$(this).removeClass("agent-tag-selected").addClass("agent-tag");
			}
		});
		
		$("#selectedAgentsList li").each(function (){
			var agentName = $(this).attr("agent-name");
			if(agentFilter.isAgentIncluded(agentName)) {
				$(this).removeClass("agent-ignore");
			}
			else {
				$(this).addClass("agent-ignore");
			}
		});
		
		$("#selectedAgentsList li").sortElements(sortFunction);
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
		//reset strict agent filter
		agentFilter.strictAgentNames = [];
		$('.agent-click').each(function (){
			$(this).removeClass("agent-tag-selected").addClass("agent-tag");
		});
		
		var tag = $(this).attr('agent-tag');
		if(agentFilter.isTagSelected(tag)){
			agentFilter.removeSelectedTag(tag);
		}
		else {
			agentFilter.selectTag(tag);
		}
    	return false;
    });
	
	$('.agent-click').click(function (){
		var agentName = $(this).attr('agent-name');
		var id = agentFilter.findStrictAgent(agentName);
		if(id>=0) {
			agentFilter.strictAgentNames.splice(id, 1);
		}
		else {
			agentFilter.strictAgentNames.push(agentName);
		}
		
		agentFilter.selectedTags = [];
		
		agentFilter.updateAgentsLayout();
		agentFilter.updateTagsLayout();
    	return false;
    });
	
	if(taskAgentFilter!=null) {
		if(taskAgentFilter.strictAgentNames!=null && taskAgentFilter.strictAgentNames.length>0) {
			agentFilter.strictAgentNames = taskAgentFilter.strictAgentNames;
		}
		else if (taskAgentFilter.selectedTags!=null && taskAgentFilter.selectedTags.length>0) {
			agentFilter.selectedTags = taskAgentFilter.selectedTags;
		}
	}
	
	agentFilter.updateAgentsLayout();
	agentFilter.updateTagsLayout();
});

function submitRunTask() {
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
