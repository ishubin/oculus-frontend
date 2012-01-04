<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">

    <c:if test="${project!=null}">
        <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
        <img src="../images/breadcrump-arrow.png"/> 
        <a href="../cstat/browse?projectId=${project.id}"><img src="../images/workflow-icon-custom-statistic.png"/> Custom Statistics</a>
        <img src="../images/breadcrump-arrow.png"/>
    
    </c:if>
    <span style="white-space: nowrap;"><img src="../images/workflow-icon-custom-statistic.png"/> <tag:escape text="${customStatistic.name}"></tag:escape></span>

</div>

<tag:escape text="${customStatistic.description}"></tag:escape>

<br/>
<br/>
<b>Parameters:</b><br/>
<tag:table columns="Name,Short name${user.hasPermissions.cstat_managment==true?', , ':''}" width="100%">
    <c:forEach items="${parameters}" var="p">
        <tag:table-row>
            <tag:table-cell style="padding:4px">
                <img src="../images/workflow-icon-settings.png"/> <tag:escape text="${p.name}"/>
            </tag:table-cell>
            <tag:table-cell style="padding:4px">
                <tag:escape text="${p.shortName}"/>
            </tag:table-cell>
            <c:if test="${user.hasPermissions.cstat_managment==true}">
                <tag:table-cell style="padding:4px" width="100px">
                    <a href="../cstat/edit-parameter?id=${p.id}"><img src="../images/workflow-icon-edit.png"/> Edit</a>
                </tag:table-cell>
                <tag:table-cell style="padding:4px" width="100px">
                    <a href="../cstat/delete-parameter?id=${p.id}" onclick="return confirm('Are you sure you want to delete this parameter?');"><img src="../images/workflow-icon-delete.png"/> Delete</a>
                </tag:table-cell>
            </c:if>
        </tag:table-row>
    </c:forEach>
</tag:table>

<c:if test="${fn:length(charts) > 0}">
    <br/>
    <b>Charts:</b><br/>
    <tag:table columns="Name${user.hasPermissions.cstat_managment==true?', , ':''}" width="100%">
        <c:forEach items="${charts}" var="chart">
            <tag:table-row>
                <tag:table-cell>
                    <a href="../cstat/display-chart?id=${chart.id}">
                        <img src="../images/workflow-icon-report.png"/>
                        <tag:escape text="${chart.name}"/>
                    </a>
                </tag:table-cell>
                <c:if test="${user.hasPermissions.cstat_managment==true}">
                    <c:if test="${user.hasPermissions.cstat_managment==true}">
		                <tag:table-cell style="padding:4px" width="100px">
		                    <a href="../cstat/edit-chart?id=${chart.id}"><img src="../images/workflow-icon-edit.png"/> Edit</a>
		                </tag:table-cell>
		                <tag:table-cell style="padding:4px" width="100px">
		                    <a href="../cstat/delete-chart?id=${chart.id}" onclick="return confirm('Are you sure you want to delete this chart?');"><img src="../images/workflow-icon-delete.png"/> Delete</a>
		                </tag:table-cell>
		            </c:if>
                </c:if>
            </tag:table-row>
        </c:forEach>
    </tag:table>
</c:if>
