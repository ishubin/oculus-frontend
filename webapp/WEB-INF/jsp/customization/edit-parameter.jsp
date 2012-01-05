<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<%@page import="net.mindengine.oculus.frontend.domain.customization.Customization"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="net.mindengine.oculus.frontend.domain.customization.CustomizationPossibleValue"%><div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../customization/project-${project.path}">Choose Unit</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../customization/project-${project.path}?unit=${unit}">Customize Unit</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <img src="../images/workflow-icon-settings.png"/> Parameter: ${customization.name}
</div>

<script language="javascript">
function onEditParameterTypeChange(el)
{
    var divList = document.getElementById("divListLayout");
    var divListSubtype = document.getElementById("divListSubtype");
    
    divList.style.display = "none";
    divListSubtype.style.display = "none";

    if(el.selectedIndex == 2)
    {
        divListSubtype.style.display = "block";
    }
    if(el.selectedIndex==2 || el.selectedIndex==4)
    {
        divList.style.display = "block";
    }
    else divList.style.display = "none";
}
</script>
<script language="javascript">
<%
	List<CustomizationPossibleValue> list = (List<CustomizationPossibleValue>)pageContext.findAttribute("customizationPossibleValues");
	StringBuffer str = new StringBuffer();
	str.append("var listPossibleValues = [");
	boolean bComma = false;
	for(CustomizationPossibleValue pv : list)
	{
	    if(bComma)
	    {
	        str.append(",");
	    }
	    str.append("\"");
	    str.append(StringEscapeUtils.escapeJavaScript(pv.getPossibleValue()));
	    str.append("\"");
	    bComma = true;
	}
	str.append("];");
	out.println(str.toString());
%>

function onListEnterPossibleValue()
{
    var value = document.getElementById("listEnterPossibleValue").value;
    if(value!=null && value.length>0)
    {
        var len = listPossibleValues.length;
        listPossibleValues[len] = value;
        renderListPossibleValues();
        document.getElementById("listEnterPossibleValue").value = "";
    }
    else alert("The value is empty");
}
function onListRemovePossibleValue(id)
{
    listPossibleValues.splice(id,1);
    renderListPossibleValues();
}
function onListDefaultValueRadioClick(id)
{
    listDefaultValueId = id;
}   
function renderListPossibleValues()
{
    var str = "";
    str+="<input type=\"hidden\" name=\"listPossibleValuesLength\" value=\""+listPossibleValues.length+"\"/>";
    str+="<table class=\"customization-possible-values\" width=\"100%\" border=\"0\">";
    str+="<thead><tr>";
    str+="<td>Value</td>";
    str+="<td width=\"50px\">&nbsp;</td>";
    str+="</tr></thead>";
    str+="<tbody>";
    var i=0;
    for(i=0;i<listPossibleValues.length;i++)
    {
        str+="<tr>";
        str+="<td>";
        str+=escapeHTML(listPossibleValues[i]);
        str+="<input type=\"hidden\" name=\"listPossibleValue_"+i+"\" value=\""+escapeHTML(listPossibleValues[i])+"\"/>";
        str+="</td>";
        str+="<td>";
        str+="<a href=\"javascript:onListRemovePossibleValue("+i+");\">Remove</a>";
        str+="</td>";
        str+="</tr>";
    }
    str+="</tbody>";
    str+="</table>";
    document.getElementById("divListContents").innerHTML = str;
}
function formErrorMessage(msg)
{
    document.getElementById("divFormErrorMessage").innerHTML = msg;
}
function formValidation(form)
{
    if(form.elements["name"].value.length<1)
    {   
        formErrorMessage("The name may not be empty");
        return false;
    }
    return true;
}
</script>
<tag:panel align="center" title="Edit Parameter" width="500px">
    <form onsubmit="return formValidation(this)" action="../customization/edit-parameter" method="post">
        Type: 
        <select name="type" onchange="onEditParameterTypeChange(this);">
            <option value="text" <c:if test="${customization.type=='text'}">selected="selected"</c:if> >Text</option>
            <option value="assignee" <c:if test="${customization.type=='assignee'}">selected="selected"</c:if>>Assignee</option>
            <option value="list" <c:if test="${customization.type=='list'}">selected="selected"</c:if>>List</option>
            <option value="checkbox" <c:if test="${customization.type=='checkbox'}">selected="selected"</c:if>>CheckBox</option>
            <option value="checklist" <c:if test="${customization.type=='checklist'}">selected="selected"</c:if>>CheckList</option>
        </select>
        <br/>
        <br/>
        <input type="hidden" name="id" value="${customization.id}"/>
        Name: <br/>
        
        <tag:edit-field-simple escapeHtml="true" name="name" id="edtName" width="100%" value="${customization.name}"></tag:edit-field-simple>
        <br/>
        Description: <br/>
        <textarea rows="4" style="width:100%;" name="description"><tag:escape text="${customization.description}"/></textarea>
        <br/>
        Group: <br/>
        <tag:edit-field-simple name="groupName" id="edtGroupName" width="100%" value="${customization.groupName}"></tag:edit-field-simple>
        
        
        <div id="divListSubtype"  <c:choose><c:when test="${customization.type=='list'}">style="display:block;"</c:when><c:otherwise>style="display:none;"</c:otherwise></c:choose>>
           Sub-type:
            <select name="listSubtype">
               <option value="drop-down" <c:if test="${customization.subtype=='drop-down'}">selected="selected"</c:if>>Drop-down list</option>
               <option value="list" <c:if test="${customization.subtype=='list'}">selected="selected"</c:if>>Classic list</option>
            </select>
        </div>
        <div id="divListLayout" <c:choose><c:when test="${customization.type=='list' || customization.type=='checklist'}">style="display:block;"</c:when><c:otherwise>style="display:none;"</c:otherwise></c:choose>>
            
            <br/>
            <tag:panel-border title="Possible Values" align="left" width="100%">
                <div id="divListContents"></div>
                Enter possible value:
                <br/>
                <tag:edit-field-simple name="listEnterPossibleValue" id="listEnterPossibleValue"  width="100%"></tag:edit-field-simple>
                <br/>
                <a href="javascript:onListEnterPossibleValue();">Add Possible Value</a>
            </tag:panel-border>
            <script language="javascript">
            renderListPossibleValues();
            </script>
        </div>
        
        <br/>
        <tag:submit value="Change Parameter" name="Submit"/>
        <div id="divFormErrorMessage" style="color:red">
        </div>
    </form>
</tag:panel>
