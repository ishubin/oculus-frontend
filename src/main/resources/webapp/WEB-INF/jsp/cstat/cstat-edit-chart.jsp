<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
    <img src="../images/breadcrump-arrow.png"/> 
    
    <a href="../cstat/display?id=${customStatistic.id}"><img src="../images/workflow-icon-custom-statistic.png"/><tag:escape text="${customStatistic.name}"></tag:escape> </a>
    <img src="../images/breadcrump-arrow.png"/> 
    
    <img src="../images/workflow-icon-settings.png"/><tag:escape text="${customStatisticChart.name}"></tag:escape>
    <img src="../images/breadcrump-arrow.png"/> Edit Chart
</div>

<tag:panel align="center" title="Edit Custom Statistic Chart" width="600px">
    <form:form method="post" commandName="customStatisticChart" action="../cstat/edit-chart">
        <form:hidden path="id"/>
        <table border="0" width="100%">
            <%@ include file="/WEB-INF/jsp/cstat/cstat-chart-form.jsp" %>
            <tr>
                <td align="center">
                    <form:hidden path="customStatisticId"/>
                    <tag:submit value="Change"/>
                    <br/>
                    <form:errors path=""/>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel>