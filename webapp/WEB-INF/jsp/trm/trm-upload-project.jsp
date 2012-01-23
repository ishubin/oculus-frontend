<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<div class="breadcrump" align="center">
    <a href="../test-run-manager/main">Test Run Manager</a>
    <img src="../images/breadcrump-arrow.png"/> 
    
    <a href="../test-run-manager/customize">Customize Settings</a>
    <img src="../images/breadcrump-arrow.png"/>
    Upload Project
</div>

<script language="javascript">
var pickBuild_projectId;
function onPickBuildClick(projectId)
{
	pickBuild_projectId = projectId;
    updateBuilds(1, projectId,"");
}
function updateBuilds(page, projectId, name)
{
    var divBuilds = document.getElementById("divPickBuildBuilds");
    var divNavigation = document.getElementById("divPickBuildNavigation");
    var divLoading = document.getElementById("divPickBuildLoadingIcon");
    divBuilds.innerHTML = "";
    divNavigation.innerHTML = "";
    divLoading.style.display = "block";
    showPopup("divPickBuild",400,400);
    
    dhtmlxAjax.post("../project/ajax-build-fetch", "projectId="+projectId+"&page="+page+"&name="+escape(name), onAjaxBuildFetchResponse);
}
function onAjaxBuildFetchResponse(loader)
{
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result != "error")
    {
        var namePattern = document.getElementById("textPickBuildNamePattern").value;
        var result = obj.object;
        var pages = Math.round(result.numberOfResults/result.limit);

        
        //Rendering the navigation panel
        var divLoading = document.getElementById("divPickBuildLoadingIcon");
        divLoading.style.display = "none";
        var divNavigation = document.getElementById("divPickBuildNavigation");
        var html = "";

        html+="<div class=\"small-description\">Found Results: "+result.numberOfResults+"</div>";
        html+="<table border=\"0\">";
        html+="<tr>";
        html+="<td width=\"15px\">";
        
        if(result.page>1)
        {
            var prevPage = result.page-1;
            html+="<a href=\"javascript:updateBuilds("+prevPage+","+pickBuild_projectId+",'"+namePattern+"');\">&lt;</a>";
        }

        html+="</td>";
        html+="<td>";
        html+=result.page;
        html+="</td>";
        html+="<td width=\"15px\">";
        if(result.page<pages)
        {
            var nextPage = result.page+1;
            html+="<a href=\"javascript:updateBuilds("+nextPage+","+pickBuild_projectId+",'"+namePattern+"');\">&gt;</a>";
        }
        html+="</td>";
        html+="</tr>";
        html+="</table>";
        divNavigation.innerHTML = html;

        var builds = result.results;
        html="<ul class=\"pick-button-list\">";
        html+="<li><a href=\"javascript:onPickBuildPicked('Current Version');\">Current Version</a></li>";
        for(var i=0;i<builds.length;i++)
        {
            html+="<li><a href=\"javascript:onPickBuildPicked('"+builds[i].name+"');\">"+builds[i].name+"</a></li>";
        }
        html+="</ul>";
        var divBuilds = document.getElementById("divPickBuildBuilds");
        divBuilds.innerHTML = html;
    }
    else alert(str);
}
function onPickBuildPicked(name)
{
    var buildLink = document.getElementById("linkPickBuild");
    buildLink.innerHTML = name;
    document.forms.formUploadProject.elements["version"].value = name;
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

<table border="0" cellspacing="0" cellpadding="2" width="100%">
    <tr>
        <td width="120px" valign="top">
            <c:choose>
                <c:when test="${project.icon!=null && project.icon!=''}">
                    <img src="../display/file?type=project-icon&projectId=${project.id}&object=${project.icon}"/>
                </c:when>
                <c:otherwise>
                    <img src="../images/project-no-icon.png"/>
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <td class="issue-table" colspan="2">
                            Project -
                            <b>${project.name}</b>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="even">
                        <td class="issue-table-left">
                            #ID:
                        </td>
                        <td class="issue-table">
                            ${project.id}
                        </td>
                    </tr>
                    <tr class="odd">
                        <td class="issue-table-left">
                            Name:
                        </td>
                        <td class="issue-table">
                            <tag:escape text="${project.name}"/>
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            Description:
                        </td>
                        <td class="issue-table">
                            <tag:bbcode-render>${project.description}</tag:bbcode-render>
                        </td>
                    </tr>
                    <tr class="odd">
                        <td class="issue-table-left">
                            SubProjects:
                        </td>
                        <td class="issue-table">
                            ${project.subprojectsCount}
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>
</table>

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

<br/>

<tag:panel align="center" title="Upload Project" width="400px">
    <form method="post" name="formUploadProject" enctype="multipart/form-data">
        <table border="0" align="center" cellpadding="10">
            <tr>
                <td colspan="2">
                    <b>Important!</b> The project jar file and libs folder should be placed in zip archive root
                </td>
            </tr>
            <tr>
                <td>
                    Build:
                </td>
                <td>
                    <input type="hidden" name="version" value="${uploadProject.version}"/>
                    <div style="width:134px;height:19px;margin-bottom:5px;">
                        <a class="pick-button" id="linkPickBuild" href="javascript:onPickBuildClick(${project.id});">${uploadProject.version}</a>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    ZIP File:
                </td>
                <td>
                    <input type="file" name="zippedFile"/>
                </td>
            </tr>
            <tr>
                <td align="center"  colspan="2">
                    <tag:submit value="Submit"></tag:submit>
                    <br/>
                    <div class="error">
                    	<tag:spring-form-error field="" command="uploadProject"></tag:spring-form-error>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel>



