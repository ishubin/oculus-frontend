<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="java.util.LinkedList"%>
<%@page import="net.mindengine.oculus.frontend.domain.test.TestParameter"%>
<%@page import="java.util.List"%>
<%@page import="net.mindengine.oculus.frontend.domain.test.Test"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>
<%
Test test = (Test) pageContext.findAttribute("test");

List<TestParameter> inputParameters = test.getInputParameters();
List<TestParameter> outputParameters = test.getOutputParameters();
if ( inputParameters == null ) {
	inputParameters = new LinkedList<TestParameter>();
}
if ( outputParameters == null ) {
	outputParameters = new LinkedList<TestParameter>();
}

ObjectMapper mapper = new ObjectMapper();
%>

<tag:pickuser-setup></tag:pickuser-setup>
<script>

function onSubmitTest() {
    $("#testContentField").val(testCaseEditor.exportContent());
    
    
    for ( var i=0;i<TestParameters.input.length; i++ ) {
    	var p = TestParameters.input[i];
    	if ( p.possibleValuesList != null ) {
    		var possibleValues = "";
    		for( var j=0; j<p.possibleValuesList.length; j++ ) {
    			possibleValues += "<value>" + escapeHTML(p.possibleValuesList[j]);
    		}
    		
    		p.possibleValues = possibleValues;
    		p.possibleValuesList = null;
    	}
    }
    $("#testInputParametersField").val(JSON.stringify(TestParameters.input));
    $("#testOutputParametersField").val(JSON.stringify(TestParameters.output));
    
    return true;
}


var TestParameters = {
	input: <%=mapper.writeValueAsString(inputParameters)%>,
	output: <%=mapper.writeValueAsString(outputParameters)%>,
};

var Formatters = {
	nameFormatter: function (cellValue, options, rowObject) {
		if(cellValue!=null){
			var str = "<img src='../images/workflow-icon-settings.png'/> <b>" + escapeHTML( cellValue ) + "</b>";
			if ( rowObject.description != null && rowObject.description != "" ) {
				str += "<br/>" + escapeHTML(rowObject.description);
			}
			return str;
	    }
	    return "";
	},
	
	controlTypeFormatter: function (cellValue, options, rowObject) {
        if(cellValue!=null){
        	return "<b>" + cellValue + "</b>";
        }
        return "";
	},
	
	defaultValueFormatter: function (cellValue, options, rowObject) {
		if ( rowObject.controlType == "list" ) {
            var h =  "<ul>";
            for ( var i=0; i<rowObject.values.length; i++) {
                h += "<li>" + escapeHTML(rowObject.values[i]) + "</li>";
            }
            h += "</ul>";
            return h;
        }
        else {
            return cellValue;
        }
    }, 
	
	editFormatter: function (cellValue, options, rowObject) {
		var str = "<a href='#' onclick=\"ParametersTable.editParameter(" + cellValue + ", '" + rowObject.type + "'); return false;\" class='edit-parameter-link button' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-edit.png'/> Edit</a> ";
		str += "<a href='#' onclick=\"ParametersTable.removeParameter(" + cellValue + ", '" + rowObject.type + "'); return false;\" class='delete-parameter-link button' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-delete.png'/> Delete</a>";
		return str;
	}
}

var ParametersTable = {
	INPUT: {
		columnNames: ['Name','Type', 'Default Value', ' '],
		columnModel: [
		      	    {name:'name', index:'name', sortable:false, formatter:Formatters.nameFormatter}, 
		      	    {name:'controlType', index:'controlType', sortable:false, width:90, formatter:Formatters.controlTypeFormatter},
		      	    {name:'defaultValue', index:'defaultValue', sortable:false, formatter:Formatters.defaultValueFormatter},
		      	    {name:'edit', index:'edit', sortable:false, width:200, formatter:Formatters.editFormatter}
		      	],
	},
	OUTPUT: {
		columnNames: ['Name',' '],
		columnModel: [
		      	    {name:'name', index:'name', sortable:false, sortCustomId:1, formatter:Formatters.nameFormatter}, 
		      	    {name:'edit', index:'edit', sortable:false,sortCustomId:4, width:200, formatter:Formatters.editFormatter}
		      	],
	},
	
	editParameter: function(index, type) {
		TestParameters[type][index].index = index;
		ParameterDialog.open(TestParameters[type][index], type);
	},
	
	removeParameter: function (index, type) {
		if ( index >= 0  && index < TestParameters[type].length) {
            TestParameters[type].splice(index, 1);
            this.initTable("#test-" + type + "-parameters-list", TestParameters[type]);
        } 
	},
	
	initTable: function (gridLocator, parameters) {
		var grid = $(gridLocator);
		grid.jqGrid("clearGridData", false);
		for ( var i=0; i < parameters.length; i++ ) {
			grid.jqGrid("addRowData", i, {index:i, id: parameters[i].id, name: parameters[i].name, description:parameters[i].description, controlType:parameters[i].controlType, defaultValue:parameters[i].defaultValue, values:parameters[i].possibleValuesList, edit: i, type: parameters[i].type});
		}	
	}
};	

var ParameterDialog = {
	parameter:null,
	type: "input",
	save: function () {
		this.parameter.name = $("#parameter-name").val();
		this.parameter.description = $("#parameter-description").val();
		if ( this.type == "input" ) {
			this.parameter.controlType = $("#inputParameter-controlType").val();
	        this.parameter.possibleValuesList = null;
	        if ( this.parameter.controlType == "text") {
	            this.parameter.defaultValue = $("#inputParameter-default-text-value").val(); 
	        }
	        else if ( this.parameter.controlType == "boolean") {
	            this.parameter.defaultValue = $("#inputParameter-default-boolean-value").val(); 
	        }
	        else if ( this.parameter.controlType == "list") {
	            this.parameter.possibleValuesList = TestParametersListEditor.getValuesList();
	            this.parameter.defaultValue = TestParametersListEditor.getDefaultValue(); 
	        }	
		}
		
		if ( this.parameter.index == null ) {
			var index = TestParameters[this.type].length;
			TestParameters[this.type][index] = this.parameter;
			ParametersTable.initTable("#test-" + this.type + "-parameters-list", TestParameters[this.type]);
		}
		else {
			TestParameters[this.type][this.parameter.index] = this.parameter;
            ParametersTable.initTable("#test-" + this.type + "-parameters-list", TestParameters[this.type]);
		}
		this.parameter = null;
		return true;
	},
	open: function (parameter, type) {
		this.type = type;
		var valuesList = [];
        if ( parameter == null ) {
            $("#parameterDialogSubmit").val("Add");
            this.parameter = {name:"", controlType:"text", defaultValue:"", id: null, index:null, type:type};
        }
        else {
            $("#parameterDialogSubmit").val("Save");
            this.parameter = parameter;
            if ( parameter.possibleValuesList != null ) {
                valuesList = parameter.possibleValuesList;
            }
        }
        
		if ( type == "input" ) {
			$(".input-parameter-configs").show();
			$("#inputParameter-default-text-value").val("");
	        $("#inputParameter-default-boolean-value").val("true");
	        
	        TestParametersListEditor.init("#possible-values-list-table tbody", valuesList, this.parameter.defaultValue);
	        $("#possible-value-list-add-text").val("");
	        
	        this.initControls();
		}
		else {
			$(".input-parameter-configs").hide();
		}
		$("#parameter-name").val(this.parameter.name);
		$("#parameter-description").val(this.parameter.description);
		showPopup("parameterDialog", 400, 500);
	},
	initControls: function () {
		$("#inputParameter-controlType").val(this.parameter.controlType);
		if ( this.parameter.controlType == "text" ) {
			$("#inputParameter-default-text-value").val(this.parameter.defaultValue);
		}
		else if ( this.parameter.controlType == "boolean" ) {
            $("#inputParameter-default-boolean-value").val(this.parameter.defaultValue);
        } 
		this.setControlType(this.parameter.controlType);
	},
	setControlType: function (controlType) {
		this.parameter.controlType = controlType;
		$(".default-values .default-value-layout").hide();
		$(".default-values #inputParameter-" + controlType + "-values").show();
		
		
	},
	init: function () {
		$("#parameterDialogSubmit").click(function (){
			if ( ParameterDialog.save() ) {
				closePopup("parameterDialog");	
			}
			return false;
		});
		
		$("#inputParameter-controlType").change(function (){
			var controlType = $("#inputParameter-controlType option:selected").val();
			ParameterDialog.setControlType(controlType);
		});		
	}
};


$(function() {
	ParameterDialog.init();
	
	$("#testTabs").tabs();
	
	$("#test-parameters-tab .add-parameter-to-table").button();
	$("#test-parameters-tab .add-parameter-to-table").click(function (){
		var parameterType = $(this).attr("x-parameter-type");
		ParameterDialog.open(null, parameterType);
		return false;
	});
	
	$("#test-input-parameters-list").jqGrid({
	    datastr:null,
	    datatype: 'jsonstring',
	    jsonReader : {
	        root: "rows"
	    },
	    autowidth:true,
	    height:"100%",
	    rowNum:-1,
	    viewrecords: true,
	    colNames: ParametersTable.INPUT.columnNames,
	    colModel: ParametersTable.INPUT.columnModel,
	    gridview: true,
	    caption: 'Input Parameters'
	});
	
	$("#test-output-parameters-list").jqGrid({
	    datastr:null,
	    datatype: 'jsonstring',
	    jsonReader : {
	        root: "rows"
	    },
	    autowidth:true,
	    height:"100%",
	    rowNum:-1,
	    viewrecords: true,
	    colNames: ParametersTable.OUTPUT.columnNames,
	    colModel: ParametersTable.OUTPUT.columnModel,
	    gridview: true,
	    caption: 'Output Parameters'
	});
	
	ParametersTable.initTable("#test-input-parameters-list", TestParameters.input);
	ParametersTable.initTable("#test-output-parameters-list", TestParameters.output);
	
});
	
</script>
	
<input id="testContentField" type="hidden" name="content" value="${test.content}"/>
<input id="testInputParametersField" type="hidden" name="__inputParametersJson" value=""/>
<input id="testOutputParametersField" type="hidden" name="__outputParametersJson" value=""/>
<div id="testTabs">
	<ul>
		<li><a href="#test-details-tab">Details</a></li>
		<li><a href="#test-content-tab">Content</a></li>
		<li><a href="#test-settings-tab"><img src="../images/workflow-icon-settings.png"/> Settings</a></li>
		<li><a href="#test-parameters-tab"><img src="../images/workflow-icon-settings.png"/> Parameters</a></li>
	</ul>
	<div id="test-details-tab">
		<p>
			Name <br/>
			<tag:edit-field name="name" value="${test.name}" width="100%"></tag:edit-field>
			<c:if test="${testCommand=='Create'}">
            	<input type="hidden" name="projectId" value="${project.id}"/>
            </c:if>
		</p>
		<p>
			Description <br/>
			<textarea name="description" style="width:100%;" rows="10" class="custom-edit-text"><tag:escape text="${test.description}"/></textarea>
		</p>
		<p>
			Mapping <br/>
			<tag:edit-field name="mapping" width="100%" value="${test.mapping}"></tag:edit-field>
		</p>
		<p>
			Test Group <br/>
			<select name="groupId">
	            <option value="0" style="color:gray;">No group...</option>
	            <c:forEach items="${groups}" var="g">
	                <option value="${g.id}" <c:if test="${group!=null &&group.id == g.id }">selected="selected"</c:if>><tag:escape text="${g.name}"/></option>
	            </c:forEach>
	        </select>
		</p>
	</div>
	<div id="test-content-tab">
		<%@ include file="/WEB-INF/jsp/test/testcase-editor.jsp" %>
	</div>
	<div id="test-settings-tab">
		<c:forEach items="${customizationGroups}" var="cgs">
            <h2><tag:escape text="${cgs.name}"></tag:escape> </h2>
            <c:forEach items="${cgs.customizations}" var="c">
               <p>
                  <tag:escape text="${c.customization.name}"/>
                  <br/>
                  <span class="small-description"><tag:escape text="${c.customization.description}"/></span>
                  <tag:customization-edit customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-edit>
               </p>        
            </c:forEach>
        </c:forEach>
	</div>
	<div id="test-parameters-tab">
		<div style="margin-bottom:20px;">
			<table id="test-input-parameters-list"></table>
			<input type='submit' class="add-parameter-to-table" x-parameter-type="input" value="Add parameter"/>
		</div>
		
		<div>
			<table id="test-output-parameters-list"></table>
			<input type='submit' class="add-parameter-to-table" x-parameter-type="output" value="Add parameter"/>
		</div>
		
	</div>
</div>
<br/>
<tag:submit value="${testCommand }"></tag:submit>

<div id="parameterDialog"  style="display:none;">
	<tag:panel title="Parameter" align="center" width="400px" height="500px" closeDivName="parameterDialog">
		<p>
			Name: <br/>
			<tag:edit-field-simple name="parameter-name" id="parameter-name" width="100%" value=""/>
		</p>
		<p>
		    Description: <br/>
		    <textarea class="custom-edit-text" name="parameter-description" id="parameter-description" rows="5" style="width:100%"></textarea>
		</p>
		<div class="input-parameter-configs">
			<p>
	            Type: <br/>
	            <select id="inputParameter-controlType">
	                <option value="text">Text</option>
	                <option value="list">List</option>
	                <option value="boolean">Boolean</option>
	            </select>
	        </p>
	        <div class="default-values" style="height:250px;">
	            <div class="default-value-layout" id="inputParameter-text-values" style="display:none;">
	                Default value:<br/>
	                <tag:edit-field-simple name="inputParameter-default-text-value" id="inputParameter-default-text-value" width="100%" value=""/>
	            </div>
	            <div class="default-value-layout" id="inputParameter-list-values" style="display:none;">
	                <div class="panel-border" style="height:180px;overflow:auto;">
	                    <table id="possible-values-list-table" class="possible-values-list">
	                        <thead>
	                            <tr>
	                                <th width="70px" class="border">Default</th>
	                                <th class="border">Name</th>
	                                <th width="70px"> </th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                        </tbody>
	                    </table>
	                </div>
	                <input type="text" name="possible-value-list-add-text" id="possible-value-list-add-text" class="custom-edit-text"/> 
	                <input id="possible-value-list-add-submit" class="custom-button-text" type="submit" name="possible-value-list-add-submit" value="Add Value"/>
	            </div>
	            <div class="default-value-layout" id="inputParameter-boolean-values" style="display:none;">
	                Default value:<br/>
	                <select id="inputParameter-default-boolean-value">
	                    <option value="true">True</option>
	                    <option value="false">False</option>
	                </select>
	            </div>
	        </div>
		</div>
		
		<tag:submit id="parameterDialogSubmit" value="" onclick="return false;" ></tag:submit>
		<tag:submit value="Cancel" onclick="closePopup('parameterDialog');return false;"></tag:submit>
	</tag:panel>
</div>

<div class="error">
	<tag:spring-form-error field="" command="test"></tag:spring-form-error>
</div>