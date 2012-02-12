<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page import="net.mindengine.oculus.frontend.domain.db.BrowseResult"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmTask"%>
<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.frontend.domain.user.User"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.text.SimpleDateFormat"%><jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<%@page import="net.mindengine.oculus.frontend.domain.trm.TaskSearchFilter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%><script language="javascript">
function onPageLimitChange(select)
{
    document.forms.searchFilter.pageLimit.value = select.options[select.selectedIndex].value;
    document.forms.searchFilter.pageOffset.value = 1;
    redirectSearchFilterForm(document.forms.searchFilter);
}
function onPageClick(id)
{
    document.forms.searchFilter.pageOffset.value = id;
    redirectSearchFilterForm(document.forms.searchFilter);
}
function onOrderByColumn(columnId,orderType)
{
    document.forms.searchFilter.orderByColumnId.value = columnId;
    document.forms.searchFilter.orderDirection.value = orderType;
    redirectSearchFilterForm(document.forms.searchFilter);
}
</script>

Search Results: <b>${tasks.numberOfResults}</b><br/><br/>

<%
//Preparing the Page Limit drop-down list model
{
    
    TaskSearchFilter filter = (TaskSearchFilter)pageContext.findAttribute("searchFilter");
    
    List list = new ArrayList();
    
    for(int i=0;i<TaskSearchFilter.PAGE_LIMITS.length;i++)
    {
        Map map = new HashMap();
        map.put("label",TaskSearchFilter.PAGE_LIMITS[i]);
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
<tag:pagination pageLimitArray="${searchFilter.pageLimitArray}" 
    numberOfResults="${tasks.numberOfResults}" 
    onPageScript="onPageClick" 
    onPageLimitScript="onPageLimitChange" 
    currentPage="${searchFilter.pageOffset}" 
    pageLimit="${searchFilter.pageLimit}"></tag:pagination>

<%
BrowseResult<TrmTask> result = (BrowseResult<TrmTask>)pageContext.findAttribute("tasks");

Collection<TrmTask> tasks = result.getResults();

StringBuffer jsonstr = new StringBuffer("{\"total\":1, \"page\":1, \"records\":"+tasks.size()+",\"rows\":[");

boolean comma = false;
int i=0;

User user = (User)pageContext.findAttribute("user");

for(TrmTask task : tasks){
    if(comma)jsonstr.append(",");
    comma = true;
    jsonstr.append("{");
    jsonstr.append("\"id\":"+i+",\"cell\":[{");
    jsonstr.append("\"id\":"+task.getId()+", \"name\":\""+StringEscapeUtils.escapeJavaScript(task.getName())+"\", \"belongs\":");
    if(user.getId().equals(task.getUserId())){
        jsonstr.append("true");
    }
    else jsonstr.append("false");
    jsonstr.append("},\"");
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    jsonstr.append(StringEscapeUtils.escapeJavaScript(sdf.format(task.getDate())));
    jsonstr.append("\",{");
    jsonstr.append("\"login\":\""+StringEscapeUtils.escapeJavaScript(task.getUserLogin())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(task.getUserName()));
    jsonstr.append("\"}]}");
    i++;
}

jsonstr.append("]}");

%>
<br/><br/>
<table id="list"></table>

<script type="text/javascript">

var jsonstr = "<%=StringEscapeUtils.escapeJavaScript(jsonstr.toString())%>";

var columnNames = ['Name', 'Created','User'];
var columnModel = [ 
    {name:'name', index:'name', sortable:true, sortCustomId:1, formatter:nameFormatter},  
    {name:'created', index:'created', sortable:true,sortCustomId:2, width:80}, 
    {name:'user', index:'user', sortable:true,sortCustomId:3, width:80, formatter:userFormatter}
];
function getColumnId(index){
    for(var i=0;i<columnModel.length;i++){
        if(columnModel[i].index == index){
            return i;
        }
    }
    return null;
}

$(function(){ 
  $("#list").jqGrid({
    datastr:jsonstr,
    datatype: 'jsonstring',
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
    autowidth:true,
    height:"100%",
    rowNum:-1,
    viewrecords: true,
    colNames:columnNames,
    colModel :columnModel,
    gridview: true,
    caption: 'Sub-Projects',
    gridComplete: function (){
        for(var i=0; i<columnModel.length; i++){
            $("#jqgh_list_"+columnModel[i].index).html(columnNames[i]);
            $.contextMenu({
                selector: "#jqgh_list_"+columnModel[i].index,
                items:{
                    "sortAsc":{
                        name:"Sort asc", 
                        icon:"sort-asc",
                        callback: function(key, opt){
                            var index = opt.selector.substr(11);
                            //alert("Sort ascending on "+index);
                            var id = getColumnId(index);
                            onOrderByColumn(columnModel[id].sortCustomId,1);
                        }
                    },
                    "sortDesc":{
                        name:"Sort desc", 
                        icon:"sort-desc",
                        callback: function(key, opt){
                            var index = opt.selector.substr(11);
                            //alert("Sort ascending on "+index);
                            var id = getColumnId(index);
                            onOrderByColumn(columnModel[id].sortCustomId,-1);
                        }
                    }
                }
            });
        }
    },
    onSortCol: function (index, iCol, sortorder){
        $("#jqgh_list_"+index).contextMenu();
        return "stop";
    }
  }); 
}); 

function nameFormatter(cellValue, options, rowObject){
    if(cellValue!=null){
        if(cellValue.belongs){
        	return "<a href='../grid/edit-task?id="+cellValue.id+"'><img src='../images/workflow-icon-task.png'/> "+escapeHTML(cellValue.name)+"</a>";
        }
        else{
        	return "<a href='../grid/shared-task?id="+cellValue.id+"'><img src='../images/workflow-icon-task.png'/> "+escapeHTML(cellValue.name)+"</a>";
        }
    }
    return "";
}
function userFormatter(cellValue, options, rowObject){
    if(cellValue!=null){
        return "<a href='../user/profile-"+cellValue.login+"'><img src='../images/workflow-icon-user.png'/> "+escapeHTML(cellValue.name)+"</a>";
    }
    return "";
}
</script>

    
    