<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
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

    
<form onsubmit="return onSubmitReportBrowseForm(this);" method="get" name="browseFilter">
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
		          Test:
		          <tag:edit-field name="testCaseName" value="${reportSearchFilter.testCaseName }" width="100%"/>
		          <br/>
		          <br/>
		          Status:
		          <ul class="flat-list">
		          <c:forEach items="${testCaseStatusList }" var="status" varStatus="statusVarStatus">
		          	<li>
		          	<input name="testCaseStatusList" id="testCaseStatusList${statusVarStatus.index+1}" type="checkbox" value="${status.key}" <c:if test="${status.checked==true}">checked="checked"</c:if>/> 
		          	<label for="testCaseStatusList${statusVarStatus.index+1}">${status.value}</label>
		          	</li>
		          </c:forEach>
		          </ul>
		          <br/>
		          Fail Reason:
		          <tag:edit-field name="testRunReason" value="${reportSearchFilter.testRunReason }"  width="100%"/>
		      </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="Suite" id="suite" align="left" width="100%"  disclosure="true" closed="false">
                Project:
                <select name="rootProject" size="6" style="width:100%;">
                    <tag:select-option value="0" style="color:gray;">Not selected</tag:select-option>
                    <c:forEach items="${rootProjects}" var="rp">
                        <tag:select-option value="${rp.id}" check="${reportSearchFilter.rootProject }"><tag:escape text="${rp.name}"/></tag:select-option>
                    </c:forEach>
                </select>
                <br/>
                
                <br/>
                Sub-Project:
                <tag:edit-field name="project" value="${reportSearchFilter.project }"  width="100%"/><br/>
                Suite:
                <tag:edit-field name="suite" value="${reportSearchFilter.suite }"  width="100%"/>
                <br/>
                Time:<br/>
                <tag:panel-border title="" align="center" width="100%">
                 <table border="0"  width="100%">
                     <tr>
                         <td class="small-description">after:</td>
                     </tr>
                     <tr>
                         <td>
                             <tag:edit-field name="suiteRunTimeAfter" id="time_after" value="${reportSearchFilter.suiteRunTimeAfter}"  width="100%"/>
                         </td>
                     </tr>
                     <tr>
                         <td class="small-description">before:</td>
                     </tr>
                     <tr>
                         <td>
                             <tag:edit-field name="suiteRunTimeBefore" id="time_before" value="${reportSearchFilter.suiteRunTimeBefore }"  width="100%"/>
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
                
                Parameters:
                <textarea name="suiteRunParameters" style="width:100%;overflow:auto;" rows="5"><tag:escape text="${reportSearchFilter.suiteRunParameters}"/></textarea>
                <br/>
                Agent:
                <tag:edit-field name="suiteRunAgent" value="${reportSearchFilter.suiteRunAgent }"  width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="User" id="user" align="left" width="100%"  disclosure="true" closed="false">
                Designer:
                <tag:edit-field name="userDesigner" value="${reportSearchFilter.userDesigner }"  width="100%"/>
                <br/>
                Runner:
                <tag:edit-field name="userRunner" value="${reportSearchFilter.userRunner}"  width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td>
            <tag:panel title="Issue" id="issue" align="left" width="100%"  disclosure="true" closed="false">
                Issue:
                <tag:edit-field name="issue" value="${reportSearchFilter.issue }" width="100%"/>
            </tag:panel>
        </td>
    </tr>
    
    <tr>
        <td align="center">
            
            <input type="hidden" name="pageOffset" value="${reportSearchFilter.pageOffset}"/>
		    <input type="hidden" name="pageLimit" value="${reportSearchFilter.pageLimit}"/>
		    <input type="hidden" name="orderByColumnId" value="${reportSearchFilter.orderByColumnId}"/>
		    <input type="hidden" name="orderDirection" value="${reportSearchFilter.orderDirection}"/>
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
</form>

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
