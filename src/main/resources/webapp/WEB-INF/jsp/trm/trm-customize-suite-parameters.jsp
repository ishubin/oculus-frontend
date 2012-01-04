<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <a href="../test-run-manager/main">Test Run Manager</a>
    <img src="../images/breadcrump-arrow.png"/> 
    
    <a href="../test-run-manager/customize">Customize Settings</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../test-run-manager/customize-suite-parameters">Customize Suite Parameters</a>
    <img src="../images/breadcrump-arrow.png"/>
    Customize Suite Parameters
</div>


<table border="0" align="center" width="500px">
    <tr>
        <td class="small-description">
            <p>
		    This set of parameters will be displayed in Test Run Manager.<br/>
		    Users will be available to choose these parameters and run the test suite with the selected parameters.
		    </p>
        </td>
    </tr>
</table>

<div class="breadcrump" align="center">Parameters:</div>
<form name="formDeleteSuiteParameter" method="post">
<input type="hidden" name="deleteParameterId" value="-1"/>
<input type="hidden" name="Submit" value="Delete Parameter"/>
</form>
<script language="javascript">
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

function escapeHTML(html) { return html. replace(/&/gmi, '&amp;'). replace(/"/gmi, '&quot;'). replace(/>/gmi, '&gt;'). replace(/</gmi, '&lt;'); }

function deleteSuiteParameter(name, id)
{
    if(confirm("Are you sure you want to delete '"+name+"' parameter?"))
    {
        var form = document.formDeleteSuiteParameter;
        form.deleteParameterId.value = id;
        form.submit();
    }
}
function OnClickOnChangeParameterType(id)
{
    var div1 = document.getElementById("divParameterChangeTypeLink"+id);
    var div2 = document.getElementById("divParameterChangeType"+id);
    div1.style.display="none";
    div2.style.display="block";
}
function OnChangeParameterType(parameterId, selectedIndex)
{
    var div = document.getElementById("divPossibleValuesParameter"+parameterId+"Layout");
    if(selectedIndex==1)
    {
        div.style.display = "block";
    }
    else div.style.display = "none";
}
function deleteParameterPossibleValue(parameterId, valueId)
{
    var div = document.getElementById("divParameterPossibleValue"+parameterId+"_"+valueId);
    div.innerHTML = "";
}
function addParameterPossibleValue(parameterId)
{
    
    var div = document.getElementById("divParameterPossibleValues"+parameterId);
    var value = document.getElementById("addPossibleValue"+parameterId).value;
    document.getElementById("addPossibleValue"+parameterId).value="";
    var html = div.innerHTML;
    var maxId = document.getElementById("possibleValuesMaxId"+parameterId).value;
    maxId++;
    document.getElementById("possibleValuesMaxId"+parameterId).value = maxId;

    html+="<div id=\"divParameterPossibleValue"+parameterId+"_"+maxId+"\" style=\"width:100%;\">";
    html+="<table border=\"0\" width=\"100%\"><tr><td><i>"+escapeHTML(value)+"</i>";
    html+="<input type=\"hidden\" name=\"changePossibleValue"+maxId+"\" value=\""+escapeHTML(value)+"\"/>";
    html+="</td>";
    html+="<td width=\"20px\"><a href=\"javascript:deleteParameterPossibleValue("+parameterId+","+maxId+");\">Delete</a></td>";
    html+="</tr>";
    html+="</table>";
    html+="</div>";

    div.innerHTML = html;
    
}
</script>

<c:forEach items="${suiteProperties}" var="p">
    <form method="post">
    <input type="hidden" name="changeParameterId" value="${p.id}"/>
    <table width="450px" border="0" cellspacing="0px" align="center" cellpadding="0">
        <tr>
            <td>
                <tag:panel title="${p.name}" logo="../images/workflow-icon-settings.png" align="left" width="400px" id="suiteProperty_${p.id}" disclosure="true" closed="true">
                    <table width="350px" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td>
                                <table border="0" width="350px">
                                    <tr>
                                        <td><b>Type:</b></td>
                                        <td>
                                            <table width="100%" border="0">
                                                <tr>
                                                    <td>${p.subtype}</td>
                                                    <td>
                                                        <div id="divParameterChangeTypeLink${p.id}"><a href="javascript:OnClickOnChangeParameterType(${p.id});">Change</a></div>
                                                        <div id="divParameterChangeType${p.id}" style="display:none;">
                                                            <select name="changeParameterType" onchange="OnChangeParameterType(${p.id},this.selectedIndex);">
                                                                <option value="text" <c:if test="${p.subtype=='text'}">selected</c:if>>Text</option>
                                                                <option value="list" <c:if test="${p.subtype=='list'}">selected</c:if>>List</option>
                                                                <option value="checkbox" <c:if test="${p.subtype=='checkbox'}">selected</c:if>>CheckBox</option>
                                                            </select>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Description:</b></td>
                                        <td class="small-description">${p.description}</td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><b>Details:</b></td>
                                        <td>
                                            
                                            <div id="divPossibleValuesParameter${p.id}Layout" 
                                                <c:choose>
                                                    <c:when test="${p.subtype=='list'}">style="display:block;"</c:when>
                                                    <c:otherwise>style="display:none;"</c:otherwise>
                                                </c:choose>
                                                >
                                                
                                                <input type="hidden" id="possibleValuesMaxId${p.id}" name="possibleValuesMaxId${p.id}" value="${p.valuesCount}"/>
                                                <div id="divParameterPossibleValues${p.id}" style="width:100%;">
                                                    <c:if test="${p.subtype=='list'}">
                                                        <c:forEach items="${p.valuesAsList}" var="v" varStatus="vStatus">
                                                            <div id="divParameterPossibleValue${p.id}_${vStatus.index}" style="width:100%;">
                                                               <table border="0" width="100%">
                                                                    <tr>
                                                                        <td>
                                                                            <i>${v}</i>
                                                                            <input type="hidden" name="changePossibleValue${vStatus.index}" value="${v}"/>
                                                                        </td>
                                                                        <td width="20px"><a href="javascript:deleteParameterPossibleValue(${p.id},${vStatus.index});">Delete</a></td>
                                                                    </tr>
                                                               </table>
                                                            </div>
                                                        </c:forEach>
                                                    </c:if>
                                                </div>
                                                <input type="text" id="addPossibleValue${p.id}" name="addPossibleValue${p.id}"/><input type="button" name="Add" onclick="addParameterPossibleValue(${p.id});" value="Add"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <tag:submit name="Submit" value="Save"></tag:submit>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </tag:panel>
            </td>
            <td valign="top" align="left">
                <a href="javascript:deleteSuiteParameter('${p.name}',${p.id});">Delete</a>
    
            </td>
        </tr>
    </table>
    </form>
</c:forEach>

<br/>
<br/>
<tag:panel title="Add Parameter" align="center" width="400px">
	<form method="post">
	    <table  border="0" width="100%" cellpadding="0" cellspacing="0">
	        <tbody>
	            <tr>
	                <td class="small-description">Name:</td>
	            </tr>
                <tr>
	                <td>
	                    <tag:edit-field-simple name="AP_Name" id="AP_Name" width="100%"></tag:edit-field-simple>
	                </td>
	            </tr>
	            <tr>
	                <td class="small-description">Description:</td>
	            </tr>
                <tr>
	                <td>
	                    <textarea name="AP_Description" style="width:100%;" rows="8"></textarea>
	                </td>
	            </tr>
	            <tr>
	                <td class="small-description">Control Type:</td>
	            </tr>
                <tr>
	                <td>
	                    <select name="AP_ControlType" onchange="onChangeAPType(this.selectedIndex);">
	                        <option value="text" selected="selected">Text</option>
	                        <option value="list">List</option>
	                        <option value="checkbox">CheckBox</option>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td class="small-description">Details:</td>
	            </tr>
                <tr>
	                <td>
	                    <script language="javascript">
	                    var ap_ListPossibleValues = Array();
	                    var ap_ListPossibleValuesDefaultId = -1;
	    
	                    function onChangeAPType(selectedIndex)
	                    {
	                        var divListDetails = document.getElementById('divAP_ListDetails');
	                        if(selectedIndex==1)
	                        {
	                            divListDetails.style.display = "block";
	                        }
	                        else divListDetails.style.display = "none";
	                    }
	                    function onDivAP_TypeListAddPossibleValue()
	                    {
	                        var id = ap_ListPossibleValues.length;
	                        var input = document.getElementById("AP_ListAddPossibleValueName");
	                        if(input.value!="")
	                        {
	                            ap_ListPossibleValues[id] = input.value;
	                            input.value = "";
	                            if(ap_ListPossibleValuesDefaultId==-1)
	                            {
	                                ap_ListPossibleValuesDefaultId=id;
	                            }
	                            onDivAP_TypeListRenderPossibleValues();
	                        }
	                        else alert("Input default value"); 
	                    }
	                    function onDivAP_ListRemovePossibleValue(id)
	                    {
	                        ap_ListPossibleValues.remove(id);
	                        if(ap_ListPossibleValues.length>0)
	                        {
	                            ap_ListPossibleValuesDefaultId=0;
	                        }
	                        onDivAP_TypeListRenderPossibleValues();
	                    }
	                    function onDivAP_TypeListRenderPossibleValues()
	                    {
	                        var div = document.getElementById("divAP_ListPossibleValues");
	                        if(ap_ListPossibleValues.length==0)
	                        {
	                            div.innerHTML = "There are no possible values defined yet."; 
	                        }
	                        else
	                        {
	                            var str = "";
	                            str+="<table border=\"0\" width=\"100%\" cellspacing=\"5\">";
	                            for(var i=0;i<ap_ListPossibleValues.length;i++)
	                            {
	                                str+="<tr style=\"background:#dddddd;\">";
	                                str+="<td>";
	                                str+=escapeHTML(ap_ListPossibleValues[i]);
	                                str+="<input type=\"hidden\" name=\"AP_ListPossibleValue_"+i+"\" value=\""+ap_ListPossibleValues[i]+"\"/>";
	                                str+="</td>";
	                                str+="<td width = \"50px\">";
	                                str+="<a href=\"javascript:onDivAP_ListRemovePossibleValue("+i+");\">Remove</a>";
	                                str+="</td>";
	                                str+="</tr>";
	                            }
	                            str+="</table>";
	                            str+="<input type=\"hidden\" name=\"AP_ListPossibleValuesCount\" value=\""+ap_ListPossibleValues.length+"\"/>";
	                            div.innerHTML = str;
	                        }
	                    }
	                    </script>
	                    <div id="divAP_ListDetails" style="display:none;">
	                        <div id="divAP_ListPossibleValues">
	                            There are no possible values defined yet.
	                        </div>
	                        New Possible Value:<br/>
	                        <table border="0">
	                            <tr>
	                                <td><input id="AP_ListAddPossibleValueName" name="AP_ListAddPossibleValueName" type="text" /></td>
	                                <td><input type="button" value="Add" onclick="onDivAP_TypeListAddPossibleValue(); return false;"/></td>
	                            </tr>
	                        </table>
	                    </div>
	                </td>
	            </tr>
	        </tbody>
	        <tfoot>
	           <tr>
	               <td colspan="2" align="center">
	                   <tag:submit name="Submit" value="Add Parameter"></tag:submit>
	               </td>
	           </tr>
	        </tfoot>
	    </table>
	</form>
</tag:panel>