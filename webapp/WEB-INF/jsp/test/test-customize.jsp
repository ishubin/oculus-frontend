<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div align="center">
<div class="breadcrump" align="center">
    <a href="../project/display-${parentProject.path}"><img src="../images/workflow-icon-project.png"/> ${parentProject.name}</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-subproject.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../test/display?id=${test.id}"><img src="../images/iconTest.png"/> ${test.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Parameters
</div>   

<script language="javascript">
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
  };
function escapeHTML(html) { return html. replace(/&/gmi, '&amp;'). replace(/"/gmi, '&quot;'). replace(/>/gmi, '&gt;'). replace(/</gmi, '&lt;') }


function checkParameterName(evt)
{
    evt = (evt) ? evt : window.event;
    var c = (evt.which) ? evt.which : evt.keyCode;
    if ((c > 31 && c < 48)
        ||(c > 57 && c < 65)
        ||(c > 90 && c < 95)
        || c == 96
        ||(c > 122 && c < 127)
        ) 
    {
        status = "This field accepts numbers and letters only.";
        return false;
    }
    status = "";
    return true;
}


function submitDeleteParametersForm(parameter, parameterId)
{
    var form = document.forms["formDeleteParameter"];
    if(confirm("Are you sure you want to delete '"+parameter+"' parameter"))
    {
        form.deleteParameterId.value = parameterId;
        form.submit();
    }
}

function onEPControlTypeChange(controlType, parameterId)
{
	var divText = document.getElementById("divParameterDetails"+parameterId+"_Text");
	var divList = document.getElementById("divParameterDetails"+parameterId+"_List");
	var divBoolean = document.getElementById("divParameterDetails"+parameterId+"_Boolean");
	if(controlType==0)
	{
		divText.style.display="block";
		divList.style.display="none";
		divBoolean.style.display="none";
	}
	else if(controlType==1)
    {
        divText.style.display="none";
        divList.style.display="block";
        divBoolean.style.display="none";
    }
	else if(controlType==2)
    {
        divText.style.display="none";
        divList.style.display="none";
        divBoolean.style.display="block";
    }
	
}

</script>

<form name="formDeleteParameter" method="post">
    <input type="hidden" name="deleteParameterId" value="0"/>
    <input type="hidden" name="Submit" value="Delete Parameter"/>
</form>

<c:if test="${parametersDefinition.inputParametersCount>0 }">
    <table border="0" align="center" width="100%">
        <tr>
            <td width="37px"><img src="../images/input-parameter.png"/></td>
            <td align="left" valign="middle" style="font-size:12pt;"><b>Input Parameters:</b></td>
        </tr>
        <c:forEach items="${parametersDefinition.inputParameters}" var="p">
            <tr>
                <td colspan="2">
                   <tag:disclosure title="${p.name}" id="testParameter_${p.id}">
                       <form method="post">
                           <input type="hidden" name="parameterId" value="${p.id}"/>
                           <input type="hidden" name="type" value="input"/>
                           Name:<br/>
                           <input type="text" name="name" style="width:100%;" value="${p.name }" onKeyPress="return checkParameterName(event);"/>
                           <br/>
                           <br/>
                           Description:<br/>
                           <tag:textarea-simple name="description" value="${p.description}" style="width:100%;" rows="5"/>
                           <br/>
                           <br/><b>Control Type:</b>
                           <select name="controlType" onchange="onEPControlTypeChange(this.selectedIndex,${p.id});">
                               <option value="text" <c:if test="${p.controlType=='text'}">selected="selected"</c:if> >Text</option>
                               <option value="list" <c:if test="${p.controlType=='list'}">selected="selected"</c:if> >List</option>
                               <option value="boolean" <c:if test="${p.controlType=='boolean'}">selected="selected"</c:if> >Boolean</option>
                           </select>
                           <br/>
                           <div id="divParameterDetails${p.id}">
                               <div id="divParameterDetails${p.id}_Text" style="display:${p.controlType=='text'?'block':'none'};">
                                   Default Value:<br/>
                                   <input type="text" style="width:100%;" name="defaultValueText" value="<tag:escape text="${p.controlType=='text'?p.defaultValue:''}"/>"/>
                               </div>
                               <div id="divParameterDetails${p.id}_List" style="display:${p.controlType=='list'?'block':'none'};">
                                   <tag:parameter-edit-list-layout layoutName="EP${p.id}" parameter="${p}"/>
                               </div>
                               <div id="divParameterDetails${p.id}_Boolean" style="display:${p.controlType=='boolean'?'block':'none'};">
                                   Default value: 
                                   <input id="defaultValueBoolean${p.id}_true" name="defaultValueBoolean" type="radio" value="true" <c:if test="${p.defaultValue=='true'}">checked="checked"</c:if>/><label for="defaultValueBoolean${p.id}_true">True</label>
                                   <input id="defaultValueBoolean${p.id}_false" name="defaultValueBoolean" type="radio" value="false" <c:if test="${p.defaultValue=='false'}">checked="checked"</c:if>/><label for="defaultValueBoolean${p.id}_true">False</label>
                               </div>
                           </div> 
                           <br/><br/>
                           <tag:submit name="Submit" value="Save"></tag:submit>
                           <br/>
                           <a href="javascript:submitDeleteParametersForm('${p.name}',${p.id});">Delete Parameter</a>
                       </form>
                     </tag:disclosure>
                </td>
            </tr>
        </c:forEach>
    
    </table>
     <br/><br/>
</c:if>

<c:if test="${parametersDefinition.outputParametersCount>0 }">
    <table border="0" align="center" width="100%">
        <tr>
            <td width="37px"><img src="../images/output-parameter.png"/></td>
            <td align="left" valign="middle" style="font-size:12pt;"><b>Output Parameters:</b></td>
        </tr>
        <c:forEach items="${parametersDefinition.outputParameters}" var="p">
            <tr>
                <td colspan="2">
                   <a class="disclosure" href="javascript:onParameterClick(${p.id});">
                       <span id="divIconDisclosure${p.id}" class="disclosure-icon-close"> </span>
                       <tag:escape text="${p.name}"></tag:escape> 
                   </a>
                   <div id="divParameterLayout${p.id}" style="display:none;">
                       <tag:panel-border title="Edit parameter" align="left" width="100%">
                           <form method="post">
                               <input type="hidden" name="parameterId" value="${p.id}"/>
                               <input type="hidden" name="type" value="output"/>
                               Name:<br/>
                               <input type="text" name="name" style="width:100%;" value="${p.name }" onKeyPress="return checkParameterName(event);"/>
                               <br/>
                               <br/>
                               Description:<br/>
                               <tag:textarea-simple name="description" value="${p.description}" style="width:100%;" rows="5"/>
                               <br/>
                               <tag:submit name="Submit" value="Save"></tag:submit>
                               <br/>
                               <a href="javascript:submitDeleteParametersForm('${p.name}',${p.id});">Delete Parameter</a>
                           </form>
                       </tag:panel-border>
                   </div>
                </td>
            </tr>
        </c:forEach>
    
    </table>
        
</c:if>


<script language="javascript">
function onInputParameterChange(checkbox)
{
    if(checkbox.value == "input")
    {
        var div = document.getElementById("divParameterDetails");
        div.style.display = "block";
        var select = document.getElementById("AP_ControlType");
        select.style.display = "block";
    }
}
function onOutputParameterChange(checkbox)
{
    if(checkbox.value == "output")
    {
        var div = document.getElementById("divParameterDetails");
        div.style.display = "none";
        var select = document.getElementById("AP_ControlType");
        select.selectedIndex = 0;
        select.style.display = "none";
    }
}
</script>

<br/>
<br/>
<tag:panel title="Add Test Parameter" align="center">
<form name="addParameterForm" method="post">
 <table border="0" cellpadding="0" cellspacing="2">
     <tr>
        <td class="small-description">Type:</td>
        <td><input type="radio" id="AP_TypeInput" onchange="onInputParameterChange(this);" name="type" value="input" checked="checked"/><label for="AP_TypeInput">Input</label>
            <br/>
            <input type="radio" id="AP_TypeOutput" onchange="onOutputParameterChange(this);" name="type" value="output"/><label for="AP_TypeOutput">Output</label>
        </td>
     </tr>
     <tr>
         <td class="small-description">Name:</td>
         <td><input type="text" id="AP_Name" name="name" style="width:100%;" onKeyPress="return checkParameterName(event);"/></td>
     </tr>
     <tr>
         <td class="small-description" valign="top">Description:</td>
         <td><textarea rows="5" cols="30" id="AP_Description" name="description"></textarea></td>
     </tr>
     <tr>
          <td class="small-description">Control</td>
          <td>
              <script language="javascript">
                 function onChangeAddParameterType(selIndex)
                 { 
                      var divLabel = document.getElementById("divAP_TypeLabel");
                      if(selIndex==1)
                      {
                          divLabel.innerHTML = "Default Value:";
                          showDialog("divAP_TypeText");
                          hideDialog("divAP_TypeList");
                          hideDialog("divAP_TypeBoolean");
                      }
                      else if(selIndex==2)
                      {
                          divLabel.innerHTML = "Possible Values:";
                          hideDialog("divAP_TypeText");
                          showDialog("divAP_TypeList");
                          hideDialog("divAP_TypeBoolean");
                      }
                      else if(selIndex==3)
                      {
                          divLabel.innerHTML = "Default:";
                          hideDialog("divAP_TypeText");
                          hideDialog("divAP_TypeList");
                          showDialog("divAP_TypeBoolean");
                      }
                 }
              </script>
              <select id="AP_ControlType" name="controlType" onchange="onChangeAddParameterType(this.selectedIndex);">
                 <option value="undefined" selected="selected" style="color:gray;">Pick a control</option>
                 <option value="text">Text</option>
                 <option value="list">List</option>
                 <option value="boolean">Boolean</option>
              </select>
          </td>
      </tr>
 </table>
 <div id="divParameterDetails">
  <table border="0" cellpadding="0" cellspacing="2">
      <tr>
         <td>
             <div id="divAP_TypeLabel">
             </div>
         </td>
         <td>
                <div id="divAP_TypeText" style="display:none;">
                     <input type="text" name="defaultValueText"/>
                </div>
                <div id="divAP_TypeList" style="display:none;">
                    <tag:parameter-edit-list-layout layoutName="AP"/> 
                </div>
                <div id="divAP_TypeBoolean" style="display:none;">
                     <input id="booleanDefaultValueTrue" name="defaultValueBoolean" type="radio" value="true" checked="checked"/><label for="booleanDefaultValueTrue">True</label>
                     <input id="booleanDefaultValueFalse" name="defaultValueBoolean" type="radio" value="false"/><label for="booleanDefaultValueFalse">False</label>
                </div>
            </td>
      </tr>
      <tr>
         <td colspan="2">
             
         </td>
      </tr>
      
  </table>
 </div>
    <br/><br/>
    <script language="javascript">
    function onSubmitAddParameter()
    {
        var name = document.getElementById("AP_Name").value;
        var type = "input";

        if(document.getElementById("AP_TypeInput").checked)
        {
            type="input";
        }
        else type="output";

        var controlType = document.forms.addParameterForm.controlType;
        if(name.length==0)
        {
            alert("The parameter name cannot be empty");
            return false;
        }
        for(var i=0;i<name.length;i++)
        {
            var code = name.charCodeAt(i);
            if(!((code>=97 && code<=122) || (code>=65 && code<=90) || (code>=48 && code<=57) || (code==95)))
            {
                alert("The parameter name cannot contain special symbols or spaces");
                return false;
            }
        }
        if(type=="input" && controlType.selectedIndex==0)
        {
            alert("Choose a control");
            return false;
        }
        return true;
    }
    </script>
    <tag:submit value="Add Parameter" name="Submit" onclick="return onSubmitAddParameter();"></tag:submit>
</form>
</tag:panel>
</div>