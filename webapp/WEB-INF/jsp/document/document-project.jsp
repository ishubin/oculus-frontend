<%@page import="net.mindengine.oculus.frontend.domain.user.User"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
 
 
List<Map<String,Object>> customizationGroups = (List<Map<String,Object>>)pageContext.findAttribute("customizationGroups");
JSONValue jsonValue =  JSONMapper.toJSON(customizationGroups);
String js = jsonValue.render(false);
out.println("var testCaseCustomizationGroups = "+js+";");
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
    else if(response.result = "attachment-uploaded") {
        testCaseEditor.attachments[testCaseEditor.attachments.length] = response.object;
        testCaseEditor.renderPassive();
        document.forms.formAttachmentUpload.attachmentName.value = "";
        document.forms.formAttachmentUpload.attachmentDescription.value = "";
        document.forms.formAttachmentUpload.file.value = "";
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
            if(selectId.substring(1,3)=="tc"){
                //Clicked on test-case
                loadTestCase(selectId.substring(3));
            }
            else if(selectId.substring(1,3)=="fl"){
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
function tcmTableMoveRowUp(id){
	testCaseEditor.content = testCaseEditor.fetchContent();
	if(testCaseEditor.switchRows(id, id-1)){
		testCaseEditor.render();
	}
}
function tcmTableMoveRowDown(id){
    testCaseEditor.content = testCaseEditor.fetchContent();
    if(testCaseEditor.switchRows(id, id+1)){
        testCaseEditor.render();
    }
}
function tcmTableInsertRow(id){
    testCaseEditor.content = testCaseEditor.fetchContent();
	testCaseEditor.addRow(id+1, "","","");
	testCaseEditor.render();
}
function tcmTableRemoveRow(id){
	testCaseEditor.content = testCaseEditor.fetchContent();
    if(testCaseEditor.removeRow(id)){
	    testCaseEditor.render();
	}
}

//id - number of step, type - 0 (action), 1 (expected)
function focusTCTableStep(id, type){

	if(testCaseEditor.nicPanelFadeTimer!=null){
		clearTimeout(testCaseEditor.nicPanelFadeTimer);
	}
	var str = "Action";
	if(type==1)str = "Expected"
	var pos = $("#tcTable"+str+"_"+id).offset();
	var h = $("#tcTableNicPanel").height();
	$("#tcTableNicPanel").fadeIn('slow');
	$("#tcTableNicPanel").offset({top:pos.top-h, left:pos.left});
}
var TEST_CASE_MODE = {
    TABLE:{
        render: function(editor){
            var html = "";
            html+= "<div id='tcTableNicPanel' style='width:300px;display:none;position:absolute;z-index:10;top:0;left:0px;'></div>";
            html+= "<div class='ui-tabs ui-widget ui-widget-content ui-corner-all'><table width='100%' border='0' cellspacing='0' cellpadding='0' class='document-test-case-editor-table'>";
            html+= "<thead><tr><td width='100px' height='30px'>Step</td><td style='padding-left:20px;'>Action</td><td style='padding-left:20px;'>Expected</td></tr>";
            html+= "</thead><tbody>";
            
            for(var i=0;i<editor.content.length;i++){
            	var ops = "";
                if(i>0){
                    ops+="<li><a href='javascript:tcmTableMoveRowUp("+i+");' title='Move step up' class='icon-button'><img src='../images/document-test-case-menu-up.png'/></a></li>"
                }
                if(i<editor.content.length-1) {
                	ops+="<li><a href='javascript:tcmTableMoveRowDown("+i+");' title='Mode step down' class='icon-button'><img src='../images/document-test-case-menu-down.png'/></a></li>"
                }
                if(editor.content.length>1) {
                	ops+="<li><a href='javascript:tcmTableRemoveRow("+i+");' title='Remove step' class='icon-button'><img src='../images/document-test-case-menu-remove.png'/></a></li>"
                }
                ops+="<li><a href='javascript:tcmTableInsertRow("+i+");' title='Insert new step below' class='icon-button'><img src='../images/document-test-case-menu-insert.png'/></a></li>"
                
                html+="<tr>";
                html+="<td style='vertical-align:top;' height='100px'><div class='document-test-case-table-step'><b>"+(i+1)+"</b><ul>"+ops+"</ul></div></td>";
                html+="<td><div onclick='focusTCTableStep("+i+",0);' id='tcTableAction_"+i+"' style='z-index:9;font-size: 10pt; background-color: #fff; padding: 3px; border: 1px solid #ccc; width: 100%;height:100%;'></div>";
                html+="<td><div onclick='focusTCTableStep("+i+",1);' id='tcTableExpected_"+i+"' style='z-index:9;font-size: 10pt; background-color: #fff; padding: 3px; border: 1px solid #ccc; width: 100%;height:100%;'></div>";
                html+="</tr>";
            }
            html+="</tbody>";
            html+="<tfoot><tr><td colspan='3' height='10px'></td></tr></tfoot>";
            html+="</table></div>";
            $("#tabs2-tablemode").html(html);

            editor.myNicEditor = new nicEditor({iconsPath : '../scripts/nicedit/nicEditorIcons.gif'});
            editor.myNicEditor.setPanel('tcTableNicPanel');
            editor.myNicEditor.addEvent('blur', function(){
                editor.nicPanelFadeTimer = setTimeout(function(){
                    $("#tcTableNicPanel").fadeOut("fast");
                }, 200);
            });
            var width = Math.round(($(window).width()-370)/2);
            
            for(var i=0;i<editor.content.length;i++){
                $("#tcTableAction_"+i).width(width);
                $("#tcTableExpected_"+i).width(width);
                editor.myNicEditor.addInstance('tcTableAction_'+i);
                editor.myNicEditor.addInstance('tcTableExpected_'+i);
                nicEditors.findEditor("tcTableAction_"+i).setContent(editor.content[i].action);
                nicEditors.findEditor("tcTableExpected_"+i).setContent(editor.content[i].expected);
            }
        },
        fetchContent: function(editor){
            for(var i=0;i<editor.content.length;i++){
                editor.content[i].action = nicEditors.findEditor("tcTableAction_"+i).getContent();
                editor.content[i].expected = nicEditors.findEditor("tcTableExpected_"+i).getContent();
            }
            return editor.content;
        }
    },
    ADVANCED:{
        render: function (editor){
            var str = "";
            for(var i=0;i<editor.content.length; i++){
            	str+="@"+editor.content[i].action.replace(/<br>/g,'\n')+"\n";
                str+="^"+editor.content[i].expected.replace(/<br>/g,'\n');
                if(editor.content[i].comment!=null && editor.content[i].comment!=""){
                	str+="\n#"+editor.content[i].comment.replace(/<br>/g,'\n');
                }
                if(i<editor.content.length-1){
                    str+="\n";
                }
            }
            $("#tabs2-advanced textarea").val(str);
        },
        fetchContent: function (editor){
        	var str = $("#tabs2-advanced textarea").val();
            var lines = str.split(/\r\n|\r|\n/g);
            var content = [];
            
            if(lines!=null)  {
                var currentStepId = -1;
                var lineToAction = false;
                for(var i=0; i<lines.length; i++){
                    var firstSymbol = lines[i].substr(0,1);
                    if(firstSymbol == '@'){
                        lineToAction = true;
                        currentStepId = content.length;
                        content[currentStepId] =  {action:"",expected:"", comment:""};
                        content[currentStepId].action = lines[i].substr(1);
                    }
                    else if (firstSymbol == '^'){
                        if(!lineToAction){
                            currentStepId = content.length;
                            content[currentStepId] =  {action:"",expected:"", comment:""};
                        }
                        if(currentStepId>=0){
                            content[currentStepId].expected = lines[i].substr(1);
                        }
                        lineToAction=false;
                    }
                    else{
                        if(lineToAction){
                            if(currentStepId>=0){
                                content[currentStepId].action+= "\n"+lines[i];
                            }
                        }
                        else{
                            if(currentStepId>=0){
                                content[currentStepId].expected += "\n"+lines[i];
                            }
                        }
                    }
                }
            }
            for(var i=0;i<content.length; i++){
                content[i].action = content[i].action.replace(/\n/g,'<br>');
                content[i].expected = content[i].expected.replace(/\n/g,'<br>');
                content[i].comment = content[i].comment.replace(/\n/g,'<br>');
            }
            return content;
        }
    },
    PREVIEW:{
        render: function (editor){
            var html = "";
            html+= "<table class='document-test-case-table-preview' cellspacing='0'>";
            html+= "<thead><tr><td width='50px'>Step</td><td>Action</td><td>Expected</td></tr></thead>";
            html+= "<tbody>";
            for(var i=0;i<editor.content.length;i++){
                html+= "<tr>";
                html+= "<td class='dtctp-number'>"+(i+1)+"</td>";
                html+= "<td class='dtctp-content'>"+editor.content[i].action+"</td>";
                html+= "<td class='dtctp-content'>"+editor.content[i].expected+"</td>";
                html+= "</tr>";
            }
            html+= "</tbody></table>";
            $("#tabs2-preview").html(html);
        },
        fetchContent: function (editor){
            return editor.content; 
        }
    }
}

var testCaseEditor = {
    id: 0,
	name:"",
	description:"",
    content:[],
    mode: TEST_CASE_MODE.TABLE,

    setMode: function (mode){
        this.mode = mode;
    },
    switchMode: function(mode){
        this.content = this.fetchContent();
        this.mode = mode;
        this.render();
    },
    renderPassive: function (){
    	if(this.id>0){
            $("#testCaseSaveButton").val("Save");
            //Rendering attachments
            if(this.attachments!=null){
                var s="<ul class='document-attachment-list'>";
                for(var i=0; i<this.attachments.length; i++){
                    s+="<li><a href='../document/attachment-download?id="+this.attachments[i].id+"'>"+escapeHTML(this.attachments[i].name)+"</a> <span><a href='javascript:removeAttachment("+this.attachments[i].id+");'><img src='../images/workflow-icon-delete.png'/></a></span></li>";
                }
                s+="</ul>";
                $("#tcAttachmentsList").html(s);
            }
        }
        else {
            $("#testCaseSaveButton").val("Create");
        }
    },
    render: function (){
        this.renderPassive();
        this.mode.render(this);
    },
    addRow: function (id, action, expected, comment){
        var step = {action:action, expected: expected, comment: comment};
        if(id==null || id==-1) {
            this.content[this.content.length] = step;
        }
        else {
            this.content.splice(id, 0, step);
        }
    },
    removeRow: function (id){
        if(this.content.length>1){
            this.content.splice(id, 1);
            return true;
        }
        return false;
    },
    switchRows: function (id1, id2){
        if(id1!=id2 && id1>=0 && id1<this.content.length && id2>=0 && id2<this.content.length){
            var temp = this.content[id1];
            this.content[id1] = this.content[id2];
            this.content[id2] = temp;
            return true;
        }
        return false;
    },
    fetchContent: function(){
        return this.mode.fetchContent(this);
    },
    save: function (){
        this.name = $("#testCaseName").val();
        this.description = $("#testCaseDescription").val();
        if(this.id==0 && (this.name==null || this.name=="")){
            alert("You haven't specified the name of test-case");
            return;
        }
        
        $("#testCaseSavingLoadingLayout").show();

        this.content = this.fetchContent();

        if(this.id==null || this.id<=0){
            //Creating a new test case
            url = "../document/testcase-create";
        }
        else {
            //Updating the already created test-case
            url = "../document/testcase-edit";
        }

        var data = {
            id: this.id,
            name: this.name,
            description: this.description,
            projectId: this.projectId,
            folderId: this.folderId,
            content: this.content,
            contentSize: this.content.length
        };

        for(var j=0; j<testCaseCustomizationGroups.length; j++) {
            for(var i=0; i<testCaseCustomizationGroups[j].customizations.length; i++) {
                var c = testCaseCustomizationGroups[j].customizations[i];
                if(c.customization.type=="text" || c.customization.type=="assignee") {
                    data["customization_"+c.customization.id]=$("#customization_"+c.customization.id).val();
                }
                else if(c.customization.type=="list") {
                    var select = document.getElementById("customization_"+c.customization.id);
                    if(select.selectedIndex>=0) {
                        data["customization_"+c.customization.id]=select.options[select.selectedIndex].value;
                    }
                }
                else if(c.customization.type=="checkbox") {
                    var checkbox = document.getElementById("customization_"+c.customization.id);
                    if(checkbox.checked) {
                        data["customization_"+c.customization.id]="on";
                    }
                }
                else if(c.customization.type=="checklist") {
                    for(var k=0; k<c.possibleValues.length; k++) {
                        var pv = c.possibleValues[k];
                        var checkbox = document.getElementById("customization_"+c.customization.id+"_pv_"+pv.id);
                        if(checkbox.checked) {
                            data["customization_"+c.customization.id+"_pv_"+pv.id]="on";
                        }
                    }
                }
            }
        }
        
        $.post(url,data, function (data){
            $("#testCaseSavingLoadingLayout").hide();
            if(data.result=="created" || data.result=="edited"){
                testCaseEditor.id = data.object.id;
                $("#testCaseSaveButton").val("Save");
            }
            else if(data.result =="error"){
                alert("Error: "+data.object.text);
            }

            if(data.result=="created"){
            	//Inserting the new test-case item to the tree
            	if(data.object.folderId>0){
                    tree.insertNewChild(
                               "f"+data.object.folderId,
                               "dtc"+data.object.id,
                               data.object.name,
                               0,
                               "iconTestCase.png",
                               "iconTestCase.png",
                               "iconTestCase.png",
                               "UNCHECKED"
                               );
                    tree.setCheck("dtc"+data.object.id, false);
                }
                else {
                    tree.insertNewChild(
                               "p"+data.object.projectId,
                               "dtc"+data.object.id,
                               data.object.name,
                               0,
                               "iconTestCase.png",
                               "iconTestCase.png",
                               "iconTestCase.png",
                               "UNCHECKED"
                               );
                    tree.setCheck("dtc"+data.object.id, false);
                }
            }
            else if(data.result=="edited"){
            	tree.setItemText("dtc"+data.object.id,data.object.name);
            }
        }, "json");
    }
};


function newTestCase(){
	hideAllLayouts();
	testCaseEditor.id = 0;
	testCaseEditor.projectId = selectedProjectId;
    testCaseEditor.folderId = selectedFolderId;
    testCaseEditor.attachments = [];
    var selectId = tree.getSelectedItemId();
    if(selectId[0]=='p') {
        this.projectId = selectId.substring(1);
    }
    else if(selectId[0]=='f') {
       this.folderId = selectId.substring(1);
    }
    else {
        alert("You have to select a folder");
        return;
    }

    if(this.folderId>0){
        tree.openItem("f"+this.folderId);
    }
    else tree.openItem("p"+this.projectId);
	
	testCaseEditor.content = [];
	testCaseEditor.addRow(-1, "","","");
    

	
	$("#testCaseTabs").tabs("select", 0);
    $("#testCaseTabs").tabs("option","disabled", [2]);
	$('#testCaseEditor').show();

	$("#testCaseName").val("");
	$("#testCaseDescription").val("");
	
	setBreadCrump("<img src='../images/workflow-icon-testcase.png'/> New test-case");

	//Clearing all customization controls
	var c;
	for(var i=0;i<testCaseCustomizationGroups.length;i++){
		for(var j=0;j<testCaseCustomizationGroups[i].customizations.length;j++){
			c = testCaseCustomizationGroups[i].customizations[j].customization;
			if(c.type=="text"){
				$("#customization_"+c.id).val("");
			}
			else if(c.type=="checklist"){
				var pv;
				for(var k=0;k<testCaseCustomizationGroups[i].customizations[j].possibleValues.length;k++){
					pv = testCaseCustomizationGroups[i].customizations[j].possibleValues[k];
					$("#customization_"+c.id+"_pv_"+pv.id).prop("checked", false);
				}
			}
			else if(c.type=="checkbox"){
				$("#customization_"+c.id).prop("checked", false);
			}
			if(c.type=="list"){
	            $("#customization_"+c.id).val("");
	        }
			if(c.type=="assignee"){
				$("#customization_"+c.id).val("");
				$("#linkPickUsercustomization_"+c.id).html("Pick User...");
	        }
		}
	}

	testCaseEditor.render();
}
function loadTestCase(testCaseId){
	hideAllLayouts();
	showGlobalLoadingLayout("Loading Test-Case...");

	$.get('../document/testcase-display?id='+testCaseId,function (data){
		if(data.result =="test-case"){
			hideAllLayouts();
			testCaseEditor.id = data.object.document.id;
	        testCaseEditor.projectId = data.object.document.projectId;
	        testCaseEditor.folderId = data.object.document.folderId;
	        testCaseEditor.name = data.object.document.name;
	        testCaseEditor.description = data.object.document.description;
	        testCaseEditor.attachments = data.object.document.attachments; 
	        testCaseEditor.content = [];
	        for(var i=0;i<data.object.steps.length;i++){
			    testCaseEditor.addRow(i, data.object.steps[i].action,data.object.steps[i].expected,data.object.steps[i].comment);
	        }
	        
	        $("#testCaseTabs").tabs("select", 0);
	        $("#testCaseTabs").tabs("option","disabled", []);
	        $('#testCaseEditor').show();
	        $("#testCaseName").val(testCaseEditor.name);
	        $("#testCaseDescription").val(testCaseEditor.description);
	        setBreadCrump("<img src='../images/workflow-icon-testcase.png'/> "+testCaseEditor.name);

	        //Setting all the customization parameters
	        var customizationGroups = data.object.customizationGroups;
	        for(var j=0; j<customizationGroups.length; j++){
	            for(var i=0; i<customizationGroups[j].customizations.length; i++){
	                var c = customizationGroups[j].customizations[i];
	                if(c.customization.type == "text"){
	                    var control = document.getElementById("customization_"+c.customization.id);
	                    if(control!=null)control.value = c.customization.value;
	                }
	                else if(c.customization.type=="assignee"){
	                    var control = document.getElementById("customization_"+c.customization.id);
	                    if(control!=null)control.value = c.customization.value;

	                    var link = document.getElementById("linkPickUsercustomization_"+c.customization.id);
	                    if(link!=null && c.assignedUser!=null){
	                        link.innerHTML = escapeHTML(c.assignedUser.name);
	                    }
	                    else if(link!=null){
		                    link.innerHTML = "Pick User...";
	                    }
	                }
	                else if(c.customization.type=="list"){
	                    var control = document.getElementById("customization_"+c.customization.id);
	                    if(control!=null){
	                        var bFound = false;
	                        for(var ci = 0; ci<control.length&&!bFound; ci++){
	                            if(control.options[ci].value==c.customization.value){
	                                bFound = true;
	                                control.selectedIndex = ci;
	                            }
	                        }
	                    }
	                }
	                else if(c.customization.type=="checkbox"){
	                    var control = document.getElementById("customization_"+c.customization.id);
	                    if(control!=null) {
	                        if(c.customization.value=="true") {
	                            control.checked = true;
	                        }
	                        else control.checked = false;
	                    }
	                }
	                else if(c.customization.type=="checklist") {
	                    
	                    for(var k=0; k<c.possibleValues.length; k++) {
	                        var pv = c.possibleValues[k];
	                        var control = document.getElementById("customization_"+c.customization.id+"_pv_"+pv.id);
	                        if(pv.isSet==true) {
	                            if(control!=null)control.checked = true;                            
	                        }
	                        else if(control!=null)control.checked = false;
	                    }   
	                }           
	            }
	        }
	        
	        testCaseEditor.render();
		}
		else if(data.result =="error"){
			alert("Error: "+data.object.text);
		}
		
	}, "json");
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

$(document).ready(function (){
	$("#testCaseTabs").tabs();
	$("#testCaseStepsTabs").tabs({
		select: function (event, ui){
		    if(ui.index==0){
			    testCaseEditor.switchMode(TEST_CASE_MODE.TABLE);
		    }
		    else if(ui.index==1){
                testCaseEditor.switchMode(TEST_CASE_MODE.ADVANCED);
            }
		    else if(ui.index==2){
                testCaseEditor.switchMode(TEST_CASE_MODE.PREVIEW);
            }
	    }
    });
});

function onTestCaseUploadAttachmentSubmit() {
	document.forms.formAttachmentUpload.documentId.value = testCaseEditor.id;
	return true;
}
function removeAttachment(id){
	if(confirm("Are you sure you want to remove this attachment?")){
		$.post("../document/attachment-delete",{id:id}, function (data){
			if(data.result == "deleted-attachment"){
		        testCaseEditor.attachments = data.object;
		        testCaseEditor.renderPassive();
		    }
		    else if(data.result == "error"){
		        alert("Error: "+data.text);
		    }
		},"json");
	}
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
    <a class="jbutton" href="javascript:newTestCase();"><img src="../images/workflow-icon-testcase.png"/> Create Test-Case</a>  
    <a class="jbutton" href="javascript:newFile();"><img src="../dhtmlxTree/imgs/csh_dhx_skyblue/leaf.gif"/> Upload File</a>
    
</div>
<script>
$(".jbutton").button();
</script>
<div id="loadingLayout"class="document-layout" style="display:none;">
    <img src="../images/loading.gif"/> <span>Loading...</span>
</div>
<div id="testCaseEditor" class="document-layout" style="display:none;">
    <div id="testCaseTabs">
	    <ul>
	        <li><a href="#tabs-details">Details</a></li>
	        <li><a href="#tabs-settings"><img src="../images/workflow-icon-settings.png"/> Settings</a></li>
	        <li><a href="#tabs-attachments"><img src="../images/workflow-icon-attachment.png"/> Attachments</a></li>
	    </ul>
	    <div id="tabs-details">
	        Name:<br/>
	        <tag:edit-field-simple name="testCaseName" id="testCaseName" width="100%"></tag:edit-field-simple>
	        <br/>
	        Description:<br/>
	        <tag:textarea-simple name="testCaseDescription" id="testCaseDescription" value="" style="width:100%;" cssClass="custom-edit-text" rows="5"/>
	    </div>
	    <div id="tabs-settings">
	        <c:forEach items="${customizationGroups}" var="cgs">
	            <c:forEach items="${cgs.customizations}" var="c">
	                <b><tag:escape text="${c.customization.name}"/></b><br/><tag:escape text="${c.customization.description}"></tag:escape>
                    <tag:customization-edit customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-edit>
                    <br/>
                    <br/>        
                </c:forEach>
	        </c:forEach>
	    </div>
	    <div id="tabs-attachments">
	        <div id="tcAttachmentsList">
	        </div>
	        <tag:disclosure id="attachmentUpload" title="Upload attachment">
		        <form name="formAttachmentUpload" 
		                action="../document/attachment-upload" 
		                method="post" 
		                target="iframeFileUploader"
		                enctype="multipart/form-data" onsubmit="return onTestCaseUploadAttachmentSubmit();">
		            <input name="documentId" type="hidden"/>
	                Name:<br/>
	                <input id="attachmentName" class="custom-edit-text" name="name" style="width:100%;"/><br/><br/>
	                Description:<br/>
	                <textarea id="attachmentDescription" name="description" class="custom-edit-text" rows="7" style="width:100%"></textarea><br/>
	                
	                File:<br/>
	                <input name="file" type="file"/>
	                
	                <tag:submit value="Upload" ></tag:submit>
		       </form>
	       </tag:disclosure>
	    </div>
	</div>
	<div id="testCaseControls" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
        <tag:submit value="Create" id="testCaseSaveButton" onclick="testCaseEditor.save(); return false;"></tag:submit>
        <span id="testCaseSavingLoadingLayout" style="display:none;"><img src="../images/loading-small.gif"/> Saving...</span>
    </div>
    <div id="testCaseStepsTabs">
	    <ul>
            <li><a href="#tabs2-tablemode">Table Mode</a></li>
            <li><a href="#tabs2-advanced">Advanced Mode</a></li>
            <li><a href="#tabs2-preview">Preview</a></li>
        </ul>
        <div id="tabs2-tablemode">
            
        </div>
        <div id="tabs2-advanced">
            <textarea rows="10" style="width:100%;" class="custom-edit-text"></textarea>
        </div>
        <div id="tabs2-preview">
            
        </div>
	</div>
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