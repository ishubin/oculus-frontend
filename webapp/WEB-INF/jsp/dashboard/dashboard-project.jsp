<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div align="left">
    <div class="breadcrump">
        <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/>  ${project.name}</a>
        <img src="../images/breadcrump-arrow.png"/> 
        <img src="../images/workflow-icon-chart.png"/> Dashboard
    </div>
    <br/>
    <c:if test="${dashboard.healthChart==true}">
        <tag:panel-dashboard>
        <img src="../dashboard/chart-${healthChartId}?date=${uniqueKey}&width=500&height=${healthChartHeight}"/>
        </tag:panel-dashboard>
        <br/>
        <br/>
    </c:if>
    
	
	<c:forEach items="${subprojectCharts}" var="subprojectChart">
	    <br/>
	    <div id="${subprojectChart.project.path}"></div>
	    <tag:panel-dashboard>
	        <img src="../dashboard/chart-${subprojectChart.todayChartId}?date=${uniqueKey}&width=500&height=200" />
            <br/>
		    <img src="../dashboard/chart-${subprojectChart.statisticsChartId}?date=${uniqueKey}" />
	    </tag:panel-dashboard>
	    
	    
	    
	    <br/>
	</c:forEach>
</div>