<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<div class="breadcrump" align="center">
    <c:if test="${project!=null}">
    <a href="../project/display-${project.path}">${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    <a href="../issue/display?id=${issue.id}">
        <img src="../images/workflow-icon-bug.png"/> 
	    <c:choose>
	        <c:when test="${issue.name!=null && issue.name!=''}">
	             ${issue.name}
	        </c:when>
	        <c:otherwise>
	            ${issue.link}
	        </c:otherwise>
	    </c:choose>
    </a>
    <img src="../images/breadcrump-arrow.png"/>
    Edit Issue
</div>

<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" title="Create Issue" logo="../images/workflow-icon-bug-edit.png" width="600px">
    <form:form method="post" commandName="issue" action="../issue/edit">
        
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/issue/issue-form.jsp" %>
            <tr>
                <td align="center">
                    <input type="hidden" name="id" value="${issue.id}"/>
                    <tag:submit value="Change"/>
                    <br/>
                    <form:errors path=""/>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel>