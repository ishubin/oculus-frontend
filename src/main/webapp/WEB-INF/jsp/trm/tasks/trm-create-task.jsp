<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<tag:panel title="Create task" align="center">
    <form:form method="post" commandName="task">
        Name:<br/>
        <tag:edit-field path="name" width="100%"/><br/><br/>
        Description:<br/>
        <form:textarea path="description" cols="40" rows="10"/><br/><br/>
        Project:<br/>
        <select name="projectId" size="7" style="width:100%;">
            <c:forEach items="${projects}" var="p" varStatus="pStatus">
                <option value="${p.id}"  <c:if test="${pStatus.index==0}">selected="selected"</c:if> ><tag:escape text="${p.name}"></tag:escape></option>
            </c:forEach>
        </select>  
        <tag:submit value="Create"></tag:submit><br/><br/>
        <form:errors path="name" cssClass="error"/>
    </form:form>
</tag:panel> 