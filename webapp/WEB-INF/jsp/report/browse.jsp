

<%@page import="com.sdicons.json.model.JSONValue"%>
<%@page import="com.sdicons.json.mapper.JSONMapper"%>
<%@page import="net.mindengine.oculus.frontend.db.search.SearchColumn"%><jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.TestRunSearchData"/>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.ReportSearchColumn"/><%@ include file="/include.jsp" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.SearchFilter"/>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.TestRunSearchResult"/>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.HashMap"/>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<c:if test="${user!=null}">
    <form action="../report/browse-remember-selected-runs" method="post" name="rememberSelectedRuns">
       <input type="hidden" name="ids" value=""/>
       <input type="hidden" name="removeIds" value=""/>
       <input type="hidden" name="redirect" value=""/>
    </form>
    <form action="../report/browse-save-collected-runs" method="post" name="saveCollectedRuns">
       <input type="hidden" name="saveName" value=""/>
       <input type="hidden" name="redirect" value=""/>
    </form>
    
    <tag:pickissue-setup></tag:pickissue-setup>
    
    <script>
    <%
    /*
     * Setting all saved runs to js variable
     */
     List<TestRunSearchData> savedRuns = (List<TestRunSearchData>)pageContext.findAttribute("savedTestRunList"); 
     if(savedRuns!=null)
     {
         JSONValue jsonValue = JSONMapper.toJSON(savedRuns);
         String str = "var _savedTestRuns = "+jsonValue.render(false)+";";
         out.print(str);
     }
     else out.print("var _savedTestRuns = [];");
    %>

     function verifySavedTestRunList()
     {
         var bResult = true;
         var isProjectSame = true;
         var isProjectZero = false;
         if(_savedTestRuns.length>0)
         {
             var parentProjectId = _savedTestRuns[0].parentProjectId;
             for(var i = 0; i<_savedTestRuns.length; i++)
             {
                 if(parentProjectId!=_savedTestRuns[i].parentProjectId)
                 {
                     isProjectSame = false;
                     bResult = false;
                 }
                 if(_savedTestRuns[i].parentProjectId==0)
                 {
                     var div = document.getElementById("divSavedTestRunWarnIcon"+_savedTestRuns[i].id);
                     if(div!=null)
                     {
                         div.style.display = "block";
                     }
                     bResult = false;
                     isProjectZero = true;
                 }
             }
         }
         var msg = "";
         if(!isProjectSame)
         {
             msg+="<img src=\"../images/workflow-icon-warn.png\"/> The tests you have selected refer to different projects. The sub-projects may be different but the project of the test runs is required to be the same.<br/><br/>";
         }
         if(isProjectZero)
         {
             msg+="<img src=\"../images/workflow-icon-warn.png\"/> There are test runs which refer to unexistent project. Please remove them from the list<br/><br/>";
         }
         if(!bResult)
         {
             showWarning(msg);
         }
         return bResult;
     }
     function onLinkToIssueClick()
     {
         if(verifySavedTestRunList() && _savedTestRuns.length > 0)
         {
             var divParams = document.getElementById("divLinkToIssueParameters");
             divParams.innerHTML = "Loading parameters <img src=\"../images/loading.gif\"/>";
             loadSuiteParametersToLinkIssuePanel();
             showPopup("divLinkToIssuePanel", 400,500);
         }
     }
     function loadSuiteParametersToLinkIssuePanel()
     {
         var projectId = _savedTestRuns[0].parentProjectId;
         document.forms.linkToIssue.projectId.value = projectId;
         dhtmlxAjax.post("../grid/ajax-fetch-suite-parameters", "projectId="+projectId, onAjaxFetchSuiteParametersResponse);
     }
     function onAjaxFetchSuiteParametersResponse(loader)
     {
         var str = loader.xmlDoc.responseText;
         var obj = eval("("+str+")");
 
         if(obj.result != "error")
         {
             var parameters = obj.object;

             var html = "Parameters:<br/><table border=\"0\" class=\"browse-report\">";
             html+="<thead>";
             html+="<tr>";
             html+="<td class=\"browse-report\">Use</td>";
             html+="<td class=\"browse-report\" width=\"50%\">Name</td>";
             html+="<td class=\"browse-report\" width=\"50%\">Value</td>";
             html+="</tr>";
             html+="</thead>";
             html+="<tbody>";
             for(var i=0;i<parameters.length;i++)
             {
                 html+="<tr class=\"gray\">";
                 html+="<td class=\"browse-report\"><input type=\"checkbox\" name=\"linkIssueSuiteParameterUse"+parameters[i].id+"\" onchange=\"onLinkToIssueUseChange(this,"+parameters[i].id+");\"/></td>";
                 html+="<td class=\"browse-report\">"+escapeHTML(parameters[i].name)+"</td>";
                 if(parameters[i].subtype=='list')
                 {
                     html+="<td class=\"browse-report\">";
                     html+="<select name=\"linkIssueSuiteParameterValue"+parameters[i].id+"\"  style=\"width:100%;height:100%;\" disabled=\"disabled\">";
                     for(var j=0;j<parameters[i].values.length;j++)
                     {
                         html+="<option>"+escapeHTML(parameters[i].values[j])+"</option>";
                     }
                     html+="</select>";
                     html+="</td>";
                 }
                 else if(parameters[i].subtype=='text')
                 {
                     html+="<td class=\"browse-report\"><input style=\"width:100%;height:100%;\" disabled=\"disabled\" type=\"text\" name=\"linkIssueSuiteParameterValue"+parameters[i].id+"\"/></td>";
                 }
                 else if(parameters[i].subtype=='checkbox')
                 {
                     html+="<td class=\"browse-report\"><input type=\"checkbox\" disabled=\"disabled\" name=\"linkIssueSuiteParameterValue"+parameters[i].id+"\"/></td>";
                 }
                 else html+="<td class=\"browse-report\"></td>";
                 html+="</tr>";
             }
             html+="</tbody>";
             html+= "</table>";

             var divParams = document.getElementById("divLinkToIssueParameters");
             divParams.innerHTML = html;
         }
         else alert(str);
     }
     function onLinkToIssueUseChange(checkbox, id)
     {
         var element = document.forms.linkToIssue.elements["linkIssueSuiteParameterValue"+id];
         if(checkbox.checked)
         {
             element.disabled = false;
         }
         else element.disabled = true;
     }
     function onClickSubmitLinkToIssue()
     {
         document.forms.linkToIssue.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
         return true;
     }
     function validateLinkToIssue(form)
     {
         if(form.reportLinkIssue.value.length<1)
         {
             document.getElementById("divLinkToIssueError").innerHTML = "You haven't selected an issue";
             return false;
         }
         return true;
     }
     </script>
     
     <div id="divLinkToIssuePanel" style="display:none;">
         <tag:panel align="center" title="Link To Issue" logo="../images/workflow-icon-bug.png" width="400px" height="100px" closeDivName="divLinkToIssuePanel">
             <form name="linkToIssue" onsubmit="return validateLinkToIssue(this);" action="../report/link-to-issue" method="post">
                 Choose Issue and specify the condition which matches the issue
                 <br/>
                 <input type="hidden" name="redirect" value=""/>
                 <input type="hidden" name="projectId" value=""/>
                 <tag:pickissue-button issueName="" issueId="" id="reportLinkIssue" parentPanel="divLinkToIssuePanel" parentPanelWidth="400" parentPanelHeight="500"></tag:pickissue-button>
                 <br/>
                 <br/>
                                         
                 Reason Pattern (Java RegEx):
                 <br/>
                 <tag:edit-field-simple name="linkIssueReasonPattern" id="linkIssueReasonPattern" width="100%"></tag:edit-field-simple>
                 <br/>
                 <div id="divLinkToIssueParameters">
                 </div>
                 <br/>
                 <tag:submit align="center" value="Link to Issue" onclick="return onClickSubmitLinkToIssue();" ></tag:submit>
                 <div id="divLinkToIssueError" class="error"></div>
             </form>
         </tag:panel>
     </div>
     
     
     <c:if test="${savedTestRunListCount>0}">
         <tag:panel align="left" title="Collected Test Runs" width="100%" id="panelCollectedTestRuns" disclosure="true" closed="false">
             <tag:submit value="Save Choosen Runs" onclick="javascript:onSaveChoosenRuns();"/> 
             <tag:submit value="Link To Issue" onclick="javascript:onLinkToIssueClick();"/>
             <br/>
             <br/>
             <input type="hidden" name="numerOfCollectedRuns" id="numerOfCollectedRuns" value="${savedTestRunListCount}"/>
             <table border="0" class="browse-report" width="100%">
                  <thead>
                      <tr>
                          <td class="browse-report">
                              <input type="checkbox" name="chkCollectedRunSelectAll" id="chkRunSelectAll" onchange="onCollectedRunSelectAll(this);"/>
                              <input type="hidden" name="numerOfCollectedRuns" id="numerOfCollectedRuns" value="${savedTestRunListCount}"/>
                              <div >
                              </div>
                          </td>
                          <td class="browse-report">Test Run Id</td>
                          <td class="browse-report">Test Id</td>
                          <td class="browse-report">Test Name</td>
                          <td class="browse-report">Project</td>
                          <td class="browse-report">Sub-Project</td>
                          <td class="browse-report">Reason</td>
                      </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${savedTestRunList}" var="row">
                      <tr class="row-${row.testRunStatus}">
                          <td class="browse-report">
                             <table border="0" cellpadding="0" cellspacing="0" width="40px">
                                 <tr>
                                     <td>
                                         <input type="checkbox" name="chkCollectedRun_${row.id}" id="chkCollectedRun_${row.id}"/>
                                         <input type="hidden" name="collectedTestRunId_${row.id}" id="collectedTestRunId_${row.id}" value="${row.testRunId}"/>
                                     </td>
                                     <td>
                                         <div id="divSavedTestRunWarnIcon${row.id}" style="display:none;"><img src="../images/workflow-icon-warn.png"/></div>
                                     </td>
                                 </tr>
                              </table>
                          </td>  
                          <td class="browse-report">${row.testRunId}</td>
                          <td class="browse-report">${row.testId}</td>
                          <td class="browse-report">${row.fetchTestName}</td>
                          <td class="browse-report"><tag:escape text="${row.parentProjectName}"/></td>
                          <td class="browse-report">${row.fetchProjectName}</td>
                          <td class="browse-report">
                             <c:if test="${row.hasReasons==true}">
                                  <div title="${row.firstReason}">
                                      <tag:cut-text text="${row.firstReason}" maxSymbols="40" endWith=" ..."/>
                                  </div>
                              </c:if>
                              <i> </i>
                          </td>
                      </tr>
                      </c:forEach>
                 </tbody>
              </table>
              <br/>
              <br/>
              <tag:submit value="Delete Selected" onclick="javascript:onDeleteSelectedCollectedRuns();"/>
         </tag:panel>
         
     </c:if>
     
</c:if>
<%
//Preparing the Page Limit drop-down list model
{
    SearchFilter filter = (SearchFilter)pageContext.findAttribute("reportSearchFilter");
    
    
    List<Object> list = new ArrayList<Object>();
    
    for(int i=0;i<SearchFilter.PAGE_LIMITS.length;i++) {
        Map<Object,Object> map = new HashMap<Object,Object>();
        map.put("label",SearchFilter.PAGE_LIMITS[i]);
        map.put("id",i);
        if(filter.getPageLimit().equals(i))
        {
            map.put("selected",true);
        }
        else map.put("selected",false);
        list.add(map);
    }
    
    pageContext.setAttribute("pageLimitOptions",list);
}
%>
 
<script>
function onPageLimitChange(select)
{
    document.forms.browseFilter.pageLimit.value = select.options[select.selectedIndex].value;
    document.forms.browseFilter.pageOffset.value = 1;
    redirectBrowseFilterForm(document.forms.browseFilter);
}
function onPageClick(id)
{
    document.forms.browseFilter.pageOffset.value = id;
    redirectBrowseFilterForm(document.forms.browseFilter);
}
function onOrderByColumn(columnId,orderType)
{
    document.forms.browseFilter.orderByColumnId.value = columnId;
    document.forms.browseFilter.orderDirection.value = orderType;
    redirectBrowseFilterForm(document.forms.browseFilter);
}

function onRunSelectAllCheckbox(chkAll)
{
    var bChecked = chkAll.checked;
    var count = document.getElementById("numerOfDisplayedResults").value;
    
    var i=0;
    for(i=0;i<count;i++)
    {
        var chk = document.getElementById("chkRunSelect_"+i);
        chk.checked = bChecked;
    }
}
function onRememberSelectedRuns()
{
    var count = document.getElementById("numerOfDisplayedResults").value;
    var strIds = "";
    var bHasOne = false;
    var i=0;
    for(i=0;i<count;i++)
    {
        var chk = document.getElementById("chkRunSelect_"+i);
        if(chk.checked)
        {
            var runId = document.getElementById("testRunId_"+i).value;
            if(bHasOne)strIds+=",";
            strIds+= runId;
            bHasOne = true; 
        }
    }
    if(!bHasOne)
    {
        alert("You haven't selected the test runs");
    }
    else
    {
    	document.forms.rememberSelectedRuns.ids.value = strIds;
    	document.forms.rememberSelectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
    	document.forms.rememberSelectedRuns.submit();
    }
}

function onSaveChoosenRuns()
{
    var name = prompt("Enter the name for the saved run","");
    if(name!=null && name!="")
    {
    	document.forms.saveCollectedRuns.saveName.value = name;
    	document.forms.saveCollectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
    	document.forms.saveCollectedRuns.submit();
    }
}
function onCollectedRunSelectAll(chkAll)
{
    var bChecked = chkAll.checked;
    var count = document.getElementById("numerOfCollectedRuns").value;
    
    var i=0;
    for(i=0;i<count;i++)
    {
        var chk = document.getElementById("chkCollectedRun_"+i);
        chk.checked = bChecked;
    }
}
function onDeleteSelectedCollectedRuns()
{
    var count = document.getElementById("numerOfCollectedRuns").value;
    var strIds = "";
    var bHasOne = false;
    var i=0;
    for(i=0;i<count;i++)
    {
        var chk = document.getElementById("chkCollectedRun_"+i);
        if(chk.checked)
        {
            var runId = document.getElementById("collectedTestRunId_"+i).value;
            if(bHasOne)strIds+=",";
            strIds+= runId;
            bHasOne = true; 
        }
    }
    if(!bHasOne)
    {
        alert("You haven't selected the test runs");
    }
    else
    {
    	document.forms.rememberSelectedRuns.removeIds.value = strIds;
        document.forms.rememberSelectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
        document.forms.rememberSelectedRuns.submit();
    }
}
function onReportRowClick(rowId)
{
	var div = document.getElementById("testRunDetails"+rowId);
	var divLink = document.getElementById("rowExpandButton"+rowId);
	if(div.style.display == "block")
	{
		div.style.display = "none";
		divLink.className = "report-row-expand";
	}
	else 
	{
        div.style.display = "block";
        divLink.className = "report-row-collapse";
    }
}


</script>


Number of results: <span id="numberOfResults" style="font-weight:bold;"></span><br/>
Display restuls: 
<select id="paginationPageLimit" onchange="onPageLimitChange(this);">
    <c:forEach items="${pageLimitOptions}" var="option">
        <option value="${option.id}" <c:if test="${option.selected == true}">selected="true"</c:if>>${option.label}</option>
    </c:forEach>
</select>

<div id="pagination" class="pagination">
</div>
<br/>
<c:if test="${user!=null}">
    <tag:submit value="Remember Selected runs" onclick="javascript:onRememberSelectedRuns();"/>
    <br/>
    <br/>
</c:if>
<div id="reportBody">
	<table id="list"><tr><td/></tr></table> 
</div>
<br/>
<c:if test="${user!=null}">
    <tag:submit value="Remember Selected runs" onclick="javascript:onRememberSelectedRuns();"/>
</c:if>
<script>


//Used for specifying the color to the entire row
var rowsToColor = [];

var columnModel = [ 
    {name:'repid', sortCustomId: 0,  index:'repid', width:30, sorttype:'int', sortable:true, shown:true, formatter:reportCheckboxFormatter},
    {name:'report',sortCustomId: 0, index:'report', width:70, sorttype:'int', sortable:true, shown:true, formatter:reportLinkFormatter},
    {name:'status',sortCustomId: 3, index:'status', width:95, sorttype:'int', sortable:true, shown:true, formatter:rowColorFormatter},
    {name:'testName', sortCustomId: 1, index:'testName', sorttype:'int', sortable:true, shown:true, formatter:testNameFormatter},
    {name:'subProject', sortCustomId: 2, index:'subProject', sorttype:'int', sortable:true, shown:true, formatter:subProjectFormatter},
    {name:'designer', sortCustomId: 4, index:'designer', sorttype:'int', sortable:true, shown:true, formatter:userFormatter},
    {name:'runner', sortCustomId: 5, index:'runner', sorttype:'int', sortable:true, shown:true, formatter:userFormatter},
    {name:'startTime', sortCustomId: 6, index:'startTime', sorttype:'int', sortable:true, shown:true},
    {name:'suiteName', sortCustomId: 7, index:'suiteName', sorttype:'int', sortable:true, shown:true, formatter:suiteFormatter},
    {name:'reason', sortCustomId: 8, index:'reason', sorttype:'int', sortable:true, shown:true, formatter:reasonFormatter},
    {name:'agent', sortCustomId: 9, index:'agent', sorttype:'int', sortable:true, shown:true},
    {name:'issue', sortCustomId: 10, index:'issue', sorttype:'int', sortable:true, shown:true, formatter:issueFormatter}
];
function issueFormatter(cellValue, options, rowObject){
	if(cellValue!=null){
		return "<a href='../issue/display?id="+cellValue.id+"'><img src='../images/workflow-icon-bug.png'/> "+escapeHTML(cellValue.name)+"</a>";
	}
	return "";
}
function reasonFormatter(cellValue, options, rowObject){
	if(cellValue!=null && cellValue.length>0){
		return "<span class='reason-cell'>"+escapeHTML(cellValue[0]) + "</span>";
	}
	return "";
}
function userFormatter(cellValue, options, rowObject){
	if(cellValue!=null){
	    return "<a href='../user/profile-"+cellValue.login+"'><img src='../images/workflow-icon-user.png'/> "+escapeHTML(cellValue.name)+"</a>";
	}
	return "";
}
function testNameFormatter(cellValue, options, rowObject){
	if(cellValue.testId!=null){
		var res = "<a href='../test/display?id="+cellValue.testId+"'><img src='../images/workflow-icon-test.png'/> "+escapeHTML(cellValue.testName)+"</a> ";
		if(cellValue.testDescription!=null){
			res = res + " <span>"+escapeHTML(cellValue.testDescription)+"</span>";
		} 
		return res;
	}
	else {
		var res = ""+escapeHTML(cellValue.testName);
		if(cellValue.testDescription!=null){
			res = res+" "+escapeHTML(cellValue.testDescription);
		}
		return res;
	}
}
function suiteFormatter(cellValue, options, rowObject){
	if(cellValue!=null){
		return "<a href='../report/browse?suite="+cellValue.id+"'>"+escapeHTML(cellValue.name)+"</a>";
	}
	return "";
}
function reportLinkFormatter(cellValue, options, rowObject){
    return "<a href='../report/report-"+cellValue+"'>Report</a>";
}

/*
 * This variable is modified only inside the reportCheckboxFormatter function 
 * and is used in select all function in context menu
 */
var _testRunIds = [];
function reportCheckboxFormatter(cellValue, options, rowObject){
	var i = _testRunIds.length;
	_testRunIds[i] = cellValue;
	
    return "<input type='checkbox' name='report_chk_"+i+"' id='report_chk_"+i+"'/>";
}
function subProjectFormatter(cellValue, options, rowObject){
	if(cellValue!=null){
		return "<a href='../project/display-"+escapeHTML(cellValue.path)+"'><img src='../images/workflow-icon-subproject.png'/> "+escapeHTML(cellValue.name)+"</a>";
	}
    return "";
}
function rowColorFormatter(cellValue, options, rowObject) {
    cellValue = cellValue.toLowerCase();
    var color = "";
    if (cellValue == "passed"){
        rowsToColor[rowsToColor.length] = {id:options.rowId,color:"#bfb"};
        color="#050";
    }
    else if (cellValue == "failed"){
        rowsToColor[rowsToColor.length] = {id:options.rowId,color:"#fbb"};
        color="#900";
    }
    else if (cellValue == "warning"){
        rowsToColor[rowsToColor.length] = {id:options.rowId,color:"#FFFA66"};
        color="#900";
    }
    return "<img src='../images/filter-"+cellValue+".png'/> <span style='margin:0;padding:0;color:"+color+";'>" + cellValue+"</span>";
}


var columnNames = [
    ' ', 'Report', 'Status', 'Test name', 'Sub-project', 'Designer', 'Runner', 'Start time', 'Suite name', 'Reason', 'Agent', 'Issue'
];

function getColumnId(index){
    for(var i=0;i<columnModel.length;i++){
        if(columnModel[i].index == index){
            return i;
        }
    }
    return null;
}
function gridShowColumn(index){
    columnModel[getColumnId(index)].shown = true;
    $("#list").showCol(index);
    return true;
}
function gridHideColumn(index){
    /* Checking if there are any other columns shown */
    var count = 0;
    for(var i=0;i<columnModel.length;i++){
        if(columnModel[i].shown){
            count++;
        }
    }
    if(count>1){
        columnModel[getColumnId(index)].shown = false;
        $("#list").hideCol(index);
        return true;
    }
    else return false;
}

function getReportAjaxUrl(){
	return "../report/browse-ajax"+generateRequest(document.forms.browseFilter);
}

$(document).ready(function(){ 
    $("#list").jqGrid({
        datatype: 'json',
        url:getReportAjaxUrl(),
        jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: true,
            cell: "cell",
            id: "id",
            userdata: "userdata",
            subgrid: {
                root:"rows", 
                repeatitems: true, 
                cell:"cell"
            }
        },
        colNames:columnNames,
        colModel :columnModel,
        pager: '#pager',
        autowidth:true,
        height:"100%",
        rowNum:-1,
        viewrecords: true,
        caption: 'Reports',
        loadComplete: function (data) {
        	$("#numberOfResults").html(data.numberOfResults);
        	
        	var pages = Math.floor(data.numberOfResults / data.displayRows) + 1;
        	if (pages>1 ) {
	        	var html = "<ul>";
	        	var startPages = Math.max(data.currentPage - 4, 1);
	        	var endPages = Math.min(data.currentPage + 4, pages);
	        	
	        	if ( data.currentPage>1) {
	        		html += "<li><a href='#' onclick='onPageClick(1);return false;'>&lt;&lt;</a></li>";
	        	}
	        	
	        	for ( var i=startPages; i<=endPages; i++) {
	        		html += "<li>";
	        		var className = "";
	        		if (i!=data.currentPage) {
	        			html += "<a href='#' onclick='onPageClick(" + i +");return false;'>"+i+"</a>";	
	        		}
	        		else {
	        			html += "<span>" + i + "</span>";
	        		}
	        		
	        		html += "</li>"
	         	}
	        	
	        	if ( data.currentPage<pages) {
	        		html += "<li><a href='#' onclick='onPageClick(" + pages + ");return false;'>&gt;&gt;</a></li>";
	        	}
	        	
	        	html += "</ul>";
	        	$("#pagination").html(html);
        	}
        },
        gridComplete: function () {
        	for (var i = 0; i < rowsToColor.length; i++) {
               var status = $("#" + rowsToColor[i].id).find("td").eq(7).html();
               $("#"+rowsToColor[i].id+" td").css("background-color", rowsToColor[i].color);
           }
    
           
           for(var i=0; i<columnModel.length; i++){
               if(!columnModel[i].shown){
                   $('#list').hideCol(columnModel[i].index);
               }
               $("#jqgh_list_"+columnModel[i].index).html(columnNames[i]);
               var items = {};
               if(i==0){
                   items["selectAll"] = {
                       name:"Select all",
                       type:"checkbox",
                       events:{
                           change: function(event){
                                for(var i=0;i<_testRunIds.length;i++){
                                    $("#report_chk_"+i).prop("checked", event.currentTarget.checked);
                                }
                           }
                       }
                   };
                   items["separator1"]="------------";
                   items["columns"] = {
                       name: "Columns",
                       items:(function f(){
                           var res = {};
                           for(var j=0;j<columnModel.length;j++) {
        
                               var disabled = false;
                               if(j==0) disabled = true;
                               res[""+columnModel[j].index] = {
                                   name:columnNames[j],
                                   type:"checkbox",
                                   selected:true,
                                   events: {
                                       change: function (event) {
                                           var index = $(this).attr('name').substr(19);
                                           if(event.currentTarget.checked){
                                               gridShowColumn(index);
                                               $("#list").setGridWidth($(window).width()-300);
                                           }
                                           else {
                                               if(!gridHideColumn(index)){
                                                   $(this).prop('checked', true);
                                               }
                                               else {
                                                   $("#list").setGridWidth($(window).width()-300);
                                               }
                                           }
                                           return false;
                                       }
                                   }
                               };
                           }
                           return res;
                           })()
                   };
               }
               else {
                   if(columnModel[i].sortCustomId>0){
	                   items["sortAsc"] ={
	                       name:"Sort asc", 
	                       icon:"sort-asc",
	                       callback: function(key, opt){
	                           var index = opt.selector.substr(11);
	                           //alert("Sort ascending on "+index);
	                           var id = getColumnId(index);
	                           onOrderByColumn(columnModel[id].sortCustomId,1);
	                       }
	                   }; 
	                   items["sortDesc"] ={
	                       name:"Sort desc",
	                       icon:"sort-desc", 
	                       callback: function(key, opt){
	                           var index = opt.selector.substr(11);
	                           var id = getColumnId(index);
	                           onOrderByColumn(columnModel[id].sortCustomId,-1);
	                       }
	                   };
	                   items["separator1"] ="-----------";
                   }
                   items["closeColumn"] ={
                       name:"Close column",
                       icon:"close-column", 
                       callback: function(key, opt){
                	       var index = opt.selector.substr(11);
                	       if(gridHideColumn(index)){
                	    	   $("input[name='context-menu-input-"+index+"']").prop("checked", false);
                	    	   $("#list").setGridWidth($(window).width()-300);
                	       }
                       }
                   };
               }
               
               $.contextMenu({
                   selector: "#jqgh_list_"+columnModel[i].index,
                   items:items
               });
           }
        },
        onSortCol: function (index, iCol, sortorder){
            $("#jqgh_list_"+index).contextMenu();
            return "stop";
        }
    }); 

    $(window).bind('resize', function() {
        $("#list").setGridWidth($(window).width()-300);
    }).trigger('resize');
}); 



function onRememberSelectedRuns(){
    var count = _testRunIds.length;
    var strIds = "";
    var bHasOne = false;
    var i=0;
    for(i=0;i<count;i++)    {
        if($("#report_chk_"+i).prop("checked")) {
            if(bHasOne)strIds+=",";
            strIds+= _testRunIds[i];
            bHasOne = true; 
        }
    }
    if(!bHasOne)    {
        alert("You haven't selected the test runs");
    } else {
        document.forms.rememberSelectedRuns.ids.value = strIds;
        document.forms.rememberSelectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
        document.forms.rememberSelectedRuns.submit();
    }
}

function onSaveChoosenRuns()
{
    var name = prompt("Enter the name for the saved run","");
    if(name!=null && name!="") {
        document.forms.saveCollectedRuns.saveName.value = name;
        document.forms.saveCollectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
        document.forms.saveCollectedRuns.submit();
    }
}
function onCollectedRunSelectAll(chkAll){
    var bChecked = chkAll.checked;
    var count = document.getElementById("numerOfCollectedRuns").value;
    
    var i=0;
    for(i=0;i<count;i++) {
        var chk = document.getElementById("chkCollectedRun_"+i);
        chk.checked = bChecked;
    }
}
function onDeleteSelectedCollectedRuns(){
    var count = document.getElementById("numerOfCollectedRuns").value;
    var strIds = "";
    var bHasOne = false;
    var i=0;
    for(i=0;i<count;i++) {
        var chk = document.getElementById("chkCollectedRun_"+i);
        if(chk.checked) {
            var runId = document.getElementById("collectedTestRunId_"+i).value;
            if(bHasOne)strIds+=",";
            strIds+= runId;
            bHasOne = true; 
        }
    }
    if(!bHasOne) {
        alert("You haven't selected the test runs");
    }
    else {
        document.forms.rememberSelectedRuns.removeIds.value = strIds;
        document.forms.rememberSelectedRuns.redirect.value = "../report/browse"+generateRequest(document.forms.browseFilter);
        document.forms.rememberSelectedRuns.submit();
    }
}


</script>