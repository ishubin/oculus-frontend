<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="java.util.LinkedList"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmProperty"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <a href="../grid/customize-suite-parameters">Customize Suite Parameters for Grid</a>
    <img src="../images/breadcrump-arrow.png"/>
    ${fn:escapeXml(project.name)}
</div>

<p>
	This set of parameters will be displayed in Test Run Manager.<br/>
	Users will be available to choose these parameters and run the test suite with the selected parameters.
</p>
<%
List<TrmProperty> properties = (List<TrmProperty>) pageContext.findAttribute("suiteProperties");
if ( properties == null ) {
    properties = new LinkedList<TrmProperty>();
}
ObjectMapper mapper = new ObjectMapper();
%>

<script>
var _suiteParameters = <%=mapper.writeValueAsString(properties)%>;

var ParametersTable = {
	editParameter: function(index) {
        _suiteParameters[index].index = index;
        ParameterDialog.open(_suiteParameters[index], "input");
    },
	
	removeParameter: function (index) {
        if ( index >= 0  && index < _suiteParameters.length) {
            _suiteParameters.splice(index, 1);
            this.initTable();
        } 
    },
		
	initTable: function () {
		var grid = $("#suite-parameters-list");
	    grid.jqGrid("clearGridData", false);
	    for ( var i=0; i < _suiteParameters.length; i++ ) {
	        grid.jqGrid("addRowData", i, {index:i, id: _suiteParameters[i].id, name: _suiteParameters[i].name, description: _suiteParameters[i].description, controlType: _suiteParameters[i].subtype, values: _suiteParameters[i].possibleValuesList, edit: i});
	    }
	}
}

var Formatters = {
	nameFormatter: function (cellValue, options, rowObject) {
        if(cellValue != null){
            var str = "<img src='../images/workflow-icon-settings.png'/> <b>" + escapeHTML( cellValue ) + "</b>";
            if ( rowObject.description != null && rowObject.description != "" ) {
                str += "<br/><div class='small-description'>" + escapeHTML(rowObject.description) + "</div>";
            }
            return str;
        }
        return "";
    },
    
    controlTypeFormatter: function (cellValue, options, rowObject) {
        if(cellValue != null){
            str = "<b>" + cellValue + "</b>";
            if ( cellValue == "list" ) {
                str +=  "<ul>";
                for ( var i=0; i<rowObject.values.length; i++) {
                    str += "<li>" + escapeHTML(rowObject.values[i]) + "</li>";
                }
                str += "</ul>";
            }
            return str;
        }
        return "";
    },
    
    editFormatter: function (cellValue, options, rowObject) {
        var str = "<a href='#' onclick=\"ParametersTable.editParameter(" + cellValue + "); return false;\" class='edit-parameter-link button' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-edit.png'/> Edit</a> ";
        str += "<a href='#' onclick=\"ParametersTable.removeParameter(" + cellValue + "); return false;\" class='delete-parameter-link button' parameter-id='" + cellValue + "' parameter-type='" + rowObject.type + "'><img src='../images/workflow-icon-delete.png'/> Delete</a>";
        return str;
    }
}

$(function () {
	$("#add-parameter-to-table").click(function (){
        ParameterDialog.open(null, "input");
        return false;
    });
	
	ParameterDialog.defaultValuesEnabled = false;
	ParameterDialog.controlTypeField = "subtype";
	ParameterDialog.init();
	
	$("#suite-parameters-list").jqGrid({
        datastr:null,
        datatype: 'jsonstring',
        jsonReader : {
            root: "rows"
        },
        autowidth:true,
        height:"100%",
        rowNum:-1,
        viewrecords: true,
        colNames: ['Name','Type', ' '],
        colModel: [
                   {name:'name', index:'name', sortable:false, formatter:Formatters.nameFormatter}, 
                   {name:'controlType', index:'controlType', sortable:false, width:90, formatter:Formatters.controlTypeFormatter},
                   {name:'edit', index:'edit', sortable:false, width:100, formatter:Formatters.editFormatter}
               ],
        gridview: true,
        caption: 'Input Parameters'
    });
	
	
	ParameterDialog.onSave = function() {
		if ( this.parameter.index == null ) {
            var index = _suiteParameters.length;
            _suiteParameters[index] = this.parameter;
            ParametersTable.initTable();
        }
        else {
        	_suiteParameters[this.parameter.index] = this.parameter;
            ParametersTable.initTable();
        }
    };
	ParametersTable.initTable();
});


function onSubmitForm() {
	for ( var i=0; i<_suiteParameters.length;  i++ ) {
		if ( _suiteParameters[i].subtype == "list" ) {
			var value = "";
			for ( var j=0; j<_suiteParameters[i].possibleValuesList.length; j++ ) {
				value += "<value>" + escapeHTML(_suiteParameters[i].possibleValuesList[j]);
			}
			_suiteParameters[i].value = value;
		}
	}
	$("#parameters-json").val(JSON.stringify(_suiteParameters));
}
</script>


<table id="suite-parameters-list"></table>
<input type='submit' id="add-parameter-to-table" class="custom-button-text" x-parameter-type="input" value="Add parameter"/>


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
            <div class="default-values">
                <div class="default-value-layout" id="inputParameter-text-values" style="display:none;">
                    Default value:<br/>
                    <tag:edit-field-simple name="inputParameter-default-text-value" id="inputParameter-default-text-value" width="100%" value=""/>
                </div>
                <div class="default-value-layout" id="inputParameter-list-values" style="display:none;">
                    <div class="panel-border" style="height:180px;overflow:auto;">
                        <table id="possible-values-list-table" class="possible-values-list">
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

<form method="post" onsubmit="onSubmitForm();">
    <input id="parameters-json" name="parameters" type="hidden" value=""/>
	<div style="margin-top:20px">
	    When you are done save changes 
	    <tag:submit name="Submit" value="Save"/>
	</div>
</form>