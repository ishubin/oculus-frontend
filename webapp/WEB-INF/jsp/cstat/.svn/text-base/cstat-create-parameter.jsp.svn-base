<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../cstat/display?id=${customStatistic.id}"><img src="../images/workflow-icon-custom-statistic.png"/><tag:escape text="${customStatistic.name}"></tag:escape> </a>
    <img src="../images/breadcrump-arrow.png"/> Create Parameter
</div>

<tag:panel align="center" title="Create Custom Statistic Parameter" logo="../images/workflow-icon-settings.png" width="600px">
    <form:form method="post" commandName="customStatisticParameter" action="../cstat/create-parameter">
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/cstat/cstat-parameter-form.jsp" %>
            <tr>
                <td align="center">
                    <form:hidden path="customStatisticId"/>
                    <tag:submit value="Create"/>
                    <br/>
                    <form:errors path="" cssClass="error"/>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel>