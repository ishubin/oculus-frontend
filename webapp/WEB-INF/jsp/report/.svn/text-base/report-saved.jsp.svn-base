<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>



<table border="0" class="browse-report" width="100%">
	<thead>
	    <tr>
	        <td>Log</td>
	        <td>Test Run Id</td>
	        <td>Test Name</td>
	        <td>Project</td>
	        <td>Status</td>
	        <td>Designer</td>
	        <td>Runner</td>
	        <td>Start Time</td>
	    </tr>
	</thead>
	<tbody>
	    <c:forEach items="${savedTestRuns}" var="row">
	    <tr class="row-${row.testRunStatus}">
	        <td><a href="../report/report-${row.testRunId}">Log</a></td>
	        <td>${row.testRunId}</td>
	        <td>${row.fetchTestName}</td>
	        <td>${row.fetchProjectName}</td>
	        <td>${row.testRunStatus}</td>
	        <td><a href="../user/profile-${row.runnerLogin}">${row.runnerName}</a></td>
	        <td><a href="../user/profile-${row.designerLogin}">${row.designerName}</a></td>
	        <td><tag:date date="${row.testRunStartTime}"/></td>
	    </tr>
	    </c:forEach>
    </tbody>
</table>