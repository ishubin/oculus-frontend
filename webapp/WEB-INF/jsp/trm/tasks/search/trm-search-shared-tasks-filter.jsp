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
    var str = "../grid/shared-tasks";
    str+=generateRequest(form);
    window.location = str;
}
function onSubmitTaskSearchForm(form)
{
    redirectBrowseFilterForm(form);
    return false;
}
</script>

<tag:pickuser-setup></tag:pickuser-setup>

<form onsubmit="return onSubmitTaskSearchForm(this);" method="get" name="searchFilter" action="../grid/shared-tasks" >
    <tag:submit value="Apply Filter" onclick="redirectSearchFilterForm(document.forms.searchFilter);return false;"></tag:submit>
    <table border="0" width="100%">
        <tr>
            <td>
                <tag:panel title="Common" id="common" align="left" width="100%" disclosure="true" closed="false">
                    Name:<br/>
                    <tag:edit-field name="name" value="${searchFilter.name }" width="100%"/>
                    
                </tag:panel>
            </td>
        </tr>
        <tr>
            <td>
                <tag:panel title="User" id="user" align="left" width="100%" disclosure="true" closed="false">
                    User Name:<br/>
                    <tag:edit-field name="user" value="${searchFilteruser}" width="100%"/>
                </tag:panel>
            </td>
        </tr>
        
    </table>
    
    <input type="hidden" name="pageOffset" value="${searchFilter.pageOffset}"/>
    <input type="hidden" name="pageLimit" value="${searchFilter.pageLimit}"/>
    <input type="hidden" name="orderByColumnId" value="${searchFilter.orderByColumnId}"/>
    <input type="hidden" name="orderDirection" value="${searchFilter.orderDirection}"/>
    
    <tag:submit value="Apply Filter" onclick="redirectSearchFilterForm(document.forms.searchFilter);return false;"></tag:submit>
</form>