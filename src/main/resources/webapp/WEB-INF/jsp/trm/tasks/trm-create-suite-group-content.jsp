<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<tag:panel align="center" width="600px" 
        title="Suite Group"
    >
    <table border="0" cellpadding="10px" width="100%" cellspacing="0px">
        <tr>
            <td>
                <div class="small-description">Name:</div>
                <tag:edit-field path="name" width="100%"></tag:edit-field>
                <form:errors path="name"/>
                <form:hidden path="taskId"/>
                
            </td>
        </tr>
        <tr>
            <td>
                <div class="small-description">Description:</div>
                <form:textarea path="description" cssStyle="width:100%;" rows="10" cssErrorClass="error"/>
            </td>
        </tr>
        <tr>
            <td>
                <c:choose>
                    <c:when test="${createSuiteGroup!=null}">
                        <tag:submit value="Create"></tag:submit>
                    </c:when>
                    <c:otherwise>
                        <tag:submit value="Save"></tag:submit>
                    </c:otherwise>
                </c:choose>
                
                <form:errors path="" cssClass="error"/>
            </td>
        </tr>
    </table>
</tag:panel>
