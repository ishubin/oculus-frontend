<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<%@ include file="/WEB-INF/jsp/trm/tasks/trm-agents-filter.jsp"%>
<tag:pickbuild-setup/>



<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../grid/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <img src="../images/breadcrump-arrow.png"/> 
    Prepare Task for Run
</div>     
            
<div class="small-description">
    Customize task parameters and when you are done click the "Run Task"
    
</div>



<form method="post" name="formTasks">
    <input type="hidden" name="Submit" value="Run Task"/>
    <input type="hidden" name="selectedAgents" value=""/>
    
    
    <tag:panel align="left" title="${task.name}" width="100%" height="100%" logo="../images/workflow-icon-task.png">
        <table border="0" width="100%" cellpadding="0px" cellspacing="0px">
            <tr>
                <td valign="top" align="left">
                    <tag:escape text="${task.description}"/>
                    <br/>
                    <table border="0" cellpadding="5px" cellspacing="0px">
                        <tr>
                            <td class="small-description"><img src="../images/workflow-icon-settings.png"/> Build:</td>
                            <td>
                                <tag:pickbuild-button build="${task.build }" id="build" projectId="${task.projectId}"></tag:pickbuild-button>
                            </td>
                        </tr>
                        <c:forEach items="${task.parameters}" var="parameter">
                        <tr>
                            <td class="small-description"><img src="../images/workflow-icon-settings.png"/> ${parameter.name}:</td>
                            <td>
                            <c:choose>
	                             <c:when test="${parameter.subtype == 'text'}">
	                                 <tag:edit-field-simple name="sp_${parameter.id}" id="sp_${parameter.id}" value="${parameter.taskValue}"/>
	                             </c:when>
	                             <c:when test="${parameter.subtype == 'list'}">
	                                 <select name="sp_${parameter.id}" id="sp_${parameter.id}" style="width:100%;">
	                                     <c:forEach items="${parameter.possibleValuesList}" var="possibleValue">
	                                         <option value="${possibleValue}" <c:if test="${parameter.taskValue == possibleValue }">selected="selected"</c:if> >${possibleValue}</option>
	                                     </c:forEach>
	                                 </select>
	                             </c:when>
	                             <c:when test="${parameter.subtype == 'boolean'}">
	                                 <input type="checkbox" name="sp_${parameter.id}" id="sp_${parameter.id}" <c:if test="${parameter.taskValue=='true' }">checked="checked"</c:if> />
	                             </c:when>
	                             <c:otherwise>Undefined Control</c:otherwise>
	                         </c:choose>
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </table>
    </tag:panel>
    
    <h2>Agents</h2>
    <c:forEach items="${agents}" var="agent">
		<div class="run-task-agent-layout" agent-name="${agent.agentInformation.name}">
	    	<tag:agent-layout agent="${agent}"/>
	    </div>
	</c:forEach>
</form>
