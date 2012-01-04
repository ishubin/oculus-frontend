<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page import="net.mindengine.oculus.frontend.domain.project.Project"%>
<%@page import="java.net.URLEncoder"%><jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="left-panel">
    <c:if test="${user.hasPermissions.cstat_managment==true}">
        <b>Work-flow:</b><br/><br/>
        <table border="0" width="100%" cellspacing="5px">
            <tr>
                <td>
                    <tag:workflow-panel title="Custom Statistic" width="100%" id="workflow_CSM">
                        <tag:workflow-element icon="create" link="../cstat/create?projectId=${project.id}">Create</tag:workflow-element>
                        <c:if test="${customStatistic!=null}">
                            <tag:workflow-element icon="edit" link="../cstat/edit?id=${customStatistic.id}">Edit</tag:workflow-element>
                            <tag:workflow-element icon="delete" link="../cstat/delete?id=${customStatistic.id}" onclick="return confirm('Are you sure you want to delete this statistic');">Delete</tag:workflow-element>
                        </c:if>
                    </tag:workflow-panel>
                </td>
            </tr>
            
            <c:if test="${customStatistic!=null}">
                <tr>
	                <td>
	                    <tag:workflow-panel title="Parameters" width="100%" id="workflow_CSM">
	                        <tag:workflow-element icon="settings" link="../cstat/create-parameter?customStatisticId=${customStatistic.id}">Create Parameter</tag:workflow-element>
	                        <c:if test="${customStatisticParameter!=null}">
	                            <tag:workflow-element icon="edit" link="../cstat/edit-parameter?id=${customStatisticParameter.id}">Edit</tag:workflow-element>
	                            <tag:workflow-element icon="delete" link="../cstat/delete-parameter?id=${customStatisticParameter.id}" onclick="return confirm('Are you sure you want to delete this statistic');">Delete</tag:workflow-element>
	                        </c:if>
	                    </tag:workflow-panel>
	                </td>
	            </tr>
	            
	            <tr>
                    <td>
                        <tag:workflow-panel title="Charts" width="100%" id="workflow_CSM">
                            <tag:workflow-element icon="create" link="../cstat/create-chart?customStatisticId=${customStatistic.id}">Create Chart</tag:workflow-element>
                            
                        </tag:workflow-panel>
                    </td>
                </tr>
            </c:if>
        </table>
    </c:if>    
</div>