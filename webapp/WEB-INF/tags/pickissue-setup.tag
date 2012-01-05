<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%
/*
 * The following code should be placed only once per page and is used by pickissue button
 */
%>
<script language="javascript">
var _pickIssueButtonInputId = null;
var _pickIssueButtonTitleDiv = null;
var _pickIssueParentPanel = null;
var _pickIssueParentPanelWidth = null;
var _pickIssueParentPanelHeight = null;
function onPickIssueClick(pickIssueButtonInputId, pickIssueButtonTitleDiv, parentPanel, parentPanelWidth, parentPanelHeight)
{
    _pickIssueButtonInputId = pickIssueButtonInputId;
    _pickIssueButtonTitleDiv = pickIssueButtonTitleDiv;
    if(parentPanel!='')
    {
    	_pickIssueParentPanel = parentPanel;
    	_pickIssueParentPanelWidth = parentPanelWidth;
    	_pickIssueParentPanelHeight = parentPanelHeight;
    }
    updateIssues(1, "");
}
function updateIssues(page, name)
{
    var divIssues = document.getElementById("divPickIssueIssues");
    var divNavigation = document.getElementById("divPickIssueNavigation");
    var divLoading = document.getElementById("divPickIssueLoadingIcon");
    divIssues.innerHTML = "";
    divNavigation.innerHTML = "";
    divLoading.style.display = "block";

    if(_pickIssueParentPanel!=null)
    {
        closePopup(_pickIssueParentPanel);
    }
    showPopup("divPickIssue",400,400);
    
    dhtmlxAjax.post("../issue/ajax-issue-fetch", "page="+page+"&name="+escape(name), onAjaxIssueFetchResponse);
}
function onAjaxIssueFetchResponse(loader)
{
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result != "error")
    {
        var namePattern = document.getElementById("textPickIssueNamePattern").value;
        var result = obj.object;
        
        var pages = Math.floor(result.numberOfResults/result.limit) + 1;

        
        
        //Rendering the navigation panel
        var divLoading = document.getElementById("divPickIssueLoadingIcon");
        divLoading.style.display = "none";
        var divNavigation = document.getElementById("divPickIssueNavigation");
        var html = "";

        html+="<div class=\"small-description\">Found Results: "+result.numberOfResults+"</div>";
        html+="<table border=\"0\">";
        html+="<tr>";
        html+="<td width=\"15px\">";
        
        if(result.page>1)
        {
            var prevPage = result.page-1;
            html+="<a href=\"javascript:updateIssues("+prevPage+",'"+namePattern+"');\">&lt;</a>";
        }

        html+="</td>";
        html+="<td>";
        if(pages>0)
        {
            html+=result.page;
        }
        html+="</td>";
        html+="<td width=\"15px\">";
        if(result.page<pages)
        {
            var nextPage = result.page+1;
            html+="<a href=\"javascript:updateIssues("+nextPage+",'"+namePattern+"');\">&gt;</a>";
        }
        html+="</td>";
        html+="</tr>";
        html+="</table>";
        divNavigation.innerHTML = html;

        var issues = result.results;
        html="";
        html+="<table class=\"pick-table\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">";
        var rowOrder = "even";
        var issueName = "";
        for(var i=0;i<issues.length;i++)
        {
            
            if(i%2==0)
            {
                rowOrder="even";
            }
            else rowOrder="odd";
            html+="<tr class=\""+rowOrder+"\"><td class=\"pick-table\">";
            issueName = issues[i].name;
            
            if(issueName==null || issueName.length<1)
            {
                issueName = issues[i].link;
            }
            html+="<a class=\"pick-table\" href=\"javascript:onPickIssuePicked('"+escapeJSON(escapeHTML(issueName))+"',"+issues[i].id+");\">"+escapeHTML(issueName);
            if(issues[i].summary!=null && issues[i].summary!="")
            {
                var summary = issues[i].summary;
                if(summary.length>50)
                {
                    summary = summary.substring(0,50)+"...";
                }
                html+="<br/><span class=\"small-description\">"+escapeHTML(summary)+"</span>";
            }
            html+="</a>";
            html+="</td></tr>";
        }
        html+="</table>";
        var divIssues = document.getElementById("divPickIssueIssues");
        divIssues.innerHTML = html;
    }
    else alert(str);
}
function onPickIssuePicked(name, id)
{
	var issueLink = document.getElementById(_pickIssueButtonTitleDiv);
    issueLink.innerHTML = name;
    var input = document.getElementById(_pickIssueButtonInputId);
    input.value = id;
    closePopup("divPickIssue");

    if(_pickIssueParentPanel!=null)
    {
        showPopup(_pickIssueParentPanel,_pickIssueParentPanelWidth,_pickIssueParentPanelHeight);
    }
}
var pickIssue_timer = null;
function onPickIssueNameChange(control)
{
    if(pickIssue_timer!=null)
    {
        clearTimeout(pickIssue_timer);
    }
    updateIssues(1, control.value);
}

</script>
<div id="divPickIssue" class="popup" style="position:absolute;display:none;width:400px;height:500px;">
    <tag:panel title="Pick issue" 
                align="center"
                closeDivName="divPickIssue" 
                width="400px" height="400px">
        <input id="textPickIssueNamePattern" type="text" onchange="onPickIssueNameChange(this);"/>
        <div id="divPickIssueNavigation" style="width:360px;height:40px;">
        </div>
        <div style="overflow:auto;width:360px;height:330px;">
            <div id="divPickIssueLoadingIcon" style="display:none;">
                <img src="../images/loading.gif"/>
            </div>
            <div id="divPickIssueIssues">
            </div>
        </div>
    </tag:panel>
</div>

