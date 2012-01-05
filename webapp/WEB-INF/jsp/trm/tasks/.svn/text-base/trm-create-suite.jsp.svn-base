<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="breadcrump">
    <a href="../test-run-manager/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    
    <a href="../test-run-manager/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <c:if test="${group!=null }">
        <img src="../images/breadcrump-arrow.png"/>
        <a href="../test-run-manager/edit-task?id=${task.id}&groupId=${group.id}">
            <img src="../images/workflow-icon-test-group.png"/> 
            <tag:escape text="${group.name}"/>
        </a>
    </c:if>
    
    <img src="../images/breadcrump-arrow.png"/>
    <img src="../images/workflow-icon-suite.png"/>
    Create Suite
</div>

<tag:panel title="Create suite" align="center">
    <form:form action="../test-run-manager/create-suite?taskId=${task.id}" method="post" commandName="suite">
        <table border="0" align="center">
            <tr>
                <td>Name:</td>
                <td><tag:edit-field path="name" width="100%"/></td>
            </tr>
            <tr>
                <td valign="top">Description:</td>
                <td><form:textarea path="description" cols="40" rows="7"/></td>
            </tr>
            <tr>
                <td valign="top">Group:</td>
                <td>
                    <select name="groupId">
	                    <option value="0" style="color:gray;">No group...</option>
	                    <c:forEach items="${groups}" var="g">
	                        <option value="${g.id}" <c:if test="${suite.groupId==g.id || (group!=null &&group.id == g.id)}">selected="selected"</c:if>><tag:escape text="${g.name}"/></option>
	                    </c:forEach>
	                </select>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <tag:submit value="Create"></tag:submit>
               </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <form:errors path="name" cssClass="error"/>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel> 