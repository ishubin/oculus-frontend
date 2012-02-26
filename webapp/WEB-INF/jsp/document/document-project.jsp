<%@page import="net.mindengine.oculus.frontend.domain.user.User"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.sdicons.json.model.JSONValue"%>
<%@page import="com.sdicons.json.mapper.JSONMapper"%><div class="breadcrump">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a> 
    <img src="../images/breadcrump-arrow.png"/> 
    <img src="../images/workflow-icon-library.png"/> Document Library
    <div id="breadCrumpLast" class="document-layout" style="display:inline;">
        <img src="../images/breadcrump-arrow.png"/>
        <span></span>
    </div>
</div>
<tag:pickuser-setup></tag:pickuser-setup>

<div style="display:none;">
    <iframe name="iframeFileUploader" onload="onIframeFileUploaderLoad();"></iframe>
</div>

<script>
<%
/*
 * Exporting the customization parameters to JSON format so we could use those in the test-case layout
 */

User user = (User)pageContext.findAttribute("user");
if(user!=null){
    out.println("var userId = "+user.getId());
}
else{
    out.println("var userId = 0;");
}
%>


var iframeUploaderCounter=0;
function onIframeFileUploaderLoad() {
    if(iframeUploaderCounter==0) {
        iframeUploaderCounter++;
        return;
    }
    //Handling the file uploading

    var responseText = frames.iframeFileUploader.document.forms.formResponse.response.value;    
    var response = eval("("+responseText+")");
    if(response.result=="error") {
        alert(response.type+":\n"+response.text);
    }
    else if(response.result=="file-uploaded") {
        var id = "dfl"+response.object.id;
        var pid = "";
        if(response.object.folderId<1) {
            pid = "p"+response.object.projectId;
        }
        else pid = "f"+response.object.folderId;
        var name = response.object.name;
        if(response.object.typeExtended!=null && response.object.typeExtended!="") {
            name+="."+response.object.typeExtended;
        }
        tree.insertNewChild(
                pid,
                id,
                name,
                0,
                "leaf.gif",
                "leaf.gif",
                "leaf.gif",
                "CHECKED"
                );
        tree.setCheck(id, false);
        hideAllLayouts();
    }
    else if(response.result=="file-edited") {
        var id = "dfl"+response.object.id;
        var name = response.object.name;
        if(response.object.typeExtended!=null && response.object.typeExtended!="") {
            name+="."+response.object.typeExtended;
        }
        tree.setItemText(id,name);
    }    
}


function onTreeElementSelect(){
	hideAllLayouts();
	window.scrollTo(0,0);
    var selectId = tree.getSelectedItemId();
    selectedFolderId = 0;
    selectedProjectId = currentProjectId;
    if(selectId!=null && selectId!=""){
        if(selectId[0]=='p'){
        	selectedFolderId = 0;
        	selectedProjectId = selectId.substring(1);
            if(userId>0){
                showOperationsForFolder();              
            }
        }
        else if(selectId[0]=='f'){
            selectedFolderId = selectId.substring(1);
            if(userId>0){
                showOperationsForFolder();              
            }
        }
        else if(selectId[0]=='d'){
            if(selectId.substring(1,3)=="fl"){
                //Clicked on file
                loadFileDocument(selectId.substring(3));
            }
        }
    }
}

function hideAllLayouts(){
    $('.document-layout').hide();	
}
function showOperationsForFolder(){
	hideAllLayouts();
	$('#folderOperations').show();
}


function showGlobalLoadingLayout(text){
	if(text!=null){
		$('#loadingLayout span').html(text);
	}
	else $('#loadingLayout span').html('Loading...');
	$('#loadingLayout').show();
}
function hideGlobalLoadingLayout(){
	$('#loadingLayout').hide();
}

function setBreadCrump(text){
	$('#breadCrumpLast span').html(text);
	$('#breadCrumpLast').show();
}

function newFile(){
	hideAllLayouts();
	$("#fileEditor").show();
	setBreadCrump("<img src='../dhtmlxTree/imgs/csh_dhx_skyblue/leaf.gif'/> New file");
	$("#divFileDownload").html("");
	if(selectedFolderId>0){
        tree.openItem("f"+selectedFolderId);
    }
    else tree.openItem("p"+selectedProjectId);

	document.forms.formFileUpload.projectId.value = selectedProjectId;
	document.forms.formFileUpload.folderId.value = selectedFolderId;
	document.forms.formFileUpload.name.value = "";
	document.forms.formFileUpload.description.value = "";
	document.forms.formFileUpload.file.value = "";
	document.forms.formFileUpload.id.value = "";
}
function loadFileDocument(id) {
    hideAllLayouts();
    showGlobalLoadingLayout("Loading file...");
    $.post("../document/file-display", {id:id}, function(response){
    	hideAllLayouts();
    	if(response.result == "file") {
            var file = response.object;

            $("#fileEditor").show();
            setBreadCrump("<img src='../dhtmlxTree/imgs/csh_dhx_skyblue/leaf.gif'/> New file");
            $("#divFileDownload").html("<a href=\"../document/file-download?id="+file.id+"\">Download File</a>");
            
            document.forms.formFileUpload.action = "../document/file-edit";
            document.forms.formFileUpload.folderId.value = file.folderId;
            document.forms.formFileUpload.projectId.value = file.projectId;
            document.forms.formFileUpload.id.value = file.id;
            document.forms.formFileUpload.name.value = file.name;
            document.forms.formFileUpload.file.value = "";
            document.forms.formFileUpload.description.value = file.description;
        }
        else if(response.result=="error") {
            alert("Error: "+response.text);
        }
    },"json");
}
</script>

<div id="folderOperations" class="document-layout" style="display:none;">
    <tag:submit value="Upload File" onclick="newFile();"></tag:submit>
</div>
<div id="loadingLayout"class="document-layout" style="display:none;">
    <img src="../images/loading.gif"/> <span>Loading...</span>
</div>

<div id="fileEditor" class="document-layout" style="display:none;">
    <div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	    <form   name="formFileUpload" 
	            action="../document/file-create" 
	            method="post" 
	            target="iframeFileUploader"
	            enctype="multipart/form-data">
	        
	        <input type="hidden" name="id" />    
	        <input type="hidden" name="projectId" />
	        <input type="hidden" name="folderId" />
	        Name:
	        <input type="text" name="name" class="custom-edit-text" value="" style="width:100%;"/><br/><br/>
	        Description:
	        <textarea name="description" class="custom-edit-text" rows="6" style="width:100%;"></textarea>
	        <br/><br/>
	        File:
	        <input type="file" name="file"/>
	        <br/>
	        <br/>
	        <div id="divFileDownload">
	        </div>
	        <br/>
	        <tag:submit value="Upload"></tag:submit>
	    </form>
    </div>
</div>