<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<script>
<%
String testContentJson = (String) pageContext.findAttribute("testContentJson");
out.println("var testSavedContent = "+testContentJson+";");
%>
$(function(){
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
	
	testCaseEditor.content = [];
	
	if(testSavedContent!=null && testSavedContent.steps!=null) {
		testCaseEditor.content = testSavedContent.steps;
	}
	
	if(testCaseEditor.content.length==0) {
		testCaseEditor.addRow(-1, "","","");
	}
	testCaseEditor.render();
	
});

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
            var width = Math.round(($(window).width()-720)/2);
            
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
    mode: TEST_CASE_MODE.TABLE,

    setMode: function (mode){
        this.mode = mode;
    },
    switchMode: function(mode){
        this.content = this.fetchContent();
        this.mode = mode;
        this.render();
    },
    render: function (){
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
    fetchContent: function() {
        return this.mode.fetchContent(this);
    },
    exportContent: function() {
    	var content = this.fetchContent();
    	
    	var xml = "<test-case>";
    	xml += "<steps>";
    	for(var i=0; i<content.length; i++) {
    		xml += "<step>";
    		xml += "<action>" + escapeHTML(content[i].action) + "</action>";
    		xml += "<expected>" + escapeHTML(content[i].expected) + "</expected>";
    		xml += "<comment>" + escapeHTML(content[i].comment) + "</comment>";
    		xml += "</step>";
    	}
    	xml += "</steps>";
    	xml += "</test-case>";
    	return xml;
    }
};


</script>

<div id="testCaseStepsTabs">
    <ul>
        <li><a href="#tabs2-tablemode">Table Mode</a></li>
        <li><a href="#tabs2-advanced">Advanced Mode</a></li>
        <li><a href="#tabs2-preview">Preview</a></li>
    </ul>
    <div id="tabs2-tablemode">
        
    </div>
    <div id="tabs2-advanced">
        <textarea rows="20" style="width:100%;" class="custom-edit-text"></textarea>
    </div>
    <div id="tabs2-preview">
        
    </div>
</div>