<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%
/*
 * The following code should be placed only once per page and is used by pickuser button
 */
%>
<script>
var _pickUserButtonInputId = null;
var _pickUserButtonTitleDiv = null;
function onPickUserClick(pickUserButtonInputId, pickUserButtonTitleDiv)
{
	_pickUserButtonInputId = pickUserButtonInputId;
	_pickUserButtonTitleDiv = pickUserButtonTitleDiv;
    updateUsers(1, "");
}
function updateUsers(page, name)
{
    var divUsers = document.getElementById("divPickUserUsers");
    var divNavigation = document.getElementById("divPickUserNavigation");
    var divLoading = document.getElementById("divPickUserLoadingIcon");
    divUsers.innerHTML = "";
    divNavigation.innerHTML = "";
    divLoading.style.display = "block";
    showPopup("divPickUser",400,400);
    
    dhtmlxAjax.post("../user/ajax-user-fetch", "page="+page+"&name="+escape(name), onAjaxUserFetchResponse);
}
function onAjaxUserFetchResponse(loader)
{
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result != "error")
    {
        var namePattern = document.getElementById("textPickUserNamePattern").value;
        var result = obj.object;
        var pages = Math.round(result.numberOfResults/result.limit);

        //Rendering the navigation panel
        var divLoading = document.getElementById("divPickUserLoadingIcon");
        divLoading.style.display = "none";
        var divNavigation = document.getElementById("divPickUserNavigation");
        var html = "";

        html+="<div class=\"small-description\">Found Results: "+result.numberOfResults+"</div>";
        html+="<table border=\"0\">";
        html+="<tr>";
        html+="<td width=\"15px\">";
        
        if(result.page>1)
        {
            var prevPage = result.page-1;
            html+="<a href=\"javascript:updateUsers("+prevPage+",'"+namePattern+"');\">&lt;</a>";
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
            html+="<a href=\"javascript:updateUsers("+nextPage+","+namePattern+"');\">&gt;</a>";
        }
        html+="</td>";
        html+="</tr>";
        html+="</table>";
        divNavigation.innerHTML = html;

        var users = result.results;
        html="";
        html+="<table class=\"pick-table\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">";
        html+="<tr class=\"odd\"><td class=\"pick-table\">";
        html+="<a class=\"pick-table\" href=\"javascript:onPickUserPicked('Pick User...',null);\"><i>None</i></a>";
        html+="</td></tr>";
        var rowOrder = "even";
        for(var i=0;i<users.length;i++) {
            if(i%2==0) {
                rowOrder="even";
            }
            else rowOrder="odd";
            html+="<tr class=\""+rowOrder+"\"><td class=\"pick-table\">";
            html+="<a class=\"pick-table\" href=\"javascript:onPickUserPicked('"+escapeHTML(users[i].name)+"',"+users[i].id+");\">"+escapeHTML(users[i].name)+"</a>";
            html+="</td></tr>";
        }
        html+="</table>";
        var divUsers = document.getElementById("divPickUserUsers");
        divUsers.innerHTML = html;
    }
    else alert(str);
}
function onPickUserPicked(name, id) {
    var userLink = document.getElementById(_pickUserButtonTitleDiv);
    userLink.innerHTML = name;
    var input = document.getElementById(_pickUserButtonInputId);
    input.value = id;
    closePopup("divPickUser");
}
var pickUser_timer = null;
function onPickUserNameChange(control)
{
    if(pickUser_timer!=null)
    {
        clearTimeout(pickUser_timer);
    }
    updateUsers(1, control.value);
}

</script>
<div id="divPickUser" class="popup" style="position:absolute;display:none;width:400px;height:500px;">
    <tag:panel title="Pick user" 
                align="center"
                closeDivName="divPickUser" 
                width="400px" height="400px">
        <input id="textPickUserNamePattern" type="text" onchange="onPickUserNameChange(this);"/>
        <div id="divPickUserNavigation" style="width:360px;height:30px;">
        </div>
        <div style="overflow:auto;width:360px;height:330px;">
            <div id="divPickUserLoadingIcon" style="display:none;">
                <img src="../images/loading.gif"/>
            </div>
            <div id="divPickUserUsers">
            </div>
        </div>
    </tag:panel>
</div>

