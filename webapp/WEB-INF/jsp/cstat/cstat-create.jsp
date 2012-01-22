<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <img src="../images/workflow-icon-custom-statistic.png"/> Create Custom Statistic
</div>

<tag:panel align="center" title="Create Custom Statistic" logo="../images/workflow-icon-custom-statistic.png" width="600px">
    <form method="post" action="../cstat/create">
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/cstat/cstat-form.jsp" %>
            <tr>
                <td align="center">
                    <input type="hidden" name="projectId" value="${customStatistic.projectId}"/>
                    <tag:submit value="Create"/>
                    <br/>
                    <div class="error">
                    	<tag:spring-form-error field="" command="customStatistic"></tag:spring-form-error>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel>