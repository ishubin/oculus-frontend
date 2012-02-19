<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%
/*
 * The following code should be placed only once per page and is used by pickbutton button
 */
%>
<script>
var _pickBuild_controlId = null;
var _pickBuild_projectId = null;
function onPickBuildClick(controlId, projectId) {
    _pickBuild_controlId = controlId;
    pickBuild_projectId = projectId;

    updateBuilds(1, projectId,"");
}
function updateBuilds(page, projectId, name) {
	$("#divPickBuildBuilds").html("");
	$("#divPickBuildNavigation").html("");
	$("#divPickBuildLoadingIcon").show();
	
    showPopup("divPickBuild",400,400);
    dhtmlxAjax.post("../project/ajax-build-fetch", "projectId="+projectId+"&page="+page+"&name="+escape(name), onAjaxBuildFetchResponse);
}

function onAjaxBuildFetchResponse(loader) {
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result != "error") {
        var namePattern = document.getElementById("textPickBuildNamePattern").value;
        var result = obj.object;
        var pages = Math.round(result.numberOfResults/result.limit);

        //Rendering the navigation panel
        $("#divPickBuildLoadingIcon").hide();
        
        var divNavigation = document.getElementById("divPickBuildNavigation");
        var html = "";

        html+="<div class=\"small-description\">Found Results: "+result.numberOfResults+"</div>";
        html+="<table border=\"0\">";
        html+="<tr>";
        html+="<td width=\"15px\">";
        
        if(result.page>1)
        {
            var prevPage = result.page-1;
            html+="<a href=\"javascript:updateBuilds("+prevPage+",'"+namePattern+"');\">&lt;</a>";
        }

        html+="</td>";
        html+="<td>";
        if(pages>1)
        {
            html+=result.page;
        }
        html+="</td>";
        html+="<td width=\"15px\">";
        if(result.page<pages)
        {
            var nextPage = result.page+1;
            html+="<a href=\"javascript:updateBuilds("+nextPage+","+namePattern+"');\">&gt;</a>";
        }
        html+="</td>";
        html+="</tr>";
        html+="</table>";
        divNavigation.innerHTML = html;

        var builds = result.results;
        html="";
        html+="<table class=\"pick-table\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">";
        html+="<tr class=\"odd\"><td class=\"pick-table\">";
        html+="<a class=\"pick-table\" href=\"javascript:onPickBuildPicked('Current Version');\"><i>Current Version</i></a>";
        html+="</td></tr>";
        var rowOrder = "even";
        for(var i=0;i<builds.length;i++) {
            if(i%2==0) {
                rowOrder="even";
            }
            else rowOrder="odd";
            html+="<tr class=\""+rowOrder+"\"><td class=\"pick-table\">";
            html+="<a class=\"pick-table\" href=\"javascript:onPickBuildPicked('"+escapeHTML(builds[i].name)+"');\">"+escapeHTML(builds[i].name)+"</a>";
            html+="</td></tr>";
        }
        html+="</table>";
        var divBuilds = document.getElementById("divPickBuildBuilds");
        divBuilds.innerHTML = html;
    }
    else alert(str);
}
function onPickBuildPicked(name)
{
    $("#linkPickBuild"+_pickBuild_controlId).html(name);
    $("#"+_pickBuild_controlId).val(name);
    closePopup("divPickBuild");
}
var pickBuild_timer = null;
function onPickBuildNameChange(control)
{
    if(pickBuild_timer!=null)
    {
        clearTimeout(pickBuild_timer);
    }
    updateBuilds(1, pickBuild_projectId, control.value);
}
</script>
<div id="divPickBuild" class="popup" style="position:absolute;display:none;width:400px;height:500px;">
    <tag:panel title="Pick build" 
                align="center"
                closeDivName="divPickBuild" 
                width="400px" height="400px">
        <input id="textPickBuildNamePattern" type="text" onchange="onPickBuildNameChange(this);"/>
        <div id="divPickBuildNavigation" style="width:360px;height:30px;">
        </div>
        <div style="overflow:auto;width:360px;height:330px;">
            <div id="divPickBuildLoadingIcon" style="display:none;">
                <img src="../images/loading.gif"/>
            </div>
            <div id="divPickBuildBuilds">
            </div>
        </div>
    </tag:panel>
</div>

