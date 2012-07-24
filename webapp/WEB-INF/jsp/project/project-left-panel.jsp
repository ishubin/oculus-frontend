<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<%@page import="net.mindengine.oculus.frontend.domain.project.Project"%>
<%@page import="java.net.URLEncoder"%><jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<%//Encoding the project name into URL
Project project = (Project)pageContext.findAttribute("project");
if(project!=null)
{
    pageContext.setAttribute("projectNameEncoded",URLEncoder.encode(project.getName(),"UTF-8"));
}
%>

<div class="left-panel">
    <b>Work-flow:</b><br/><br/>
    <table border="0" width="100%" cellspacing="5px">
        <c:if test="${project.id>0}">
            <tr>
                <td>
                    <tag:workflow-panel title="Project Links" width="100%" id="workflow_PL">
                    	<c:if test="${user!=null }">
                        <tag:workflow-element icon="library" link="../document/project-${project.path}">Documentation Library</tag:workflow-element>
                        </c:if>
                        <c:if test="${project.parentId>0}">
                            <tag:workflow-element icon="search" link="../test/search?project=${project.parentId}&subProject=${project.id}">Tests</tag:workflow-element>
                        </c:if>
                        <c:if test="${project.parentId==0}">
                            <tag:workflow-element icon="search" link="../test/search?project=${project.id}">Tests</tag:workflow-element>
                            <tag:workflow-element icon="build" link="../project/builds-${project.path}">Builds</tag:workflow-element>
                            <c:if test="${user.hasPermissions.project_managment == true}">
                               <tag:workflow-element icon="settings" link="../customization/project-${project.path}">Customize Project</tag:workflow-element>
                            </c:if>
                        </c:if>
                        <c:choose>
                            <c:when test="${project.parentId==0}">
                                <tag:workflow-element icon="report" link="../report/browse?rootProject=${project.id}">Reports</tag:workflow-element>
                            </c:when>
                            <c:otherwise>
                                <tag:workflow-element icon="report" link="../report/browse?project=${project.id}">Reports</tag:workflow-element>
                            </c:otherwise>
                        </c:choose>
                        
                            
                    </tag:workflow-panel>
                </td>
            </tr>
        </c:if>
        <c:if test="${user.hasPermissions.project_managment == true && (project.parentId==null || project.parentId==0)}">
            <tr>
	            <td>
                    <tag:workflow-panel title="Project Management" width="100%" id="workflow_PM" icon="project">
                        <c:if test="${project==null}">
                            <tag:workflow-element icon="create" link="../project/create">New Project</tag:workflow-element>
                        </c:if>
	                    <c:if test="${project.id>0}">
	                       <tag:workflow-element icon="edit" link="../project/edit?id=${project.id}">Edit Project</tag:workflow-element>
	                       <tag:workflow-element icon="delete" link="../project/delete?id=${project.id}" onclick="if(confirm('Are you sure you want delete this project'))return true; else return false;">Delete Project</tag:workflow-element>
	                    </c:if>
	                </tag:workflow-panel>
	            </td>
	        </tr>
        </c:if>
                
        <c:if test="${(user.hasPermissions.subproject_managment == true )&&(project.id>0)}">
            <tr>
                <td>
		            <tag:workflow-panel title="Sub-Project Management" width="100%" id="workflow_SM">
	                    <c:if test="${project.id>0 && (project.parentId==null || project.parentId==0)}">
	                        <tag:workflow-element icon="subcreate" link="../project/create?parentId=${project.id}">New Sub-Project</tag:workflow-element>
		                </c:if>
		                <c:if test="${project.parentId>0}">
		                    <tag:workflow-element icon="edit" link="../project/edit?id=${project.id}">Edit Subproject</tag:workflow-element>
		                    <tag:workflow-element icon="delete" link="../project/delete?id=${project.id}" onclick="if(confirm('Are you sure you want delete this sub-project'))return true; else return false;">Delete Sub-Project</tag:workflow-element>
		                </c:if>
		            </tag:workflow-panel>
	            </td>
	        </tr>
	    </c:if>
	    
	    <c:if test="${user.hasPermissions.issue_managment == true && project.parentId==0}">
	       <tr>
                <td>
                    <tag:workflow-panel title="Issue Management" width="100%" id="workflow_IM" icon="bug">
                        <tag:workflow-element icon="bug-create" link="../issue/create?projectId=${project.id}">Create Issue</tag:workflow-element>
                        <tag:workflow-element icon="search" link="../issue/search?project=${project.id}">All Project Issues</tag:workflow-element>
                    </tag:workflow-panel>
                </td>
           </tr>
	    </c:if>
	    
	    
	    <c:if test="${user.hasPermissions.test_managment == true && project.id>0 && project.parentId > 0}">
	        <tr>
	           <td>
	               <%@ include file="/WEB-INF/jsp/test/test-workflow.jsp" %>
	           </td>
	        </tr>
	    </c:if>
	    
        
    </table>
</div>