<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.grid.domain.task.Task"%><div class="breadcrump" align="center">
My Active Tasks
</div>

<table id="jqgTreeGrid"></table> 
<div id="ptreegrid"></div>
<script>
var _progressBars = [];

function setProgressBar(id, value){
	$('#progressBar_'+id).progressbar({value:value});
    $('#progressBarValue_'+id).html(""+value+"%");
}

$(document).ready(function() {
    $('#jqgTreeGrid').jqGrid({
    	treeGrid: true,
        treeGridModel: 'adjacency',
        ExpandColumn : 'name',
        url: '../test-run-manager/ajax-my-active-tasks',
        datatype: "xml",
        mtype: "GET",
        colNames:["id","Name","Created", "Completed", "Progress","Status","Report","Actions"],
        colModel:[
             {name:'id',index:'id', width:1,hidden:true,key:true},
             {name:'name',index:'name', width:180, formatter:nameFormatter},
             {name:'created',index:'created', width:80, align:"center"},
             {name:'completed',index:'completed', width:80, align:"center", formatter:completedDateFormatter},      
             {name:'progress',index:'progress', width:100,align:"left", formatter:progressFormatter},      
             {name:'status',index:'status', width:40,align:"center", formatter:statusFormatter},
             {name:'report',index:'report', width:80,align:"center", formatter:reportFormatter},
             {name:'operations',index:'operations', width:80,align:"left",formatter:operationsFormatter}      
        ],
        height:'auto',
        autowidth:true,
        caption: "Active tasks",
        gridComplete: function () {
             for(var i=0; i<_progressBars.length; i++){
                 if(!_progressBars[i].loaded){
                     setProgressBar(_progressBars[i].id,_progressBars[i].percent);
                     _progressBars[i].loaded = true;
                 }
             }
        }
    });

    setInterval("updateTasks();",5000);
}); 

function updateTasks(){
	$.getJSON('../test-run-manager/ajax-fetch-tasks', function(data) {
        if(data.result=="error"){
            alert("Error occured: "+data.object.text);
        }
        else if(data.result == 'fetched'){
            var tasks = data.object;
            for(var i=0; i<tasks.length; i++){
                if($('#status_tsk'+tasks[i].id).length>0){
                    $('#status_tsk'+tasks[i].id).html(renderStatus(tasks[i].status));
                    setProgressBar('tsk'+tasks[i].id, tasks[i].progress);
                    if(tasks[i].report!=null && tasks[i].report!=""){
                    	$('#report_tsk'+tasks[i].id).html("<a href='../report/browse?suite="+tasks[i].report+"'>Report</a>");    
                    }
                    
                    $('#operations_tsk'+tasks[i].id).html(renderOperations(tasks[i].id, tasks[i].status));
                }
                //updating status for all suites
                if(tasks[i].children!=null){
                    for(var j=0; j<tasks[i].children.length; j++){
                        var suite = tasks[i].children[j];
                        if($('#status_ste'+suite.id).length>0){
                            $('#status_ste'+suite.id).html(renderStatus(suite.status));
                            setProgressBar('ste'+suite.id, suite.progress);
                            if(suite.report!=null && suite.report!=""){
                                $('#report_ste'+suite.id).html("<a href='../report/browse?suite="+suite.report+"'>Report</a>");    
                            }
                        }
                    }
                }
            }
        }
    });
}

function registerProgressbar(id, percent){
	var len = _progressBars.length;
	_progressBars[len] = {id:id, percent: percent, loaded:false};
}

function reportFormatter(cellValue, options, rowObject){
	var id = rowObject.childNodes[1].textContent;

	var str="";
	if(cellValue!=null && cellValue!=""){
		str="<a href='../report/browse?suite="+cellValue+"'>Report</a>";
	}
	return "<div id='report_"+id+"'>"+str+"</div>";
}
function nameFormatter(cellValue, options, rowObject){
    var id = rowObject.childNodes[1].textContent;
    var str = "";
    if(id.indexOf('tsk') === 0){
        str = "<img src='../images/workflow-icon-task.png'/>";
    }
    else if(id.indexOf('ste') === 0){
        str = "<img src='../images/workflow-icon-suite.png'/>";
    }
    return str+" "+cellValue;
}
function progressFormatter(cellValue, options, rowObject){
	var id = rowObject.childNodes[1].textContent;

	if(id.indexOf('tsk') === 0 || id.indexOf('ste') === 0) {
		registerProgressbar(id, parseInt(cellValue));
		return "<div id='progressBar_"+id+"' class='progressbar'></div><div id='progressBarValue_"+id+"' class='progressbar-text'></div>";
    }
    return "";
}

function renderStatus(status){
	var icon = "";
	var text = "";
	if(status==0){
		icon = "active";
		text = "Executing";
	}
	else if(status == 2){
		icon = "completed";
		text = "Completed";
	}
	else if(status == 3){
        icon = "wait";
        text = "Waiting for agent";
    }
	else {
        icon = "error";
        text = "Run error";
    }
	
	return "<img src='../images/workflow-icon-status-"+icon+".png' title='"+text+"'/>";
}

function completedDateFormatter(cellValue, options, rowObject){
	var id = rowObject.childNodes[1].textContent;
	var str ="";
	if(cellValue!=null) str = cellValue;
	return "<div id='completed_"+id+"'>"+str+"</div>";
}

function statusFormatter(cellValue, options, rowObject){
	var status = parseInt(cellValue);
	var id = rowObject.childNodes[1].textContent;
	return "<div id='status_"+id+"'>"+renderStatus(status)+"</div>";
}
function renderOperations(id, status){
	if(status==3 || status==0){
		return  "<a href='javascript:stopTask("+id+");'>Stop</a>";
	}
	else if(status == 2 || status == 1){
		return  "<a href='javascript:removeTask("+id+");'>Remove</a>";
	}
	return "";
}
function operationsFormatter(cellValue, options, rowObject){
    var status = parseInt(rowObject.childNodes[11].textContent);
    var id = rowObject.childNodes[1].textContent;
    if(id.indexOf('tsk') === 0) {
    	return "<div id='operations_"+id+"'>"+renderOperations(id.substr(3), status)+"</div>";
    }
    return " ";
}

function stopTask(taskId){
	if(confirm("Are you sure you want to stop this task?")){
		$('#operations_tsk'+taskId).html('');
        $.getJSON('../test-run-manager/ajax-stop-task?taskId='+taskId, function(data) {
            if(data.result=="error"){
                alert("Error occured: "+data.object.text);
            }
        }); 
    }
}

function removeTask(taskId){
	if(confirm("Are you sure you want to remove this task?")){
	    $.getJSON('../test-run-manager/ajax-remove-completed-task?taskId='+taskId, function(data) {
	        if(data.result=="removed"){
	            $('#jqgTreeGrid').delTreeNode('tsk'+taskId);
	        }
	        else if(data.result=="error"){
	            alert("Error occured: "+data.object.text);
	        }
	        else alert("Unexpected error occured");
	    }); 
	}
}
</script>