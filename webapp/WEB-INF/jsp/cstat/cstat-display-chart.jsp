<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticParameter"%>
<%@page import="com.sdicons.json.mapper.JSONMapper"%>
<%@page import="com.sdicons.json.model.JSONValue"%>


<%@page import="net.mindengine.oculus.frontend.domain.customstatistics.CustomStatisticChart"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%><script type="text/javascript" src="../scripts/jscolor/jscolor.js"></script>

<div class="breadcrump" align="center">

    <c:if test="${project!=null}">
        <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
        <img src="../images/breadcrump-arrow.png"/> 
        <a href="../cstat/browse?projectId=${project.id}"><img src="../images/workflow-icon-custom-statistic.png"/> Custom Statistics</a>
        <img src="../images/breadcrump-arrow.png"/>
    
    </c:if>
    <a href="../cstat/display?id=${customStatistic.id}"><img src="../images/workflow-icon-custom-statistic.png"/> <tag:escape text="${customStatistic.name}"></tag:escape></a>
    <img src="../images/breadcrump-arrow.png"/>
    <img src="../images/workflow-icon-report.png"/> <tag:escape text="${chart.name}"/>

    

</div>
<script language="javascript">
var _parameters = 
<%
//Printing all parameters to js var so later it will be used to build navigation table
Collection<CustomStatisticParameter> parameters = (Collection<CustomStatisticParameter>)pageContext.findAttribute("parameters");

JSONMapper mapper = new JSONMapper();
JSONValue value = mapper.toJSON(parameters);
out.print(value.render(true));
%>
;

var _grid = 
<%
CustomStatisticChart chart = (CustomStatisticChart) pageContext.findAttribute("chart");

if(chart.getParameters()!=null && !chart.getParameters().isEmpty()){
    out.print(StringEscapeUtils.unescapeJavaScript(chart.getParameters().substring(1, chart.getParameters().length()-1)));
    out.println(";");   
}
else {
    out.print("[];");
}
%>

function renderGrid(){
	var str = "";

    str+="<table class='grid' cellspacing='0' cellpadding='0'>";

    str+="<tr>";
    str+="<td class='grid grid-header'>Name";
    str+="</td>";
    for(var i=0; i<_grid.length; i++){
        str+="<td class='grid grid-regular'>";
        str+="<input id='gridName_"+i+"' onchange='onChangeGridName("+i+", this);' class='grid-text' value='"+_grid[i].name+"'/>";
        str+="</td>";
    }
    str+="<td></td>";
    str+="</tr>";

    str+="<tr>";
    str+="<td class='grid grid-header'>Color";
    str+="</td>";
    for(var i=0; i<_grid.length; i++){
        str+="<td class='grid grid-regular'>";
        str+="<input id='gridColor_"+i+"' class='color grid-text' onchange='onChangeGridColor("+i+",this);' value='"+_grid[i].color+"'/>";
        str+="</td>";
    }
    str+="<td></td>";
    str+="</tr>";

    for(var i=0; i<_parameters.length; i++){
        str+="<tr>";
        str+="<td class='grid grid-header'>"+escapeHTML(_parameters[i].name);
        str+="</td>";

        for(var j=0; j<_grid.length; j++){
        	str+="<td class='grid grid-regular'>";
            str+="<select id='gridParameter_"+_parameters[i].id+"_"+j+"' onchange='onChangeGridParameter("+j+","+i+",this);' class='grid-select'>";
            str+="<option></option>";
            
            for(var k=0; k<_parameters[i].possibleValues.length; k++){
            	var strSelected = "";
            	if(_grid[j].parameters[i] == _parameters[i].possibleValues[k]){
                	strSelected = "selected='selected'";
            	}
            	str+="<option "+strSelected+" value='"+escapeHTML(_parameters[i].possibleValues[k])+"'>"+escapeHTML(_parameters[i].possibleValues[k])+"</option>";
            }
            str+="</select>";
            str+="</td>";
        }
        str+="<td></td>";
        str+="</tr>";
    }

    str+="<tr>";
    str+="<td class='grid grid-header'><span> </span> </td>";
    for(var i=0; i<_grid.length; i++) {
        str+="<td class='grid grid-regular'><span> </span>";
        str+="<a href='javascript:removeGraph("+i+");' class='red' style='white-space:nowrap;'><img src='../images/workflow-icon-delete.png'/> Remove</a>";
        str+="</td>";
        
    }
    str+="<td> <a href='javascript:addGraph();' style='white-space:nowrap;'><img src='../images/workflow-icon-report.png'/> Add</a></td>";
    str+="</tr>";
    
    str+="</table>";

    document.getElementById("grid").innerHTML = str;

    //Handling jscolor inputs

    for(var i=0; i<_grid.length; i++){
    	var myPicker = new jscolor.color(document.getElementById("gridColor_"+i), {});
    	if(_grid[i].color!=null && _grid[i].color!=""){
    		myPicker.fromString(_grid[i].color);
    	}
    	else myPicker.fromString("FF0000");
    }
}

function addGraph(){
	var id = _grid.length;
	_grid[id] = {
		color:"FF0000",
		name:"Undefined "+id,
		parameters:[]
	};

	for(var i=0;i<_parameters.length; i++){
		_grid[id].parameters[i] = "";
	}
	
	renderGrid();
}

function removeGraph(i){
	_grid.splice(i,1);
	renderGrid();
}

function onChangeGridName(id, input){
    _grid[id].name = input.value;
}
function onChangeGridColor(id, input){
	_grid[id].color = input.value;
}
function onChangeGridParameter(id, pId, select){
	_grid[id].parameters[pId] = select.options[select.selectedIndex].value;
}
</script>

<div id="grid" class="grid-container">

</div>

<script language="javascript">
renderGrid();

function onSaveChart(){
	var gridText = JSON.stringify(_grid);
	document.forms.saveChart.parameters.value = gridText;
	document.forms.saveChart.submit();
}
</script>

<br/>
<br/>

<tag:submit value="Save" onclick="onSaveChart();"></tag:submit>
<form name="saveChart" action="../cstat/save-chart-settings" method="post">
    <input type="hidden" name="id" value="${chart.id}"/>
    <input type="hidden" name="parameters" value=""/>
</form>


