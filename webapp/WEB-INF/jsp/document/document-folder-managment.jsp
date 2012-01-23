<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<script>
var tree = null;
var currentProjectId = 0;
var selectedFolderId = 0;
var selectedProjectId = 0;
function loadFolderTree(projectId)
{
	currentProjectId = projectId;
    tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
    tree.enableDragAndDrop(0);
    tree.enableCheckBoxes(true, true);
    tree.enableTreeLines(true);
    tree.setXMLAutoLoading("../document/display-folders");
    //tree.setXMLAutoLoadingBehaviour("function");
    
    var d = new Date();
    var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
    tree.loadXML("../document/display-folders?projectId="+projectId+"&tmstp="+str);

    tree.attachEvent("onSelect", onTreeElementSelect);
}
function onNewFolder()
{
	var selectId = tree.getSelectedItemId();
	if(selectId!=null && selectId!="")
	{
		document.getElementById("folderName").value = "";
		showPopup("divCreateFolderDialog",300,100);
		document.getElementById("folderName").focus();
	}
	else 
	{
	    alert("You haven't selected any folder");
	}
}
function onCreateFolderSubmit()
{
	var selectId = tree.getSelectedItemId();
	var params="";
    if(selectId!="")
    {
    	params="projectId="+selectedProjectId;
        if(selectId[0]=='f')
        {
            params+="&parentId="+selectId.substring(1);
        }
        else params+="&parentId=0";

        var name = document.getElementById("folderName").value;
        params+="&name="+escape(name);
        closePopup("divCreateFolderDialog");
    }
	
	dhtmlxAjax.post("../document/folder-create", params, onCreateFolderResponse);
	hideAllLayouts();
	showGlobalLoadingLayout(null);
}
function onCreateFolderResponse(loader)
{
	hideAllLayouts();
	var str = loader.xmlDoc.responseText;
	var obj = eval("("+str+")");

	if(obj.result == "added"){
		if(obj.parentId>0){
            tree.insertNewChild(
                       "f"+obj.parentId,
                       "f"+obj.id,
                       obj.name,
                       0,
                       "folderClosed.gif",
                       "folderOpen.gif",
                       "folderClosed.gif",
                       "CHECKED"
                       );
        }
		else{
            tree.insertNewChild(
                       "p"+obj.projectId,
                       "f"+obj.id,
                       obj.name,
                       0,
                       "folderClosed.gif",
                       "folderOpen.gif",
                       "folderClosed.gif",
                       "CHECKED"
                       );
        }
		tree.disableCheckbox("f"+obj.id,true); 
         
	}
	else if(obj.result == "error"){
        alert("error: "+obj.type+"\n"+obj.text);
    }
}
function onDeleteFolder(){
	var selectId = tree.getSelectedItemId();
	if(selectId!=null && selectId!=""){
	    if( selectId.indexOf("f")===0){
	        var d = document.getElementById("divDFD_info");
	        d.innerHTML = "Are you sure you want to delete \"<b>"+escapeHTML(tree.getItemText(selectId))+"</b>\" folder and all documents in it?";
	        showPopup("divDeleteFolderDialog", 300, 150);
	    }
	    else if(selectId.indexOf("dtc")===0){
	    	var d = document.getElementById("divDFD_info");
            d.innerHTML = "Are you sure you want to delete \"<b>"+escapeHTML(tree.getItemText(selectId))+"</b>\" test-case?";
            showPopup("divDeleteFolderDialog", 300, 150);
	    }
	    else if(selectId.indexOf("dfl")===0){
            var d = document.getElementById("divDFD_info");
            d.innerHTML = "Are you sure you want to delete \"<b>"+escapeHTML(tree.getItemText(selectId))+"</b>\" file?";
            showPopup("divDeleteFolderDialog", 300, 150);
        }
	}
}
function onDeleteFolderSubmit()
{
	hideAllLayouts();
    showGlobalLoadingLayout(null);
    closePopup("divDeleteFolderDialog");
	var selectId = tree.getSelectedItemId();
    if(selectId!="") {
        if(selectId[0]=='p') {
            //It is not allowed to delete a project here
            return;
        }
        else if(selectId[0]=='f') {
        	$.post("../document/folder-delete", {id:selectId.substring(1)}, function (data){
            	hideAllLayouts();
                if(data.result == "deleted"){
                    tree.deleteItem("f"+data.object.id,true);
                }
                else if(data.result=="error"){
                    alert(data.text);
                }
            }, 'json');
        }
        else if(selectId[0]=='d') {
        	$.post("../document/document-delete", {id:selectId.substring(3)}, function (data){
                hideAllLayouts();
                if(data.result == "deleted-testcase"){
                    tree.deleteItem("dtc"+data.object.id,true);
                }
                if(data.result == "deleted-file"){
                    tree.deleteItem("dfl"+data.object.id,true);
                }
                else if(data.result=="error"){
                    alert(data.text);
                }
            }, 'json');
        }
    }
}

function onRenameFolder() {
	var selectId = tree.getSelectedItemId();
    if(selectId!=null && selectId!="" && selectId.indexOf("f")===0) {	
    	var d = document.getElementById("divRFD_info");
        var str = escapeHTML(tree.getItemText(selectId));
        d.innerHTML = "<input type=\"text\" width=\"100%\" id=\"renameFolderInput\" value=\""+str+"\"/>";
        showPopup("divRenameFolderDialog", 300, 150);
    }
    else {
        alert("You haven't selected any folder");
    }
}
function onRenameFolderSubmit() {
	hideAllLayouts();
    showGlobalLoadingLayout(null);
    var selectId = tree.getSelectedItemId();
    var params="";
    if(selectId!="") {
        if(selectId[0]=='p') {
            return;
        }
        else if(selectId[0]=='f') {
            params="id="+selectId.substring(1);
        }

        var name = document.getElementById("renameFolderInput").value;
        params+="&name="+escape(name);
    }
    
    dhtmlxAjax.post("../document/folder-rename", params, onRenameFolderResponse);
    
    closePopup("divRenameFolderDialog");
}
function onRenameFolderResponse(loader){
	hideAllLayouts();
	var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result == "renamed") {
        var id = "f"+obj.object.id;
        tree.setItemText(id,obj.object.name,tree.getItemTooltip(id));
    }
    else if(obj.result=="error") {
        alert(obj.text);
    }
}

var treeMove = null;
function onMoveDocuments(){
	var selectedItems = tree.getAllChecked();
    if(selectedItems!=null && selectedItems!=""){
	
		if(treeMove==null){
			treeMove = new dhtmlXTreeObject("treeboxMoveDocuments", "100%", "100%", 0);
			treeMove.setSkin('dhx_skyblue');
		    treeMove.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
		    treeMove.enableDragAndDrop(0);
		    treeMove.enableTreeLines(true);
		    treeMove.setXMLAutoLoading("../document/display-folders?onlyFolders=true");
		    
		    var d = new Date();
		    var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
		    treeMove.loadXML("../document/display-folders?onlyFolders=true&projectId="+currentProjectId+"&tmstp="+str);
	
		    treeMove.attachEvent("onSelect", onTreeMoveDocumentsSelect);
		}
		
	    showPopup("divMoveDocuments", 500, 450);
    }
}
function onTreeMoveDocumentsSelect(){
	
}

function onMoveDocumentsSubmit(){
	var selectDestId = treeMove.getSelectedItemId();
	tree.openItem(selectDestId);
	if(selectDestId!=null && selectDestId!=""){ 
	
		var selectedItems = tree.getAllChecked();
	    if(selectedItems!=null && selectedItems!=""){
	        
	        dhtmlxAjax.post("../document/documents-move", "documents="+selectedItems+"&destination="+selectDestId, onMoveDocumentsResponse);
	        hideAllLayouts();
	        showGlobalLoadingLayout(null);
	        closePopup("divMoveDocuments");
	    }
	}
	else alert("You haven't selected the destination");
}

function onMoveDocumentsResponse(loader) {
	hideAllLayouts();
    var str = loader.xmlDoc.responseText;
    var obj = eval("("+str+")");

    if(obj.result == "moved"){
        var response = obj.object;
        for(var i=0;i<response.documents.length; i++){
            var itemText = tree.getItemText(response.documents[i]);
            tree.deleteItem(response.documents[i], false);

            if(tree.getItemText(response.documents[i]==null)){
	            if(response.documents[i].indexOf("dfl") === 0){
	            	tree.insertNewChild(
	                        response.destination,
	                        response.documents[i],
	                        itemText,
	                        0,
	                        "leaf.gif",
	                        "leaf.gif",
	                        "leaf.gif",
	                        "CHECKED"
	                        );
	            	tree.setCheck(response.documents[i], false);
	            	
	            } 
	            else{
	            	tree.insertNewChild(
	                        response.destination,
	                        response.documents[i],
	                        itemText,
	                        0,
	                        "iconTestCase.png",
	                        "iconTestCase.png",
	                        "iconTestCase.png",
	                        "CHECKED"
	                        );
	                tree.setCheck(response.documents[i], false);
	            }
            }
        }
    }
    else if(obj.result=="error")
    {
        alert(obj.text);
    }
    hideAllLayouts();
}

</script>

<div id="divCreateFolderDialog" style="display:none;">
    <tag:panel align="center"  width="300px" height="100px" title="Create Folder" closeDivName="divCreateFolderDialog">
		<table align="center" width="100%">
	        <tr>
	            <td colspan="2" align="left">
	                Name:<br/>
	                <tag:edit-field-simple name="folderName" id="folderName" width="100%"></tag:edit-field-simple>
	            </td>
	        </tr>
	        <tr>
	            <td><tag:submit value="Create" onclick="onCreateFolderSubmit();"></tag:submit></td>
	            <td><tag:submit value="Cancel" onclick="closePopup('divCreateFolderDialog');" width="70px"></tag:submit></td>
	        </tr>
	    </table>
    </tag:panel>
</div>

<div id="divDeleteFolderDialog" style="display:none;">
    <tag:panel align="center"  width="300px" height="150px" title="Delete Folder" closeDivName="divDeleteFolderDialog">
	    <table align="center">
	        <tr>
	            <td colspan="2">
	                 <div id="divDFD_info"></div>
	            </td>
	        </tr>
	        <tr>
	            <td><tag:submit value="Yes" onclick="onDeleteFolderSubmit();" width="70px"></tag:submit></td>
	            <td><tag:submit value="Cancel" onclick="closePopup('divDeleteFolderDialog');" width="70px"></tag:submit></td>
	        </tr>
	    </table>
	</tag:panel>
</div>

<div id="divRenameFolderDialog" style="display:none;">
    <tag:panel align="center"  width="300px" height="150px" title="Rename Folder" closeDivName="divRenameFolderDialog">
	    <table align="center">
	        <tr>
	            <td colspan="2" align="left">
	                Name:<br/>
	                <div id="divRFD_info"></div>
	            </td>
	        </tr>
	        <tr>
	            <td><tag:submit value="Rename" onclick="onRenameFolderSubmit();" width="70px"></tag:submit></td>
	            <td><tag:submit value="Cancel" onclick="closePopup('divRenameFolderDialog');" width="70px"></tag:submit></td>
	        </tr>
	    </table>
	</tag:panel>
</div>

<div id="divMoveDocuments" style="display:none;">
    <tag:panel align="center"  width="500px" height="450px" title="Rename Folder" closeDivName="divMoveDocuments">
        <table align="center">
            <tr>
                <td colspan="2" align="left">
                    <tag:panel-inner align="left" width="100%" height="100%">
		               <div id="treeboxMoveDocuments" style="width:450px;height:400px;overflow:auto;"></div>
		            </tag:panel-inner>
                </td>
            </tr>
            <tr>
                <td><tag:submit value="Move" onclick="onMoveDocumentsSubmit();" width="70px"></tag:submit></td>
                <td><tag:submit value="Cancel" onclick="closePopup('divMoveDocuments');" width="70px"></tag:submit></td>
            </tr>
        </table>
    </tag:panel>
</div>

<div id="treeNavigationPanel" style="width:250px;height:400px;">
    <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
        <c:if test="${user.hasPermissions.document_managment == true}">
	        <tr>
	            <td height="30px" valign="top">
	                <div style="display:${user!=null?'block':'none'};height:30px;">
	                
	                    <a class="icon-button icon-button-white" href="javascript:onNewFolder();" title="Create Folder"><img src='../images/btn-folder-create.png'/> </a>
	                    <a class="icon-button icon-button-white" href="javascript:onDeleteFolder();" title="Delete Folder"><img src='../images/btn-folder-delete.png'/> </a>
	                    <a class="icon-button icon-button-white" href="javascript:onRenameFolder();" title="Rename Folder"><img src='../images/btn-folder-rename.png'/> </a>
	                    <a class="icon-button icon-button-white" href="javascript:onMoveDocuments();" title="Move documents"><img src='../images/workflow-icon-shared-task.png'/> </a>
			        </div>
	            </td>
	        </tr>
	        <tr>
	           <td valign="top">
	               <div id="treeNavigationScrollable" style="width:247px;height:300px;overflow:auto;background:white;border:1px solid #aaa;">
				       <div id="treeboxbox_tree" style="width:100%;height:100%;overflow:scroll;background:none;"></div>
				   </div>
	           </td>
	        </tr>
	    </c:if>
    </table>
</div>
<script>
$(document).ready(function(){
	$("#treeNavigationPanel").stickySidebar({speed: 100, padding: 30, constrain: true});
	$(window).bind('resize', function() {
        $("#treeNavigationPanel").height($(window).height()-110);
        $("#treeNavigationScrollable").height($(window).height()-160);
    }).trigger('resize');
});
</script>
