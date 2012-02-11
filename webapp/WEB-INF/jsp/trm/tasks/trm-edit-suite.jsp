<%@page import="com.sdicons.json.model.JSONValue"%>
<%@page import="com.sdicons.json.mapper.JSONMapper"%>
<%@page import="net.mindengine.oculus.frontend.domain.test.Test"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@page import="java.util.Enumeration"%>
<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../test-run-manager/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/>
        <tag:escape text="${task.name}"/>
    </a>
    <c:if test="${suite.groupId>0 }">
        <img src="../images/breadcrump-arrow.png"/> 
        <a href="../test-run-manager/edit-task?id=${task.id}&groupId=${suite.groupId}">
            <img src="../images/workflow-icon-test-group.png"/>
            <tag:escape text="${suite.groupName}"/>
        </a>
    </c:if>
    
    <img src="../images/breadcrump-arrow.png"/>
    <img src="../images/workflow-icon-suite.png"/>
    <tag:escape text="${suite.name}"/>
</div>
    
           
<form name="editSuiteForm" method="post" onsubmit="return submitEditSuiteForm();">
    <tag:submit name="Submit" value="Save" ></tag:submit><br/>    
     <tag:panel align="left" title="Suite Description" width="100%" disclosure="true" closed="false" id="suiteDescriptionPanel"> 
	                <table width="100%" border="0" cellpadding="0" cellspacing="0">
	                    <tbody>
	                        <tr>
	                            <td class="small-description">Name:</td>
	                        </tr>
	                        <tr>
	                            <td>
	                                <tag:edit-field-simple name="suiteName" id="editTaskName" width="100%" value="${suite.name}"></tag:edit-field-simple>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td  class="small-description">Description:</td>
	                        </tr>
	                        <tr>
	                            <td>
	                                <tag:textarea-simple name="suiteDescription" style="width:100%;" rows="5" value="${suite.description}"/>
	                                <input type="hidden" name="suiteData" value=""/>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td  class="small-description">Group:</td>
	                        </tr>
	                        <tr>
	                            <td>
	                                <select name="groupId">
	                                    <option value="0" style="color:gray;">No group...</option>
	                                    <c:forEach items="${groups}" var="g">
	                                        <option value="${g.id}" <c:if test="${suite.groupId==g.id}">selected="selected"</c:if>><tag:escape text="${g.name}"/></option>
	                                    </c:forEach>
	                                </select>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </tag:panel><br/>
                <tag:panel align="left" title="Tests" width="100%" disclosure="true" closed="false" id="testsPanel"> 
                            <script>
                            
                            //Array of tests which are belong to the current test suite
                            var myTests = new Array();
                            
                            function findMaxCustomId(tests, max) {
                            	if(tests!=null){
                                    for(var i=0;i<tests.length;i++){
                                    	if(tests[i].tests!=null) {
                                    		max = findMaxCustomId(tests[i].tests, max);
                                    	}
                                    	
                                    	if(!isNaN(tests[i].customId)) {
                                    		cId = parseInt(tests[i].customId);
                                    		if(max < cId){
                                                max = cId;
                                            }	
                                    	}
                                    }
                                }
                            	return max;
                            }
                            /**
                            * Fetching the unique custom id so it wouldn't interfier with existing tests
                            */
                            function getUniqueCustomTestId(){
                                var uid = findMaxCustomId(myTests, 0);
                                
                                //Setting the uid as the maxixum customId value from exising tests
                                
                                //Incrementing it so it becomes unique
                                uid++;
                                return uid.toString();
                            }
                            
                            function addSelectedTestsToMySuite()
                            {
                                var str = "";
                                for(var i=0;i<checkedTests.length;i++)
                                {
                                    if(i>0)str+=",";
                                    str+=checkedTests[i];
                                }
                                dhtmlxAjax.post("../test/ajax-fetch", "ids="+str, onAjaxTestFetchResponse);
                            }
                            function onAjaxTestFetchResponse(loader) {
                                var str = loader.xmlDoc.responseText;
                                var obj = eval("("+str+")");
                        
                                if(obj.result != "error") {
                                    var tests = obj.object;
                                    if(tests!=null) {
                                        gatherAllParameterValues();
                                        for(var i=0; i<tests.length; i++) {
                                            tests[i].customId = getUniqueCustomTestId();
                                            //As it is a new test need to set the depends and value fields as null
                                            for(var j=0;j<tests[i].inputParameters.length;j++)
                                            {
                                                tests[i].inputParameters[j].depends = null;
                                                tests[i].inputParameters[j].value = tests[i].inputParameters[j].defaultValue;
                                            }
                                            
                                            if(_droppedTestType=='last') {
                                            	myTests[myTests.length] = tests[i];
                                            }
                                            else if(_droppedTestType=='test') {
                                            	var positionLevelTo = findTestIdByCustomId(_droppedTestCustomId);
                                            	myTests.splice(positionLevelTo+i,0,tests[i]);
                                            }
                                            else if(_droppedTestType=='lastInGroup') {
                                            	//Adding test to last position in specified test group
                                            	var testGroupTo = findTestByCustomId(_droppedTestCustomId);
                                            	testGroupTo.tests.splice(testGroupTo.tests.length, 0, tests[i]);
                                            }
                                            else if(_droppedTestType=='child') {
                                            	//Adding test before child test of specified test group
                                            	var testGroupTo = findParentForCustomId(_droppedTestCustomId);
                                            	var positionLevelTo = findTestIdByCustomId(_droppedTestCustomId);
                                            	testGroupTo.tests.splice(positionLevelTo, 0, tests[i]);
                                            }
                                        }
                                        rerenderTests();
                                    }
                                }
                                else alert(str);
                            }
                            function addTestToSuite(test) {
                                var testId = myTests.length;
                                myTests[testId] = test;
                                if(myTests[testId].customId==null) {
                                	myTests[testId].customId = getUniqueCustomTestId();
                                }
                                
                                var table = document.getElementById("myTestsTable");
                                var tr = document.createElement("tr");
                                var td = document.createElement("td");
                                td.innerHTML = renderTest(myTests[testId]);
                                tr.appendChild(td);
                                table.appendChild(tr);
                                myTests[testId].tableRowElement = tr;
                        
                                addTestToShortTestsLayout(myTests[testId]);
                            }
                            function addTestToShortTestsLayout(test) {
                                var table = document.getElementById("myTestsShortTable");
                                var tr = document.createElement("tr");
                                var td = document.createElement("td");
                                td.innerHTML = renderShortTestLayout(test);
                                tr.appendChild(td);
                                table.appendChild(tr);
                                test.tableShortRowElement = tr;
                            }
                            function renderShortTestLayout(test) {
                                var str="";

                                var icon = "iconTest.png";
                                if(test.tests!=null) {
                                	icon = "iconTestGroupCustom.png";
                                }
                                str+= "<a class=\"disclosure\" href=\"javascript:onDisclosurePanelClick('divTestShortContent"+test.customId+"','divIconShortTC"+test.customId+"');\">";
                                str+= "<div id=\"divIconShortTC"+test.customId+"\" class=\"disclosure-icon-close\" style=\"float:left;\"></div>";
                                str+= "<img style=\"float: left;\" src=\"../images/"+ icon +"\">";
                                
                                str+= "<b>"+escapeHTML(test.name)+"</b>";
                                str+= "</a>";
                                str+= "<div id=\"divTestShortContent"+test.customId+"\" style=\"display:none;padding-left:10px\">";
                                if(test.inputParameters!=null && test.inputParameters.length>0) {
                                    str+= "Input parameters:";
                                    str+= "<ul class='grid-test-short-layout-parameters'>";
                                    for(var i=0;i<test.inputParameters.length;i++)
                                    {
                                        str+="<li><a href=\"javascript:confirmLinkParameter("+test.inputParameters[i].id+",'"+test.customId+"');\">"+escapeHTML(test.inputParameters[i].name)+"</a></li>";
                                    }
                                    str+= "</ul>";  
                                }
                                if(test.outputParameters!=null && test.outputParameters.length>0) {
                                    str+= "Output parameters:";
                                    str+= "<ul>";
                                    for(var i=0;i<test.outputParameters.length;i++)
                                    {
                                        str+="<li><a href=\"javascript:confirmLinkParameter("+test.outputParameters[i].id+",'"+test.customId+"');\">"+escapeHTML(test.outputParameters[i].name)+"</a></li>";
                                    }
                                    str+= "</ul>";  
                                }
                                if((test.inputParameters==null || test.inputParameters.length==0)
                                        &&(test.outputParameters==null || test.outputParameters.length==0)
                                        && test.tests==null) {
                                    str+= "There are no parameters in this test";
                                }
                                if(test.tests!=null) {
                                	for(var i=0;i< test.tests.length; i++) {
                                		str+= renderShortTestLayout(test.tests[i]);
                                	}	
                                }
                                str+= "</div>";
                                
                                return str;
                            }
                            function findTestIdByCustomIdInArray(tests, customId) {
                            	for(var i=0;i<tests.length;i++) {
                                    if(tests[i].customId == customId) return i;
                                    if(tests[i].tests!=null) {
                                    	var id = findTestIdByCustomIdInArray(tests[i].tests, customId);
                                    	if(id>=0) {
                                    		return id;
                                    	}
                                    }                                    
                                }
                                return -1;
                            }
                            function findTestIdByCustomId(customId) {
                                return findTestIdByCustomIdInArray(myTests, customId);
                            }
                            
                            function findParentForCustomId(customId){
                            	for(var i=0;i<myTests.length;i++) {
                            		if(myTests[i].tests!=null) {
                            			for(var j=0; j<myTests[i].tests.length; j++) {
                            				if(myTests[i].tests[j].customId == customId) {
                            					return myTests[i];
                            				}
                            			}
                            		}
                            		
                            	}
                            	return null;
                            }
                            
                            function findTestByCustomIdInArray(tests, customId) {
                            	for(var i=0;i<tests.length;i++) {
                            		if(tests[i].customId == customId) {
                            			return tests[i];
                            		}
                            		if(tests[i].tests!=null) {
                            			var test = findTestByCustomIdInArray(tests[i].tests, customId);
                            			if(test!=null) {
                            				return test;
                            			}
                            		}
                                }
                            	return null;
                            }
                            function findTestByCustomId(customId) {
                                return findTestByCustomIdInArray(myTests, customId);
                            }
                            
                            function removeParameterDependenciesForTest(testCustomId, tests) {
                            	if (tests==null) {
                            		removeParameterDependenciesForTest(testCustomId, myTests);
                            	}
                            	else {
                            		//Checking if there was a depency set to this test
                            		for(var i=0; i<tests.length; i++) {
	                                    if(tests[i].inputParameters!=null) {
	                                        for(var j=0; j<tests[i].inputParameters.length; j++) {
	                                        	if(tests[i].inputParameters[j].depends!=null) {
	                                        		if(tests[i].inputParameters[j].depends.testCustomId == testCustomId) {
	                                                    tests[i].inputParameters[j].depends = null;
	                                                }
	                                            }
	                                        }
	                                    }
	                                    if(tests[i].tests!=null) {
	                                    	removeParameterDependenciesForTest(testCustomId, tests[i].tests);
	                                    }
	                                }
                            	}
                            }
                            function removeTest(testCustomId, tests) {
                            	if(tests==null) {
                            		removeTest(testCustomId, myTests);
                            		rerenderTests();
                            	}
                            	else {
	                            	var id = findTestIdByCustomId(testCustomId);
	                            	for(var i=0; i<tests.length; i++) {
	                            		if(tests[i].customId == testCustomId) {
	                            			
	                            			//Removing parameter dependencies for subtests 
	                            			if(tests[i].tests!=null) {
	                            				for(var k=0;k < tests[i].tests.length; k++) {
	                            					removeParameterDependenciesForTest(tests[i].tests[k].customId);		
	                            				}
	                            			}
	                            			//Removing parameter dependencies for this test 
	                            			removeParameterDependenciesForTest(testCustomId);
	                            			tests.remove(id);
	                            			return;
	                            		}
	                            		if(tests[i].tests!=null) {
	                            			removeTest(testCustomId, tests[i].tests);
	                            		}
	                            	}
                            	}
                            }
                            /**
                            Returns string with html content
                            */
                            function renderTestParameterControl(test, parameter) {
                            	var strControl = "";
                                if(parameter.controlType == "text") {
                                    strControl = "<input type='text' class='custom-edit-text' width='100%'";
                                    strControl+=" name='test_"+test.customId+"_parameter_"+parameter.id+"'";
                                    strControl+=" value='"+escapeHTML(parameter.defaultValue)+"'/>";
                                }
                                else if(parameter.controlType == "list") {
                                    strControl = "<select class='custom-dropdown' style='width:100%;' ";
                                    strControl+=" name='test_"+test.customId+"_parameter_"+parameter.id+"'>";
                                    for(var j=0; j<parameter.possibleValuesList.length;j++) {
                                        if(parameter.defaultValue==parameter.possibleValuesList[j]) {
                                            strControl+="<option selected='selected'>";
                                        }
                                        else strControl+="<option>";
                                        
                                        strControl+=escapeHTML(parameter.possibleValuesList[j]);
                                        strControl+="</option>";
                                    }
                                    strControl+="</select>";
                                }
                                else if(parameter.controlType == "boolean") {
                                    var name="test_"+test.customId+"_parameter_"+parameter.id;
                                    var checked = "";
                                    if(parameter.defaultValue=="true") {
                                        checked="checked=\"checked\"";
                                    }
                                    
                                    strControl = "<input type=\"checkbox\" name=\""+name+"\" id=\""+name+"\" "+checked+"/>";
                                }
                                return strControl;
                            }
                            /**
                            * Generates div element with test layout view 
                            */
                            function renderTest(test, isChild) {
                            	
                                var id = test.customId;
                                var trDescriptionLayout = "<span id='testRunDescription_"+test.customId+"' style='color:#999999;font-weight:normal;'> "+escapeHTML(trDescription)+" </span>";
                                var str = "";
                                
                                var trDescription = "";

                                if(test.testRunDescription!=null){
                                    trDescription = test.testRunDescription;
                                }
                                
                                var displayContent = "none";
                                var disclosureIcon = "disclosure-icon-close";
                                var testLayoutClassName="test-layout";
                                if(test.isOpen!=null && test.isOpen) {
                                	displayContent = "block";
                                	disclosureIcon = "disclosure-icon-open";
                                	testLayoutClassName="test-layout-selected";
                                }
                                
                                
                                str+= "<div id=\"divTestMainLayout"+test.customId+"\" class=\""+testLayoutClassName+"\">";
                                str+= "   <div class='dropArea' onmouseup=\"return onDropAreaMouseUp(this, '"+(isChild?'child':'test')+"', "+id+", false);\" onmouseover='onDropAreaMouseOver(this, false);' onmouseout='onDropAreaMouseOut(this, false);'>";
                                str+= "      <div class=\"dropArea-line\"></div>";
                                str+= "      <table border=\"0\" width=\"100%\" cellpadding=\"0px\" cellspacing=\"0px\">";
                                str+= "         <tr>";
                                str+= "             <td width=\"20px\" height=\"20px\">";
                                str+= "                 <a class=\"test-layout-remove-link\" href=\"javascript:removeTest('"+test.customId+"');\"><img src=\"../images/button-close-2.png\"/></a>";
                                str+= "             </td>";
                                str+= "             <td>";
                                str+= "                 <div onMouseDown=\"onAddedBrickMouseDown(this, "+test.customId+",'"+(isChild?'child':'test')+"'); return false;\">";
                                str+= "                 <a class=\"test-title-link\" style=\"padding:5px;width:100%;height:100%;display: block;margin:0px;outline-color:invert;outline-style:none;outline-width:medium;\" ";
                                str+= "                      href=\"javascript:onTestPanelClick('"+test.customId+"');\"><b>"+escapeHTML(test.name)+"</b>";
                                str+= "                     <span id=\"divIconTC"+test.customId+"\" class=\""+disclosureIcon+"\" style=\"float:left;\"></span>";
                                
                                if(test.tests==null) {
                                	str+= "                     <img src=\"../images/iconTest.png\" style=\"float:left;\"/>";
                                }
                                else { 
                                	str+= "                     <img src=\"../images/iconTestGroupCustom.png\" style=\"float:left;\"/>";
                                }
                                str+= trDescriptionLayout;
                                
                                str+= "                 </a>";
                                str+= "                 </div>";
                                str+= "             </td>";
                                str+= "             <td width=\"20px\" height=\"20px\">";
                                str+= "                 <a class='icon-button' href='javascript:editTestRunDescription("+test.customId+");' title='Edit description'><img src='../images/trm-icon-show-in-editor.png'/></a>";
                                str+= "             </td>";
                                str+= "             <td width=\"20px\" height=\"20px\">";
                                str+= "                 <a class='icon-button' href='../test/display?id="+test.id+"' target='_blank'><img src='../images/workflow-icon-external-link.png'/></a>";
                                str+= "             </td>";
                                str+= "         </tr>";
                                str+= "      </table>";
                                str+= "   </div>";
                                str+= "   <div id=\"divTestContent"+test.customId+"\" style=\"display:"+displayContent+";font-size:8pt;padding-left:10px;\">";
                                if(test.tests==null) {
	                                str+= "                     <div class=\"small-description\" style=\"padding-left:50px;padding-bottom:10px;\">";
	                                str+= "                     "+escapeHTML(test.description);
	                                
	                                str+= "                     </div>";
	                                if(test.inputParameters!=null && test.inputParameters.length>0)
	                                {
	                                    str+= "                 <div style=\"padding-left:50px;margin-bottom:10px;\">";
	                                    str+= "                    Input Parameters:<br/>";
	                                    str+= "                    <table border=\"0\">";
	                                    for(var i=0;i<test.inputParameters.length;i++)
	                                    {
	                                        str+= "                   <tr class=\"test-input-parameter-table\">";
	                                        str+= "                       <td onMouseMove=\"showTooltip(event,this,'parameterTooltip"+test.customId+"_"+test.inputParameters[i].id+"');\" onMouseOut=\"hideTooltip('parameterTooltip"+test.customId+"_"+test.inputParameters[i].id+"');\">";
	                                        str+= "                           <img id=\"parameterLogo_"+test.customId+"_"+test.inputParameters[i].id+"\" src=\"../images/trm-parameter-icon.png\"/> <b>"+test.inputParameters[i].name+"</b>";
	                                        str+= "                       <div id=\"parameterTooltip"+test.customId+"_"+test.inputParameters[i].id+"\" style=\"display:none;\" class=\"tooltip\"><pre>"+escapeHTML(test.inputParameters[i].description)+"</pre></div>";
	                                        str+= "                       </td>";
	                                        str+= "                       <td width=\"20px\"> = </td>";
	                                        str+= "                       <td>";
	                                        str+= "                           <div id=\"divTest_"+test.customId+"_Parameter_"+test.inputParameters[i].id+"_Control\">"+renderTestParameterControl(test, test.inputParameters[i])+"</div>";
	                                        str+= "                           <div id=\"divTest_"+test.customId+"_Parameter_"+test.inputParameters[i].id+"_Link\" style=\"display:none;\"></div>";
	                                        str+= "                       </td>";
	                                        str+= "                       <td width=\"100px\">";
	                                        if(test.inputParameters[i].controlType=="text")
	                                        {
	                                              str+="<a class=\"icon-button\" href=\"javascript:showParameterInBigEditor('"+test.customId+"',"+test.inputParameters[i].id+");\"><img src=\"../images/trm-icon-show-in-editor.png\"/></a>";   
	                                        }
	                                        str+= "                           <a class=\"icon-button\" href=\"javascript:linkParameter("+test.inputParameters[i].id+",'"+test.customId+"');\"><img src=\"../images/trm-link-icon.png\"/></a>";
	                                        str+= "                       </td>";
	                                        str+= "                   </tr>";
	                                    }
	                                    str+= "                    </table>";
	                                    str+= "                 </div>";
	                                }
	                                if(test.outputParameters!=null && test.outputParameters.length>0)
	                                {
	                                    str+= "                 <div style=\"padding-left:50px;margin-bottom:10px;\">";
	                                    str+= "                    Output Parameters:<br/>";
	                                    str+= "                    <table border=\"0\">";
	                                    for(var i=0;i<test.outputParameters.length;i++)
	                                    {
	                                        str+= "                   <tr>";
	                                        str+= "                       <td onMouseMove=\"showTooltip(event,this,'parameterTooltip"+test.customId+"_"+test.outputParameters[i].id+"');\" onMouseOut=\"hideTooltip('parameterTooltip"+test.customId+"_"+test.outputParameters[i].id+"');\">";
	                                        str+= "                           <img src=\"../images/trm-parameter-icon.png\"/> <b>"+test.outputParameters[i].name+"</b>";
	                                        str+= "                           <div id=\"parameterTooltip"+test.customId+"_"+test.outputParameters[i].id+"\" style=\"display:none;\" class=\"tooltip\"><pre>"+escapeHTML(test.outputParameters[i].description)+"</pre></div>";
	                                        str+= "                       </td>";
	                                        str+= "                   </tr>";
	                                        
	                                    }
	                                    str+= "                    </table>";
	                                    str+= "                 </div>";
	                                }
                                }
                                else {
                                	//The test is a test group 
                                	for(var k=0;k<test.tests.length;k++) {
                                		str+=renderTest(test.tests[k], true);
                                	}
                                	
                                	str+= "<div class='dropArea-big' onmouseup=\"onDropAreaMouseUp(this, 'lastInGroup','"+test.customId+"', true); return false;\" onmouseover='onDropAreaMouseOver(this, true);' onmouseout='onDropAreaMouseOut(this, true);'>";
                                	str+= "<div class='dropArea-line'></div> Drop your tests here</div>";
                                }
                                str+= "                 </div>";
                                str+= "</div>";
                                
								return str;
                            }
                        
                            var _dependentTestCustomId = 0;
                            var _dependentTestParameterId = "";
                            function linkParameter(parameterId, testCustomId) {
                                _dependentTestCustomId = testCustomId;
                                _dependentTestParameterId = parameterId;
                                //Collapsing all tests in short tests popup
                                var div = null;
                                for(var i=0;i<myTests.length;i++) {
                                    div = document.getElementById("divTestShortContent"+myTests[i].customId);
                                    if(div!=null)div.style.display="none";
                                }
                                showPopup("divTestsShort",400,500);
                            }
                            function renderDependentParameter(test, parameter){
                            	var prerTest = findTestByCustomId(parameter.depends.testCustomId);
                                var prerParameterName = findInputParameterNameById(parameter.depends.parameterId, prerTest);
                        
                                var parentTest = findParentForCustomId(prerTest.customId);
                                var parentTitle = "";
                                if(parentTest!=null) {
                                	parentTitle = "<b>"+escapeHTML(parentTest.name)+"</b> -&gt; ";
                                }
                                
                                var html = parentTitle + "<b>"+escapeHTML(prerTest.name)+"</b> -&gt; "+escapeHTML(prerParameterName);
                                html+=" <a href=\"javascript:removeParameterLink('"+test.customId+"',"+parameter.id+");\"><img src=\"../images/button-close-2.png\"/></a>";
                                
                                var divPC = document.getElementById("divTest_"+test.customId+"_Parameter_"+parameter.id+"_Control");
                                var divPL = document.getElementById("divTest_"+test.customId+"_Parameter_"+parameter.id+"_Link");
                                divPL.innerHTML = html;
                                divPC.style.display = "none";
                                divPL.style.display = "block";
                        
                                var img = document.getElementById("parameterLogo_"+test.customId+"_"+parameter.id);
                                img.src="../images/trm-parameter-icon-linked.png";
                            }
                            function confirmLinkParameter(parameterId, testCustomId) {
                                if(testCustomId == _dependentTestCustomId) {
                                    alert("Test cannot be linked to itself");
                                    return;
                                }
                                var test = findTestByCustomId(_dependentTestCustomId);
                                var paramIndex = findInputParameterIndexById(_dependentTestParameterId, test);
                        
                                var depends = new Object();
                                depends.testCustomId = testCustomId;
                                depends.parameterId = parameterId;
                                test.inputParameters[paramIndex].depends = depends;
                        
                                //Rendering the linked parameter 
                                renderDependentParameter(test, test.inputParameters[paramIndex]);
                                closePopup("divTestsShort");
                            }
                            function findInputParameterIndexById(parameterId, test) {
                            	for(var i=0;i<test.inputParameters.length;i++) {
                                    if(test.inputParameters[i].id == parameterId) return i;
                                }
                                return -1;
                            }
                            function findInputParameterNameById(parameterId, test) {
                            	for(var i=0;i<test.inputParameters.length;i++) {
                                    if(test.inputParameters[i].id == parameterId) return test.inputParameters[i].name;
                                }
                                return -1;
                            }
                            function removeParameterLink(testCustomId, parameterId) {
                                var test = findTestByCustomId(testCustomId);
                                var pId = findInputParameterIndexById(parameterId, test);
                        
                                var divPC = document.getElementById("divTest_"+testCustomId+"_Parameter_"+parameterId+"_Control");
                                var divPL = document.getElementById("divTest_"+testCustomId+"_Parameter_"+parameterId+"_Link");
                                divPL.innerHTML = "";
                                divPC.style.display = "block";
                                divPL.style.display = "none";
                        
                                test.inputParameters[pId].depends = null;
                                var img = document.getElementById("parameterLogo_"+testCustomId+"_"+parameterId);
                                img.src="../images/trm-parameter-icon.png";
                            }
                        
                        
                        
                            function onTestPanelClick(testCustomId) {
                                var divIcon = document.getElementById("divIconTC"+testCustomId);
                                var divTestMain = document.getElementById("divTestMainLayout"+testCustomId);
                                
                                $("#divTestContent"+testCustomId).slideToggle("fast", function(){
                                	var test = findTestByCustomId(testCustomId);
                                	if ($(this).is(':hidden')) {
                                		if(test!=null) {
                                			test.isOpen = false;
                                		}
                                		divIcon.className = "disclosure-icon-close";
                                		divTestMain.className="test-layout";
                                    } else {
                                    	if(test!=null) {
                                			test.isOpen = true;
                                		}
                                    	divIcon.className = "disclosure-icon-open";
                                        divTestMain.className="test-layout-selected";
                                    }
                                });
                            }
                            function gatherAllParameterValues(tests) {
                            	if(tests==null) {
                            		gatherAllParameterValues(myTests);
                            	}
                            	else {
	                            	//Gathering all input parameters values for the tests
	                                for(var i=0;i<tests.length;i++) {
	                                	if(tests[i].tests!=null) {
	                                		gatherAllParameterValues(tests[i].tests);
	                                	}
	                                    if(tests[i].inputParameters!=null) {
	                                        for(var j=0;j<tests[i].inputParameters.length;j++) {
	                                            if(tests[i].inputParameters[j].depends==null) {
	                                                var value = "";
	                                                if(tests[i].inputParameters[j].controlType == "boolean") {
	                                                    var chk = document.getElementById("test_"+tests[i].customId+"_parameter_"+tests[i].inputParameters[j].id);
	                                                    if(chk.checked) {
	                                                        value="true";
	                                                    }
	                                                    else value = "false";
	                                                }
	                                                else if(tests[i].inputParameters[j].controlType == "text") {
	                                                	value = $("[name=\"test_"+tests[i].customId+"_parameter_"+tests[i].inputParameters[j].id+"\"]").val();
	                                                }
	                                                else if(tests[i].inputParameters[j].controlType == "list") {
	                                                	value = $("[name=\"test_"+tests[i].customId+"_parameter_"+tests[i].inputParameters[j].id+"\"]").val();
	                                                }
	                                                tests[i].inputParameters[j].value = value;
	                                            }
	                                        }
	                                    }
	                                }
                            	}
                            }
                            
                            function clearTrashDataFromTest(test) {
                            	
                            	var c = {};
                            	if(test.id!=null){
                            		c.id = test.id;	
                            	}
                            	c.customId = test.customId;
                            	if(test.testRunDescription!=null) { 
                                	c.testRunDescription = test.testRunDescription;
                            	}
                                if(test.inputParameters!=null){
                                	c.inputParameters = [];
                                    for(var j=0;j<test.inputParameters.length;j++){
                                    	c.inputParameters[j] = {};
                                        c.inputParameters[j].id = test.inputParameters[j].id;
                                        c.inputParameters[j].name = test.inputParameters[j].name;
                                        c.inputParameters[j].value = test.inputParameters[j].value;
                                        if(test.inputParameters[j].depends!=null){
                                        	c.inputParameters[j].depends = test.inputParameters[j].depends;
                                        }
                                    }
                                }
                                if(test.outputParameters!=null){
                                	c.outputParameters = [];
                                    for(var j=0;j<test.outputParameters.length;j++){
                                    	c.outputParameters[j]={};
                                    	c.outputParameters[j].id = test.outputParameters[j].id;
                                    	c.outputParameters[j].name = test.outputParameters[j].name;
                                    }
                                }
                                if(test.tests!=null) {
                                	//Saving name of test group 
                                	c.name = test.name;
                                	
                                	c.tests = [];
                                	
                                	for(var i=0; i<test.tests.length; i++) {
                                		c.tests[i] = clearTrashDataFromTest(test.tests[i]);
                                	}
                               	}
                                return c;
                            }
                            function submitEditSuiteForm() {
                            	gatherAllParameterValues();

                                var tests = [];
                                
                                for(var i=0;i<myTests.length;i++){
                                	tests[i] = clearTrashDataFromTest(myTests[i]);
                                }
                                var jsonSuiteData = JSON.stringify(tests);
                                document.forms.editSuiteForm.suiteData.value = jsonSuiteData;
                                return true;
                            }
                            function moveTestUp(testCustomId)
                            {
                                var id = findTestIdByCustomId(testCustomId);
                                if(id>0)
                                {
                                    var tmp = myTests[id];
                                    myTests[id] = myTests[id-1];
                                    myTests[id-1] = tmp;
                                    
                                    rerenderTests();
                                }
                            }
                            function moveTestDown(testCustomId)
                            {
                                var id = findTestIdByCustomId(testCustomId);
                                if(id<(myTests.length-1))
                                {
                                    var tmp = myTests[id];
                                    myTests[id] = myTests[id+1];
                                    myTests[id+1] = tmp;
                                    
                                    rerenderTests();
                                }
                            }	
                            function rerenderTests() {
                                var table = document.getElementById("myTestsTable");
                                while(table.hasChildNodes()) {
                                    table.removeChild(table.firstChild);
                                }
                                
                                table = document.getElementById("myTestsShortTable");
                                while(table.hasChildNodes())
                                {
                                    table.removeChild(table.firstChild);
                                }
                                
                                table = document.getElementById("myTestsTable");
                                for(var i=0; i<myTests.length;i++)
                                {
                                    var tr = document.createElement("tr");
                                    var td = document.createElement("td");
                                    td.innerHTML = renderTest(myTests[i]);
                                    tr.appendChild(td);
                                    table.appendChild(tr);
                                    myTests[i].tableRowElement = tr;
                            
                                    addTestToShortTestsLayout(myTests[i]);
                                }
                                
                                 renderTestParameters();
                            }
                            
                            var _bigEditor = {
                            };
                            
                            function showParameterInBigEditor(testCustomId, parameterId) {
                                _bigEditor = {
                                        testCustomId: testCustomId,
                                        parameterId: parameterId,
                                        onSave: function(){
                                  			var textarea = document.getElementById("bigEditorTextarea");
		                                    var text = textarea.value;
		                                    //Replacing all breaklines with spaces
		                                    text = text.replace(/(\r\n|\n|\r)/gm," ");
		                                    $("[name=\"test_"+this.testCustomId+"_parameter_"+this.parameterId+"\"]").val(text);
		                                    closePopup("divBigEditor");
                                        }
                                 };
                                  
                                var text = $("[name=\"test_"+testCustomId+"_parameter_"+parameterId+"\"]").val();
                                var title = document.getElementById("panel_bigEditor_title");
                                var test = findTestByCustomId(testCustomId);
                                var parameterName = findInputParameterNameById(parameterId, test);
                                title.innerHTML = parameterName;
                                $("#bigEditorTextarea").text(text);
                                showPopup("divBigEditor", 600, 400);
                            }

                            function editTestRunDescription(testCustomId) {
                                var test  = findTestByCustomId(testCustomId);
                                
                                _bigEditor = {
                                	    test: test,
                                        testCustomId: testCustomId,
                                        onSave: function(){
                                            var textarea = document.getElementById("bigEditorTextarea");
                                            var text = textarea.value;
                                            this.test.testRunDescription = text;
                                            var el = document.getElementById("testRunDescription_"+this.testCustomId);
                                            
                                            el.innerHTML = escapeHTML(text);
                                            closePopup("divBigEditor");
                                        }
                                 };
                                  
                                
                                 var title = document.getElementById("panel_bigEditor_title");
                                 title.innerHTML = "Edit description for test \""+escapeHTML(test.name)+"\"";
                                 var textarea = document.getElementById("bigEditorTextarea");
                                 textarea.value = "";
                                 if(test.testRunDescription!=null){
                                     textarea.value = test.testRunDescription;
                                 }
                                 showPopup("divBigEditor", 600, 400);
                            }
                            
                            var _testGroupEditor= {
                            };
                            
                            function onAddTestGroup() {
                            	_testGroupEditor = {
                            		onSave: function () {
                            			var testGroupName=$("#testGroupEditName").val();
                            			if(testGroupName!=null && testGroupName!="") {
                            				var testGroup = {
                            					name: testGroupName,
                            					tests: []
                            				};
                            				addTestToSuite(testGroup);
                            			}
                            			closePopup("divTestGroupEdit");
                            		}		
                            	};
                            	
                            	$("#testGroupEditName").val("");
                            	showPopup("divTestGroupEdit", 300, 250);
                            	$("#testGroupEditName").focus();
                            }
                            
                            </script>
                            
                
                
                <div id="divCurrentTests" class="my-suite-tests-layout">
                    <table id="myTestsTable" border="0px" cellspacing="0px" cellpadding="0px" width="100%">
                    </table>
                    <div class="dropArea-big" onMouseUp="onDropAreaMouseUp(this, 'last', 0, true); return false;" onMouseOver="onDropAreaMouseOver(this, true);" onMouseOut="onDropAreaMouseOut(this, true);">
                        <div class="dropArea-line"></div>
                        <br/>
                        Drop your tests here
                    </div>
                </div>
                <div id="divTestsShort" style="position:absolute;display:none;width:400px;height:500px;">
                    <tag:panel title="Choose prerequisite test parameter" 
                                align="center"
                                closeDivName="divTestsShort" 
                                width="400px" height="500px">
                        <div style="overflow:auto;width:360px;height:460px;">
                        <table id="myTestsShortTable" border="0px" cellspacing="0px" cellpadding="0px" width="100%">
                        </table>
                        </div>
                    </tag:panel>
                </div>
                
                <a href="javascript:onAddTestGroup();">Add test group</a>
            </tag:panel>
</form>

<div id="divBigEditor" style="position:absolute;display:none;width:600px;height:400px;">
    <tag:panel title="Edit parameter" 
                align="center"
                closeDivName="divBigEditor" 
                width="600px" height="400px"
                id="bigEditor"
                logo="../images/workflow-icon-settings.png"
                >
        <table border="0px" cellspacing="0px" cellpadding="0px" width="100%" height="100%">
            <tr>
                <td colspan="2">
                    <textarea id="bigEditorTextarea" style="width:100%" rows="18"></textarea>
                </td>
            </tr>
            <tr>
                <td>
                    <tag:submit value="OK" onclick="javascript:_bigEditor.onSave(); return false;" width="100px"></tag:submit>
                </td>
                <td>
                    <tag:submit value="Cancel" onclick="javascript:closePopup('divBigEditor'); return false;" width="100px"></tag:submit>
                </td>
            </tr>
        </table>
    </tag:panel>
</div>

<div id="divTestGroupEdit" style="position:absolute;display:none;width:300px;height:250px;">
    <tag:panel title="Add test group" 
                align="center"
                closeDivName="divTestGroupEdit" 
                width="300px" height="200px"
                id="bigEditor"
                >
        <table border="0px" cellspacing="0px" cellpadding="0px" width="100%" height="100%">
            <tr>
                <td colspan="2">
                	Test group allows to unite tests into one instance, so you can use same data (report, browser etc.) within test group.
                	<br/>
					<br/>                
                	Test group name:<br/>
                    <tag:edit-field name="testGroupName" width="100%" id="testGroupEditName" value=""/>
                    <br/>
                    <br/>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <tag:submit value="OK" onclick="javascript:_testGroupEditor.onSave(); return false;" width="100px"></tag:submit>
                </td>
                <td align="center">
                    <tag:submit value="Cancel" onclick="javascript:closePopup('divTestGroupEdit'); return false;" width="100px"></tag:submit>
                </td>
            </tr>
        </table>
    </tag:panel>
</div>
<script language="javascript">

function getTestInputParameterById(test, parameterId)
{
    for(var i=0;i<test.inputParameters.length;i++)
    {
        if(test.inputParameters[i].id==parameterId) return test.inputParameters[i];
    }
    return null;
}
/**
This function is used to adjust the suite test to latest test changes
where c - cashedTest, s - suite test

*/
function adjustTestChanges(c, s)
{
    c.customId = s.customId;
    c.testRunDescription = s.testRunDescription;
    if(c.inputParameters!=null) {
	    for(var i=0;i<c.inputParameters.length;i++) {
	        var sip = getTestInputParameterById(s, c.inputParameters[i].id);
	        if(sip!=null) {
	            c.inputParameters[i].value = sip.value;
	            c.inputParameters[i].depends = sip.depends;
	        }
	        else c.inputParameters[i].value = "";
	    }
    }
    
    return c;
}

function loadTestGroupUsingCache(testGroup) {
	var tg = {};
	tg.customId = testGroup.customId;
	tg.name = testGroup.name;
	tg.testRunDescription = testGroup.testRunDescription;
	tg.tests = [];
	
	for(var i=0; i<testGroup.tests.length; i++) {
		var ctest = cloneObject(cashedTests[testGroup.tests[i].id]);
		if(ctest!=null) {
			ctest = adjustTestChanges(ctest, testGroup.tests[i]);
            tg.tests[i] = ctest;
		}
	}
	return tg;
}
function onLoadSuiteData() {
    if(loadedSuiteData!=null) {
    	for(var i=0;i<loadedSuiteData.length;i++) {
    		if(loadedSuiteData[i].tests!=null) {
    			var testGroup = loadTestGroupUsingCache(loadedSuiteData[i]);
    			addTestToSuite(testGroup);
    		}
    		else {
	            var ctest = cloneObject(cashedTests[loadedSuiteData[i].id]);
	            if(ctest!=null) {
	                ctest = adjustTestChanges(ctest, loadedSuiteData[i]);
	                addTestToSuite(ctest);
	            }
    		}
        }
        renderTestParameters();
    }
}
function renderTestParameters(tests) {
	if(tests==null) {
		renderTestParameters(myTests);
	}
	else {
		//Setting tests input parameters 
	    for(var i=0; i<tests.length; i++){
	    	if(tests[i].tests!=null) {
	    		renderTestParameters(tests[i].tests);
	    	}
	        if(tests[i].inputParameters!=null){
	            for(var j=0;j<tests[i].inputParameters.length;j++){
	                if(tests[i].inputParameters[j].depends==null) {
	                    if(tests[i].inputParameters[j].controlType == "boolean") {
	                        var chk = eval("document.forms.editSuiteForm.test_"+tests[i].customId+"_parameter_"+tests[i].inputParameters[j].id);
	                        if(tests[i].inputParameters[j].value=="true") {
	                            chk.checked=true;
	                        }
	                        else {
	                            chk.checked=false;
	                        }
	                    }
	                    else {
	                    	$("[name=\"test_"+tests[i].customId+"_parameter_"+tests[i].inputParameters[j].id+"\"]").val(tests[i].inputParameters[j].value);
	                    }
	                }
	                else {
	                	renderDependentParameter(tests[i], tests[i].inputParameters[j]);
	                }
	            }
	        }
	    }
	}
}
<%
net.mindengine.oculus.frontend.domain.trm.TrmSuite suite = (net.mindengine.oculus.frontend.domain.trm.TrmSuite)pageContext.findAttribute("suite"); 
String suiteData = suite.getSuiteData();
if(suiteData!=null)
{
    out.print("var loadedSuiteData = ");
    out.print(suiteData);
    out.println(";");
}
else out.print("var loadedSuiteData = null;");

Map<Long, Test> cashedTests = (Map<Long, Test>)pageContext.findAttribute("cashedTests");
JSONValue jsonValue = JSONMapper.toJSON(cashedTests);
out.println("var cashedTests = "+jsonValue.render(false)+";");
out.println("onLoadSuiteData();");
%>
</script>