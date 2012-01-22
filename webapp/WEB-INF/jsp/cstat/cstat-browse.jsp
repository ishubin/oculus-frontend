<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">

    <c:if test="${project!=null}">
        <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
        <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    <span style="white-space: nowrap;"><img src="../images/workflow-icon-custom-statistic.png"/> Custom Statistics</span>

</div>


<tag:table columns=" ,Name,Date" width="100%">
	<c:forEach items="${customStatistics}" var="c">
	   <tag:table-row>
	       <tag:table-cell style="padding:4px;width:20px;">
	           <a href="../cstat/display?id=${c.id}"><img src="../images/workflow-icon-custom-statistic.png"/></a>
	       </tag:table-cell>
	       <tag:table-cell style="padding:4px;">
	           <a href="../cstat/display?id=${c.id}"><tag:escape text="${c.name}"></tag:escape></a>
	       </tag:table-cell>
	       <tag:table-cell style="padding:4px;width:100px;">
	           <tag:date date="${c.createdDate}"></tag:date>
           </tag:table-cell>
	   </tag:table-row>
	</c:forEach>
</tag:table>

