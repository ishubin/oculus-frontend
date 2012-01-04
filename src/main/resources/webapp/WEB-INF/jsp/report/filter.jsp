<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<script language="javascript">
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

    if(isRequestParameterSet(form.testCaseName.value))  {
        ps[ps.length] = createParameter("testCaseName", form.testCaseName.value);
    }

    if(isRequestParameterSet(form.testRunReason.value))    {
        ps[ps.length] = createParameter("testRunReason", form.testRunReason.value);
    }

    if(isRequestParameterSet(form.rootProject.value))    {
        ps[ps.length] = createParameter("rootProject", form.rootProject.value);
    }
    
    if(isRequestParameterSet(form.project.value))    {
        ps[ps.length] = createParameter("project", form.project.value);
    }

    if(isRequestParameterSet(form.suite.value))    {
        ps[ps.length] = createParameter("suite", form.suite.value);
    }

    if(isRequestParameterSet(form.suiteRunTimeAfter.value))    {
        ps[ps.length] = createParameter("suiteRunTimeAfter", form.suiteRunTimeAfter.value);
    }

    if(isRequestParameterSet(form.suiteRunTimeBefore.value))    {
        ps[ps.length] = createParameter("suiteRunTimeBefore", form.suiteRunTimeBefore.value);
    }

    if(isRequestParameterSet(form.userDesigner.value))    {
        ps[ps.length] = createParameter("userDesigner", form.userDesigner.value);
    }

    if(isRequestParameterSet(form.userRunner.value))    {
        ps[ps.length] = createParameter("userRunner", form.userRunner.value);
    }

    if(isCheckboxChecked("testCaseStatusList1"))    {
        ps[ps.length] = createParameter("testCaseStatusList", "PASSED");
    }   
    if(isCheckboxChecked("testCaseStatusList3"))   {
        ps[ps.length] = createParameter("testCaseStatusList", "FAILED");
    }   
    if(isCheckboxChecked("testCaseStatusList2"))    {
        ps[ps.length] = createParameter("testCaseStatusList", "WARNING");
    }

    if(isRequestParameterSet(form.pageOffset.value))    {
        ps[ps.length] = createParameter("pageOffset", form.pageOffset.value);
    }
    if(isRequestParameterSet(form.pageLimit.value))    {
        ps[ps.length] = createParameter("pageLimit", form.pageLimit.value);
    }
    if(isRequestParameterSet(form.orderByColumnId.value))    {
        ps[ps.length] = createParameter("orderByColumnId", form.orderByColumnId.value);
    }
    if(isRequestParameterSet(form.orderDirection.value))    {
        ps[ps.length] = createParameter("orderDirection", form.orderDirection.value);
    }

    if(isRequestParameterSet(form.suiteRunParameters.value))    {
        ps[ps.length] = createParameter("suiteRunParameters", form.suiteRunParameters.value);
    }

    if(isRequestParameterSet(form.suiteRunAgent.value))    {
        ps[ps.length] = createParameter("suiteRunAgent", form.suiteRunAgent.value);
    }

    if(isRequestParameterSet(form.issue.value))    {
        ps[ps.length] = createParameter("issue", form.issue.value);
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
function redirectBrowseFilterForm(form)
{
	var str = "../report/browse";
    str+=generateRequest(form);
    window.location = str;
}
function onSubmitReportBrowseForm(form)
{
	redirectBrowseFilterForm(form);
	return false;
}
function onSaveFilter()
{
	var request = "report/browse"+generateRequest(document.forms.browseFilter);
	document.forms.saveFilter.filter.value = request;
	document.forms.saveFilter.redirect.value = "../"+request;
	showPopup("divSaveFilterDialogBox", 320,300);
}
</script>

    
<form:form onsubmit="return onSubmitReportBrowseForm(this);" method="get" name="browseFilter" commandName="reportSearchFilter">
<table border="0" align="center">
    <tr>
        <td>
            <tag:submit value="Apply Filter"></tag:submit>
        </td>
        <c:if test="${user!=null}">
            <td>
                <tag:submit value="Save Filter" onclick="onSaveFilter();return false;"/>
            </td>
        </c:if>
    </tr>
</table>
<table border="0" width="240px" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <tag:panel title="Test" id="testCase" align="left" width="100%" disclosure="true" closed="false">
          <div class="small-description">Test:</div>
          <tag:edit-field path="testCaseName" width="100%"/>
          <br/>
          <div class="small-description">Status:</div>
          <form:checkboxes path="testCaseStatusList" items="${testCaseStatusList}"
              itemValue="key" itemLabel="value" delimiter="<br/>"/>
          <br/>
          <br/>
                <div class="small-description">Fail Reason:</div>
                <tag:edit-field path="testRunReason"  width="100%"/>
      </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="Suite" id="suite" align="left" width="100%"  disclosure="true" closed="false">
                <div class="small-description">Project:</div>
                <form:select path="rootProject" size="6" cssStyle="width:100%;">
                    <form:option value="0" cssStyle="color:gray;">Not selected</form:option>
                    <c:forEach items="${rootProjects}" var="rp">
                        <form:option value="${rp.id}"><tag:escape text="${rp.name}"/></form:option>
                    </c:forEach>
                </form:select>
                <br/>
                
                <br/>
                <div class="small-description">Sub-Project:</div>
                <tag:edit-field path="project"  width="100%"/><br/>
                
                <div class="small-description">Suite:</div>
                <tag:edit-field path="suite"  width="100%"/>
                <br/>
                <tag:panel-border title="Time" align="center" width="100%">
                 <table border="0"  width="100%">
                     <tr>
                         <td class="small-description">after:</td>
                     </tr>
                     <tr>
                         <td>
                             <tag:edit-field path="suiteRunTimeAfter" id="time_after"  width="100%"/>
                         </td>
                     </tr>
                     <tr>
                         <td class="small-description">before:</td>
                     </tr>
                     <tr>
                         <td>
                             <tag:edit-field path="suiteRunTimeBefore" id="time_before"  width="100%"/>
                             <script>
                             $(function() {
                                 $("#time_before").datepicker({dateFormat: 'yy-mm-dd'});
                                 $("#time_after").datepicker({dateFormat: 'yy-mm-dd'});
                             });
                             </script>
                         </td>
                     </tr>
                 </table>
                </tag:panel-border>
                
                <div class="small-description">Parameters:</div>
                <form:textarea path="suiteRunParameters" cssStyle="width:100%;overflow:auto;" rows="5"/>
                <br/>
                <div class="small-description">Agent:</div>
                <tag:edit-field path="suiteRunAgent"  width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="User" id="user" align="left" width="100%"  disclosure="true" closed="false">
                <div class="small-description">Designer:</div>
                <tag:edit-field path="userDesigner"  width="100%"/>
                <br/>
                <div class="small-description">Runner:</div>
                <tag:edit-field path="userRunner"  width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="Issue" id="issue" align="left" width="100%"  disclosure="true" closed="false">
                <div class="small-description">Issue:</div>
                <tag:edit-field path="issue" width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td align="center">
            
            <form:hidden path="pageOffset"/>
            <form:hidden path="pageLimit"/>
            <form:hidden path="orderByColumnId"/>
            <form:hidden path="orderDirection"/>
            
            
        </td>
    </tr>
</table>
<table border="0" align="center">
        <tr>
            <td>
                <tag:submit value="Apply Filter"></tag:submit>
            </td>
            <c:if test="${user!=null}">
                <td>
                    <tag:submit value="Save Filter" onclick="onSaveFilter();return false;"/>
                </td>
            </c:if>
        </tr>
    </table>
</form:form>

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
