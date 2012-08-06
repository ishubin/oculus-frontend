<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="net.mindengine.oculus.frontend.domain.project.ProjectSearchFilter"%>
<%@page import="net.mindengine.oculus.frontend.domain.db.BrowseResult"%>
<%@page import="net.mindengine.oculus.frontend.domain.project.Project"%>
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

Search Results: <b>${projects.numberOfResults}</b><br/><br/>

<%
//Preparing the Page Limit drop-down list model
{
    
    ProjectSearchFilter filter = (ProjectSearchFilter)pageContext.findAttribute("searchFilter");
    
    List list = new ArrayList();
    
    for(int i=0;i<ProjectSearchFilter.PAGE_LIMITS.length;i++)
    {
        Map map = new HashMap();
        map.put("label",ProjectSearchFilter.PAGE_LIMITS[i]);
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
    numberOfResults="${projects.numberOfResults}" 
    onPageScript="onPageClick" 
    onPageLimitScript="onPageLimitChange" 
    currentPage="${searchFilter.pageOffset}" 
    pageLimit="${searchFilter.pageLimit}"></tag:pagination>

<%
BrowseResult<Project> result = (BrowseResult<Project>)pageContext.findAttribute("projects");

Collection<Project> projects = result.getResults();

StringBuffer jsonstr = new StringBuffer("{\"total\":1, \"page\":1, \"records\":"+projects.size()+",\"rows\":[");

boolean comma = false;
int i=0;
for(Project project:projects){
    if(comma)jsonstr.append(",");
    comma = true;
    jsonstr.append("{");
    jsonstr.append("\"id\":"+i+",\"cell\":[{");
    jsonstr.append("\"path\":\""+StringEscapeUtils.escapeJavaScript(project.getPath())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(project.getName()));
    jsonstr.append("\"},{");
    jsonstr.append("\"path\":\""+StringEscapeUtils.escapeJavaScript(project.getParentPath())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(project.getParentName()));
    jsonstr.append("\"},\"");
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
    jsonstr.append(StringEscapeUtils.escapeJavaScript(sdf.format(project.getDate())));
    jsonstr.append("\",{");
    jsonstr.append("\"login\":\""+StringEscapeUtils.escapeJavaScript(project.getAuthorLogin())+"\", \"name\":\""+StringEscapeUtils.escapeJavaScript(project.getAuthorName()));
    jsonstr.append("\"}]}");
    i++;
}

jsonstr.append("]}");

%>
<br/><br/>
<table id="list"></table>

<script type="text/javascript">

var jsonstr = "<%=StringEscapeUtils.escapeJavaScript(jsonstr.toString())%>";

var columnNames = ['Name','Parent project', 'Created','User'];
var columnModel = [ 
    {name:'name', index:'name', sortable:true, sortCustomId:1, formatter:nameFormatter}, 
    {name:'parentProject', index:'parentProject',sortCustomId:2, sortable:true, width:90, formatter: parentFormatter}, 
    {name:'created', index:'created', sortable:true,sortCustomId:3, width:80}, 
    {name:'user', index:'user', sortable:true,sortCustomId:4, width:80, formatter:userFormatter}
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
        return "<a href='../project/display-"+cellValue.path+"'><img src='../images/workflow-icon-subproject.png'/> "+escapeHTML(cellValue.name)+"</a>";
    }
    return "";
}
function parentFormatter(cellValue, options, rowObject){
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

