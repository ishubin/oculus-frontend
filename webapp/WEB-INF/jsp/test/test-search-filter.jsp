<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<script>
function onDateAfterChanged(date)
{
}
function onDateBeforeChanged(date)
{
}

function createParameter(name, value)
{
    var param = new Object();
    param.name = name;
    param.value = value;
    return param;
}

function isCheckboxChecked(id)
{
    var chk =  document.getElementById(id);
    if(chk!=null && chk.checked)
    {
        return true;
    }
    return false;
}
function generateRequest(form)
{
    var ps = new Array();

    for(var i=0;i<form.elements.length;i++)
    {
        if(form.elements[i].name!=null && form.elements[i].name.length>0 && form.elements[i].name!=''
            && form.elements[i].value!=null && form.elements[i].value.length>0 && form.elements[i].value!='')
        {
            if(form.elements[i].type=="checkbox")
            {
                if(form.elements[i].checked)
                {
                    var ename = form.elements[i].name; 
                    if(ename.indexOf("_pv_")==-1)
                    {
                        ps[ps.length] = createParameter(form.elements[i].name, "true");
                    }
                    else 
                    {
                        ps[ps.length] = createParameter(form.elements[i].name, "on");
                    }
                }
            }
            else
            {
                ps[ps.length] = createParameter(form.elements[i].name, form.elements[i].value);
            }
        }
    }
        
    var str = "";
    var bAmp = false;
    for(var i=0;i<ps.length;i++)
    {
        if(bAmp)str+="&";
        else str+="?";
        bAmp = true;
        str+=ps[i].name+"="+escape(ps[i].value);
    }
    return str;
}
function isRequestParameterSet(value)
{
    if(value!=null && value.length>0)
    {
        return true;
    }
    return false;
}
function redirectSearchFilterForm(form)
{
    var str = "../test/search";
    str+=generateRequest(form);
    window.location = str;
}
function onSubmitTestSearchForm(form)
{
    redirectBrowseFilterForm(form);
    return false;
}
function onSaveFilter(){
	var request = "test/search"+generateRequest(document.forms.searchFilter);
    document.forms.saveFilter.filter.value = request;
    document.forms.saveFilter.redirect.value = "../"+request;
    showPopup("divSaveFilterDialogBox", 320,300);
}
function onExportXLS(){
	var request = "../test/search-export.xls"+generateRequest(document.forms.searchFilter);
    document.forms.exportXLS.action = request;
    
    <%
    Boolean customizationsAvailable = (Boolean)pageContext.findAttribute("customizationsAvailable");
    if (customizationsAvailable) {
        out.println("showPopup(\"divExportXLSDialogBox\", 450, 500);");
    }
    else out.println("document.forms.exportXLS.submit();");
    %>
    
    return false;
}

</script>

<tag:pickuser-setup></tag:pickuser-setup>
<table border="0" align="center">
    <tr>
        <td>
            <tag:submit value="Apply Filter" onclick="redirectSearchFilterForm(document.forms.searchFilter);return false;"></tag:submit>
        </td>
        <c:if test="${user!=null}">
            <td>
                <tag:submit value="Save Filter" onclick="onSaveFilter();return false;"/>
            </td>
        </c:if>
    </tr>
</table>
<form onsubmit="return onSubmitTestSearchForm(this);" method="get" name="searchFilter" action="../test/search">
    
    <table border="0" width="100%">
        <tr>
            <td>
                <tag:panel title="Common" id="common" align="left" width="100%" disclosure="true" closed="false">
                    <p>
                        Name:<br/>
                        <tag:edit-field name="name" value="${searchFilter.name}" width="100%"/>
                    </p>
                    <p>
                        Group:<br/>
                        <tag:edit-field name="testGroup" value="${searchFilter.testGroup}" width="100%"/>
                    </p>
                    <p>
                        Automated:
                        <select name="automated">
                            <option value=""> </option>
                            <option value="1" <c:if test="${searchFilter.automated=='1' }">selected="selected"</c:if>>Yes</option>
                            <option value="0" <c:if test="${searchFilter.automated=='0' }">selected="selected"</c:if>>No</option>
                        </select>
                    </p>
                </tag:panel>
            </td>
        </tr>
        <tr>
            <td>
                <tag:panel title="User" id="user" align="left" width="100%" disclosure="true" closed="false">
                    Designer Name:<br/>
                    <tag:edit-field name="designer" value="${searchFilter.designer}" width="100%"/>
                </tag:panel>
            </td>
        </tr>
        <tr>
            <td>
                <tag:panel title="Project" id="project" align="left" width="100%" disclosure="true" closed="false">
                    Project:<br/>
                    <select name="project" size="6" style="width:100%;">
                        <tag:select-option value="0" style="color:gray;">Not selected</tag:select-option>
                        <c:forEach items="${rootProjects}" var="rp">
                            <tag:select-option value="${rp.id}" check="${searchFilter.project}"><tag:escape text="${rp.name}"/></tag:select-option>
                        </c:forEach>
                    </select>
                    <br/>
                    
                    <c:if test="${searchFilter.project!=null && searchFilter.project!='' && subprojects!=null}">
                    	Sub-Project:<br/>
                    	<select name="subProject" style="width:100%;">
	                        <tag:select-option value="" style="color:gray;">Not selected</tag:select-option>
	                        <c:forEach items="${subprojects}" var="sp">
	                            <tag:select-option value="${sp.id}" check="${searchFilter.subProject}"><tag:escape text="${sp.name}"/></tag:select-option>
	                        </c:forEach>
	                    </select>
	                    <br/>
                    </c:if>
                    
                    
                </tag:panel>
            </td>
        </tr>
        <c:if test="${searchFilter.project!=null && searchFilter.project!='' && customizations!=null}">
            <c:forEach items="${customizations}" var="cg">
                <tr>
                    <td>
                        <tag:panel align="left" title="${cg.name}" width="100%" logo="../images/workflow-icon-settings.png" disclosure="true">
                            <c:forEach items="${cg.customizations}" var="c">
                                ${c.customization.name}
                                <br/>
                                <tag:customization-search fetchConditionType="${c.fetchConditionType}" useDefaultEmptyValues="true" customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-search>
                                <br/><br/>
                            </c:forEach>
                        </tag:panel>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    
    <input type="hidden" name="pageOffset" value="${searchFilter.pageOffset}"/>
    <input type="hidden" name="pageLimit" value="${searchFilter.pageLimit}"/>
    <input type="hidden" name="orderByColumnId" value="${searchFilter.orderByColumnId}"/>
    <input type="hidden" name="orderDirection" value="${searchFilter.orderDirection}"/>
</form>
<table border="0" align="center">
    <tr>
        <td>
            <tag:submit value="Apply Filter" onclick="redirectSearchFilterForm(document.forms.searchFilter);return false;"></tag:submit>
        </td>
        <c:if test="${user!=null}">
            <td>
                <tag:submit value="Save Filter" onclick="onSaveFilter();return false;"/>
            </td>
        </c:if>
    </tr>
</table>

<div id="divExportXLSDialogBox" style="display:none">
    <tag:panel align="left" title="XLS Export Search Result" width="300px" closeDivName="divExportXLSDialogBox">
         Choose parameters for exporting
         <form name="exportXLS" action="../test/search-export.xls" method="post">
            <c:if test="${customizations!=null}">
                <table border="0" width="100%">
	                <c:forEach items="${customizations}" var="cg">
		                <tr>
		                    <td>
		                        <tag:panel-border title="${cg.name}" align="left" width="100%">
		                            <c:forEach items="${cg.customizations}" var="c">
		                                <span style="white-space: nowrap;">
			                                <input name="cexport${c.customization.id}" type="checkbox" checked="checked"/>
			                                <tag:escape text="${c.customization.name}"></tag:escape>
		                                </span><br/>
		                            </c:forEach>
		                        </tag:panel-border>   
		                    </td>
		                </tr>
		            </c:forEach>
		        </table>
		        <br/>
		        <tag:submit value="Export"></tag:submit>
            </c:if>    
         </form>    
     </tag:panel>
</div>

<c:if test="${user!=null}">
    <div id="divSaveFilterDialogBox" style="display:none;">
     <tag:panel align="left" title="Save Filter" width="300px" closeDivName="divSaveFilterDialogBox">
         <form name="saveFilter" action="../report/save-filter" method="post">
             <input type="hidden" name="filter" value=""/>
             <input type="hidden" name="redirect" value=""/>
             <table border="0" width="100%">
                <tr>
                    <td>Name:
                    <br/>
                    <tag:edit-field-simple name="name" id="saveFilterName" width="100%"/></td>
                </tr>
                <tr>
                       <td>Description:
                       <br/>
                       <tag:textarea-simple name="description" value="" style="width:100%;"/></td>
                   </tr>
                   <tr>
                       <td>
                           <tag:submit value="Save Filter" />
                       </td> 
                   </tr>
             </table>
         </form>    
     </tag:panel>
    </div>
</c:if>
