<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="net.mindengine.oculus.frontend.domain.document.DocumentSearchFilter"%>
<%@page import="net.mindengine.oculus.frontend.domain.db.BrowseResult"%>
<%@page import="net.mindengine.oculus.frontend.domain.document.Document"%>
<%@page import="java.util.Collection"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.text.SimpleDateFormat"%><script language="javascript">
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

<tag:submit onclick="onExportXLS();" value="Export XLS"></tag:submit>

Search Results: <b>${testCases.numberOfResults}</b><br/><br/>

<%
//Preparing the Page Limit drop-down list model
{
    
    DocumentSearchFilter filter = (DocumentSearchFilter)pageContext.findAttribute("searchFilter");
    
    List list = new ArrayList();
    
    for(int i=0;i<DocumentSearchFilter.PAGE_LIMITS.length;i++)
    {
        Map map = new HashMap();
        map.put("label",DocumentSearchFilter.PAGE_LIMITS[i]);
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
    numberOfResults="${testCases.numberOfResults}" 
    onPageScript="onPageClick" 
    onPageLimitScript="onPageLimitChange" 
    currentPage="${searchFilter.pageOffset}" 
    pageLimit="${searchFilter.pageLimit}"></tag:pagination>

<%
BrowseResult<Document> result = (BrowseResult<Document>)pageContext.findAttribute("testCases");

Collection<Document> documents = result.getResults();

StringBuffer jsonstr = new StringBuffer("{\"total\":1, \"page\":1, \"records\":"+documents.size()+",\"rows\":[");

boolean comma = false;
int i=0;
for(Document document: documents){
    if(comma)jsonstr.append(",");
    comma = true;
    jsonstr.append("{");
    jsonstr.append("\"id\":"+i+",\"cell\":[{");
    jsonstr.append("\"id\":"+document.getId()+", \"name\":\""+StringEscapeUtils.escapeJavaScript(document.getName()));
    jsonstr.append("\", \"projectPath\":\""+StringEscapeUtils.escapeJavaScript(document.getProjectPath())+"\"},");
    
    if(document.getParentProjectPath()!=null){
        jsonstr.append("{\"path\":\""+StringEscapeUtils.escapeJavaScript(document.getParentProjectPath())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(document.getParentProjectName()));
        jsonstr.append("\"},");
    }
    else jsonstr.append("null,");
    
    if(document.getProjectPath()!=null){
        jsonstr.append("{\"path\":\""+StringEscapeUtils.escapeJavaScript(document.getProjectPath())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(document.getProjectName()));
        jsonstr.append("\"},\"");
    }
    else jsonstr.append("null,");
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    jsonstr.append(StringEscapeUtils.escapeJavaScript(sdf.format(document.getDate())));
    jsonstr.append("\",{");
    jsonstr.append("\"login\":\""+StringEscapeUtils.escapeJavaScript(document.getAuthorLogin())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(document.getAuthorName()));
    jsonstr.append("\"}]}");
    i++;
}

jsonstr.append("]}");

%>
<br/><br/>
<table id="list"></table>

<script type="text/javascript">

var jsonstr = "<%=StringEscapeUtils.escapeJavaScript(jsonstr.toString())%>";

var columnNames = ['Test Name','Project', 'Sub-Project', 'Created','Designer'];
var columnModel = [ 
    {name:'name', index:'name', sortable:true, sortCustomId:1, formatter:nameFormatter}, 
    {name:'parentProject', index:'parentProject',sortCustomId:2, sortable:true, width:90, formatter: projectFormatter},
    {name:'subProject', index:'subProject',sortCustomId:3, sortable:true, width:90, formatter: subProjectFormatter}, 
    {name:'created', index:'created', sortable:true,sortCustomId:4, width:80}, 
    {name:'user', index:'user', sortable:true,sortCustomId:5, width:80, formatter:userFormatter}
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
    caption: 'Test-cases',
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
        return "<a href='../document/project-"+cellValue.projectPath+"?testCaseId="+cellValue.id+"'><img src='../images/workflow-icon-testcase.png'/> "+escapeHTML(cellValue.name)+"</a>";
    }
    return "";
}
function subProjectFormatter(cellValue, options, rowObject){
    if(cellValue!=null){
        return "<a href='../project/display-"+cellValue.path+"'><img src='../images/workflow-icon-subproject.png'/> "+escapeHTML(cellValue.name)+"</a>";
    }
    return "";
}
function projectFormatter(cellValue, options, rowObject){
    if(cellValue!=null){
        return "<a href='../project/display-"+cellValue.path+"'><img src='../images/workflow-icon-project.png'/> "+escapeHTML(cellValue.name)+"</a>";
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