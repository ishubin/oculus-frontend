<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}">${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/>
    <a href="../customization/project-${project.path}">Choose Unit</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../customization/project-${project.path}?unit=${unit}">Customize Unit</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Add Parameter for 
    <c:choose>
        <c:when test="${unit == 'project'}">
            Project
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'test'}">
            Test
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'test-case'}">
            Test-Case
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${unit == 'issue'}">
            Issue
        </c:when>
    </c:choose>
</div>

<script language="javascript">
function onAddParameterTypeChange(el)
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


<tag:panel align="center" title="Add Parameter" width="500px">
    <form onsubmit="return formValidation(this)" action="../customization/add-parameter" method="post">
        Type: 
	    <select name="type" onchange="onAddParameterTypeChange(this);">
	        <option value="text">Text</option>
	        <option value="assignee">Assignee</option>
	        <option value="list">List</option>
	        <option value="checkbox">CheckBox</option>
	        <option value="checklist">CheckList</option>
	    </select>
	    <br/>
	    <br/>
        <input type="hidden" name="projectId" value="${project.id}"/>
        <input type="hidden" name="unit" value="${unit}"/>
        Name: <br/>
        <tag:edit-field-simple name="name" id="edtName" width="100%"></tag:edit-field-simple>
        <br/>
        Description: <br/>
        <textarea rows="4" style="width:100%;" name="description"></textarea>
        <br/>
        Group: <br/>
        <tag:edit-field-simple name="groupName" id="edtGroupName" width="100%"></tag:edit-field-simple>
        
	    
	    <div id="divListSubtype" style="display:none;">
	       Sub-type:
            <select name="listSubtype">
               <option value="drop-down">Drop-down list</option>
               <option value="list">Classic list</option>
            </select>
	    </div>
	    <div id="divListLayout" style="display:none;">
	        
	        <br/>
	        <tag:panel-border title="Possible Values" align="left" width="100%">
		        <script language="javascript">
		        var listPossibleValues = new Array();
		        
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
		        </script>
		        
		        <div id="divListContents"></div>
		        Enter possible value:
		        <br/>
		        <tag:edit-field-simple name="listEnterPossibleValue" id="listEnterPossibleValue"  width="100%"></tag:edit-field-simple>
		        <br/>
		        <a href="javascript:onListEnterPossibleValue();">Add Possible Value</a>
	        </tag:panel-border>
	    </div>
	    
	    <br/>
	    <tag:submit value="Add Parameter" name="Submit"/>
	    <div id="divFormErrorMessage" style="color:red">
	    </div>
    </form>
</tag:panel>
