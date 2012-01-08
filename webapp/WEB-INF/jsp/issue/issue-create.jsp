<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <img src="../images/workflow-icon-bug.png"/> Create Issue
</div>

<tag:pickuser-setup></tag:pickuser-setup>

<tag:panel align="center" title="Create Issue" logo="../images/workflow-icon-bug.png" width="600px">
    <form method="post" action="../issue/create">
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/issue/issue-form.jsp" %>
            <tr>
                <td align="center">
                    <tag:submit value="Create"/>
                    <br/>
                    <div class="error">
                    	<tag:spring-form-error field="" command="issue"/>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel>