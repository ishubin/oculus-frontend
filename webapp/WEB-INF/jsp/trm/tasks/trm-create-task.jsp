<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<tag:panel title="Create task" align="center" width="100%">
    <form method="post">
        Name:<br/>
        <tag:edit-field name="name" value="${task.name }" width="100%"/><br/><br/>
        Description:<br/>
        <textarea name="description" style="width:100%" rows="10"><tag:escape text="${task.description }"/></textarea>
        <br/><br/>
        Project:<br/>
        <select name="projectId" size="7" style="width:100%;">
            <c:forEach items="${projects}" var="p" varStatus="pStatus">
                <option value="${p.id}"  <c:if test="${pStatus.index==0}">selected="selected"</c:if> ><tag:escape text="${p.name}"></tag:escape></option>
            </c:forEach>
        </select>  
        <tag:submit value="Create"></tag:submit><br/><br/>
        <div class="error">
        	<tag:spring-form-error field="" command="task"></tag:spring-form-error>
        </div>
    </form>
</tag:panel> 