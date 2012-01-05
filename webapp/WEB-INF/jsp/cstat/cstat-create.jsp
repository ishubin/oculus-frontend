<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    <img src="../images/workflow-icon-custom-statistic.png"/> Create Custom Statistic
</div>

<tag:panel align="center" title="Create Custom Statistic" logo="../images/workflow-icon-custom-statistic.png" width="600px">
    <form:form method="post" commandName="customStatistic" action="../cstat/create">
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/cstat/cstat-form.jsp" %>
            <tr>
                <td align="center">
                    <form:hidden path="projectId"/>
                    <tag:submit value="Create"/>
                    <br/>
                    <form:errors path="" cssClass="error"/>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel>