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
var TestParameters = {
	input: <%=mapper.writeValueAsString(inputParameters)%>,
	output: <%=mapper.writeValueAsString(outputParameters)%>,
};

var Formatters = {
	nameFormatter: function (cellValue, options, rowObject) {
		if(cellValue!=null){
	        return "<img src='../images/workflow-icon-settings.png'/> <b>" + escapeHTML( cellValue ) + "</b></a>";
	    }
	    return "";
	},
	
	editFormatter: function (cellValue, options, rowObject) {
		var str = "<a href='#' class='edit-parameter-link' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-edit.png'/> Edit</a> <span style='width:50px;display:inline-block;'> </span>";
		str += "<a href='#' class='delete-parameter-link' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-delete.png'/> Delete</a>";
		return str;
	}
}

var ParametersTable = {
	INPUT: {
		columnNames: ['Name','Type', ' '],
		columnModel: [
		      	    {name:'name', index:'name', sortable:true, sortCustomId:1, formatter:Formatters.nameFormatter}, 
		      	    {name:'controlType', index:'controlType',sortCustomId:2, sortable:true, width:90},
		      	    {name:'edit', index:'edit', sortable:false,sortCustomId:4, width:200, formatter:Formatters.editFormatter}
		      	],
	},
	OUTPUT: {
		columnNames: ['Name',' '],
		columnModel: [
		      	    {name:'name', index:'name', sortable:true, sortCustomId:1, formatter:Formatters.nameFormatter}, 
		      	    {name:'edit', index:'edit', sortable:false,sortCustomId:4, width:200, formatter:Formatters.editFormatter}
		      	],
	},
	
	initTable: function (gridLocator, parameters) {
		var grid = $(gridLocator);
		for ( var i=0; i < parameters.length; i++ ) {
			grid.jqGrid("addRowData", i, {id: parameters[i].id, name: parameters[i].name, controlType:'text', defaultValue:parameters[i].defaultValue, values:parameters[i].valuesAsList, edit: parameters[i].id, type: parameters[i].type});
		}	
	}
};	


var InputParameterDialog = {
	parameter:null,
	save: function () {
		this.parameter = null;
		return true;
	},
	open: function (parameter) {
		var valuesList = [];
		if ( parameter == null ) {
			$("#inputParameterDialogSubmit").val("Add");
			this.parameter = {name:"", controlType:"text", defaultValue:"", id: null, type:"input"};
		}
		else {
			$("#inputParameterDialogSubmit").val("Save");
			this.parameter = parameter;
			if ( parameter.valuesAsList != null ) {
				valuesList = parameter.valuesAsList;
			}
		}
		TestParametersListEditor.valuesList = valuesList;
		TestParametersListEditor.init("#possible-valies-list-table tbody");
		$("#possible-value-list-add-text").val("");
		
		this.initControls();
		showPopup("inputParameterDialog", 400, 500);
	},
	initControls: function () {
		$("#inputParameter-controlType").val(this.parameter.controlType);
		$("#inputParameter-name").val(this.parameter.name);
		
		this.setControlType(this.parameter.controlType);
	},
	setControlType: function (controlType) {
		this.parameter.controlType = controlType;
		$(".default-values .default-value-layout").hide();
		$(".default-values #inputParameter-" + controlType + "-values").show();
	},
	init: function () {
		$("#inputParameterDialogSubmit").click(function (){
			if ( InputParameterDialog.save() ) {
				closePopup("inputParameterDialog");	
			}
			return false;
		});
		
		$("#inputParameter-controlType").change(function (){
			var controlType = $("#inputParameter-controlType option:selected").val();
			InputParameterDialog.setControlType(controlType);
		});		
	}
};


$(function() {
	InputParameterDialog.init();
	
	$("#testTabs").tabs();
	
	$("#test-parameters-tab .add-parameter-to-table").button();
	
	$("#test-parameters-tab .add-parameter-to-table").click(function (){
		var parameterType = $(this).attr("x-parameter-type");
		if ( parameterType == "input") {
			InputParameterDialog.open();	
		}
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
	
function onSubmitTest() {
	$("#testContentField").val(testCaseEditor.exportContent());
}
</script>
	
<input id="testContentField" type="hidden" name="content" value="${test.content}"/>
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
		<div>
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

<div id="inputParameterDialog"  style="display:none;">
	<tag:panel title="Input Parameter" align="center" width="400px" height="500px" closeDivName="inputParameterDialog">
		<p>
			Name: <br/>
			<tag:edit-field-simple name="inputParameter-name" id="inputParameter-name" width="100%" value=""/>
		</p>
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
                    <table id="possible-valies-list-table" class="possible-values-list">
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
				<select name="inputParameter-default-boolean-value">
					<option value="true">True</option>
					<option value="false">False</option>
				</select>
			</div>
		</div>
		
		<tag:submit id="inputParameterDialogSubmit" value="" onclick="return false;" ></tag:submit>
		<tag:submit value="Cancel" onclick="closePopup('inputParameterDialog');return false;"></tag:submit>
	</tag:panel>
</div>

<div class="error">
	<tag:spring-form-error field="" command="test"></tag:spring-form-error>
</div>