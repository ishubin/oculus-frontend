<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page import="net.mindengine.oculus.frontend.testrunmanager.domain.task.schedule.DateOccurrence"%>


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="net.mindengine.oculus.frontend.testrunmanager.domain.task.schedule.WeeklyOccurrence"%><script language="javascript">
<%
java.util.List<java.util.Map<String,Object>> tasks = (java.util.List<java.util.Map<String,Object>>)pageContext.findAttribute("tasks");

for(java.util.Map<String,Object> task : tasks)
{
    out.println("var task"+task.get("id")+" = new Object();");
    
    out.println("task"+task.get("id")+".occurrence = new Object();");
    out.println("task"+task.get("id")+".occurrenceType = \""+task.get("occurrenceType")+"\";");
    out.println("task"+task.get("id")+".editable = "+task.get("editable")+";");
    if(task.get("occurrenceType").equals("weekly"))
    {
        WeeklyOccurrence occurrence = (WeeklyOccurrence)task.get("occurrence");
        java.lang.Integer[][]matrix = occurrence.getOccurrenceMatrix();
        out.print("task"+task.get("id")+".occurrence"+".matrix = [");
        for(int day=0;day<7;day++)
        {
            if(day>0)out.print(",");
            out.print("[");
            for(int hour=0;hour<24;hour++)
            {
                if(hour>0)out.print(",");
                out.print(matrix[day][hour]);
            }
            out.print("]");
        }
        out.println("];");
    }
    else if(task.get("occurrenceType").equals("date"))
    {
        DateOccurrence occurrence = (DateOccurrence)task.get("occurrence");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        out.println("task"+task.get("id")+".occurrence"+".date = \""+sdf.format(occurrence.getDate())+"\"");
    }
}%>

function onDeleteTaskClick(taskId)
{
    if(confirm("Are you sure you want to delete this task from scheduler"))
    {
        window.location = "../test-run-manager/scheduler-delete-task?taskId="+taskId;
    }
}
var currentTaskId = 0;
function onEditOccurrenceClick(taskId)
{
	currentTaskId = taskId;
    var task = eval("task"+taskId);
    
    if(task.occurrenceType == "weekly")
    {
        showPopup("divTaskOccurrence",600,400);
        var inputOccurrenceType = document.getElementById("occurrenceType");
        inputOccurrenceType.options[1].selected=false;
        inputOccurrenceType.options[0].selected=true;
        
        var div = document.getElementById("divDateOccurrence");
        div.style.display = "none";
        div = document.getElementById("divWeeklyOccurrence");
        div.style.display = "block";

        var inputTaskId = document.getElementById("weeklyOccurrenceTaskId");
        inputTaskId.value = taskId;
        for(var day=0;day<7;day++)
        {
            for(var hour=0;hour<24;hour++)
            {
            	var divCell = document.getElementById("divWeeklyOccurrenceMatrix_cell_"+day+"_"+hour);
                var inputCell = document.getElementById("weeklyOccurrenceMatrix_cell_"+day+"_"+hour);
                if(task.occurrence.matrix[day][hour] == 0)
                {
                	divCell.className = "weekly-occurrence-matrix";
                    inputCell.value = 0;
                }
                else if(task.occurrence.matrix[day][hour] == 1)
                {
                	divCell.className = "weekly-occurrence-matrix-selected";
                    inputCell.value = 1;
                }
            }
        }
        
    }
    else if(task.occurrenceType == "date")
    {
    	showPopup("divTaskOccurrence",600,400);
        var inputOccurrenceType = document.getElementById("occurrenceType");
        inputOccurrenceType.options[0].selected=false;
        inputOccurrenceType.options[1].selected=true;
        
        var div = document.getElementById("divDateOccurrence");
        div.style.display = "block";
        div = document.getElementById("divWeeklyOccurrence");
        div.style.display = "none";

        var inputDate = document.getElementById("dateOccurrence_date");
        inputDate.value = task.occurrence.date;

        var inputTaskId = document.getElementById("dateOccurrenceTaskId");
        inputTaskId.value = taskId;

        for(var day=0;day<7;day++)
        {
            for(var hour=0;hour<24;hour++)
            {
                var divCell = document.getElementById("divWeeklyOccurrenceMatrix_cell_"+day+"_"+hour);
                var inputCell = document.getElementById("weeklyOccurrenceMatrix_cell_"+day+"_"+hour);
                divCell.className = "weekly-occurrence-matrix";
                inputCell.value = 0;
            }
        }
    }
}
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
function onDateChanged(date)
{
	
}
function onOccurrenceTypeChange(typeId)
{
	if(typeId == 0)
	{
		var div = document.getElementById("divDateOccurrence");
        div.style.display = "none";
        div = document.getElementById("divWeeklyOccurrence");
        div.style.display = "block";

        var inputTaskId = document.getElementById("weeklyOccurrenceTaskId");
        inputTaskId.value = currentTaskId;
	}
	else if(typeId == 1)
	{
		var div = document.getElementById("divDateOccurrence");
        div.style.display = "block";
        div = document.getElementById("divWeeklyOccurrence");
        div.style.display = "none";

        var inputTaskId = document.getElementById("dateOccurrenceTaskId");
        inputTaskId.value = currentTaskId;
	}
}
</script>

<div id="divTaskOccurrence" style="display:none;">
    <tag:panel align="center" title="Task Occurrence" width="600px" height="400px" closeDivName="divTaskOccurrence">
       Choose occurrence type
       <select id="occurrenceType" onchange="onOccurrenceTypeChange(this.selectedIndex);">
           <option value="0">Weekly</option>
           <option value="1">Date</option>
       </select>
       
       <div id="divWeeklyOccurrence" style="display:none;">
           <form action="../test-run-manager/scheduler-change-occurrence" method="post">
               <input id="weeklyOccurrenceTaskId" name="taskId" type="hidden" value="0"/>
               <input name="occurrenceType" type="hidden" value="0"/>
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
	           <tag:submit name="Submit" value="Change"></tag:submit>
	       </form>     
           <div class="small-description" id="divWeeklyOccurrenceMatrix_display" style="height:30px;"></div>
       </div>
       <div id="divDateOccurrence" style="display:none;">
           <form action="../test-run-manager/scheduler-change-occurrence" method="post">
               <input id="dateOccurrenceTaskId" name="taskId" type="hidden" value="0"/>
               <input name="occurrenceType" type="hidden" value="1"/>
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
	           <tag:submit name="Submit" value="Change"></tag:submit>
	       </form>
       </div>
    </tag:panel>
</div>

<table id="tasksTable">
    <tr>
        <th>Name</th>
        <th>Occurence</th>
        <th>Closest Event</th>
        <th>User</th>
        <th> </th>
    </tr>
    <c:forEach items="${tasks}" var="task">
        <tr>
            <td><img src="../images/workflow-icon-task.png"/><b>${task.name}</b></td>
            <td width="120px"><c:choose><c:when test="${task.occurrenceType == 'weekly'}"><a href="javascript:onEditOccurrenceClick(${task.id})"><b>Weekly</b></a></c:when><c:when test="${task.occurrenceType == 'date'}"><a href="javascript:onEditOccurrenceClick(${task.id})"><b>Date:</b></a></c:when></c:choose></td>
            <td width="150px"><c:if test="${task.closestEvent!=null}"><tag:date date="${task.closestEvent}"/></c:if><div style="white-space: nowrap;">${task.eta}</div></td>
            <td width="100px" style="padding:4px;"><img src="../images/workflow-icon-user.png"/> <tag:escape text="${task.user.name}"></tag:escape></td>
            <td width="32px" style="text-align:center;"><c:if test="${task.editable == true}"><a href="javascript:onDeleteTaskClick(${task.id});" title="Delete task from scheduler"><img src="../images/button-stop.png"/></a></c:if></td>
        </tr>
    </c:forEach>
</table>
<script>
$(document).ready(function(){
    tableToGrid("#tasksTable",{
        height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'Scheduled tasks'
    });
});
</script>