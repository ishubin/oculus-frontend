<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<div class="breadcrump">
    <a href="../grid/my-tasks">My Tasks</a>
    <img src="../images/breadcrump-arrow.png"/>
    
    <a href="../grid/edit-task?id=${task.id}">
        <img src="../images/workflow-icon-task.png"/> 
        <tag:escape text="${task.name}"/>
    </a>
    <c:if test="${group!=null }">
        <img src="../images/breadcrump-arrow.png"/>
        <a href="../grid/edit-task?id=${task.id}&groupId=${group.id}">
            <img src="../images/workflow-icon-test-group.png"/> 
            <tag:escape text="${group.name}"/>
        </a>
    </c:if>
    
    <img src="../images/breadcrump-arrow.png"/>
    <img src="../images/workflow-icon-suite.png"/>
    Create Suite
</div>

<tag:panel title="Create suite" align="center">
    <form action="../grid/create-suite?taskId=${task.id}" method="post">
        <table border="0" align="center">
            <tr>
                <td>Name:</td>
                <td><tag:edit-field name="name" width="100%" value="${suite.name}"/></td>
            </tr>
            <tr>
                <td valign="top">Description:</td>
                <td><textarea name="description" cols="40" rows="7"><tag:escape text="${suite.description}"/></textarea></td>
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
                    <div class="error">
                    	<tag:spring-form-error field="" command="suite"></tag:spring-form-error>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel> 