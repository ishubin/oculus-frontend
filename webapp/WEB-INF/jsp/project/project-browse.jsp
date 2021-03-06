<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.HashMap"/>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter"/>


<%
//Preparing the Page Limit drop-down list model
{
    ProjectBrowseFilter filter = (ProjectBrowseFilter)pageContext.findAttribute("projectBrowseFilter");
    
    
    List list = new ArrayList();
    
    for(int i=0;i<ProjectBrowseFilter.PAGE_LIMITS.length;i++)
    {
        Map map = new HashMap();
        map.put("label",ProjectBrowseFilter.PAGE_LIMITS[i]);
        map.put("id",i);
        if(filter.getPageLimit().equals(i))
        {
            map.put("selected",true);
        }
        else map.put("selected",false);
        list.add(map);
    }
    
    pageContext.setAttribute("pageLimitOptions",list);
}
%>
<%@page import="net.mindengine.oculus.frontend.domain.project.ProjectBrowseFilter"%>
<div class="breadcrump" align="center">Projects</div>


<b>Projects found: </b>${projectBrowseResult.numberOfResults}
<script language="javascript" >
function onPageLimitChange(select)
{
    document.forms.projectBrowseFilter.pageLimit.value = select.options[select.selectedIndex].value;
    document.forms.projectBrowseFilter.submit();
}
function onPageClick(id)
{
    document.forms.projectBrowseFilter.pageOffset.value = id;
    document.forms.projectBrowseFilter.submit();
}
</script>
<tag:pagination numberOfResults="${projectBrowseResult.numberOfResults}" 
                onPageScript="onPageClick" 
                onPageLimitScript="onPageLimitChange"
                currentPage="${projectBrowseFilter.pageOffset}"
                pageLimit="${projectBrowseFilter.pageLimit}"
                pageLimitArray="${projectBrowseFilter.pageLimitArray}"
                />
<br/>
<br/>
<c:forEach items="${projectBrowseResult.projects}" var="p">
    <div class="project-layout" onclick="window.location='../project/display-${p.path}'">
	    <table class="project-browse-project" border="0" width="100%" cellpadding="0" cellspacing="0">
	        <tr>
	            <td width="140px" height="140px" valign="middle" align="center">
	               <a href="../project/display-${p.path}">
	                   <c:choose>
		                    <c:when test="${p.icon!=null && p.icon!=''}">
		                        <img src="../display/file?type=project-icon&projectId=${p.id}&object=${p.icon}"/>
		                    </c:when>
		                    <c:otherwise>
		                        <img src="../images/project-no-icon.png"/>
		                    </c:otherwise>
	                   </c:choose>
	                </a>
	            </td>
	            <td valign="top">
	                <table border="0" cellpadding="0" width="100%" height="100%" cellspacing="0">
	                    <tr>
	                        <td height="10px" valign="top" align="left">
	                            <a class="big-text" href="../project/display-${p.path}">${p.name}</a>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td valign="top" align="left">
	                            <div class="small" style="width:100%;height:100%;">
				                    <tag:bbcode-render>${p.description}</tag:bbcode-render>
				                    <br/>
				                    <br/>
				                    SubProjects: <b>${p.subprojectsCount}</b>
				                </div>
	                        </td>
	                    </tr>
	                </table>
	            </td>
	        </tr>
	    </table>
	</div>    
    <br/>
</c:forEach>