<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<h2>Problem was found in your suite groups for this task</h2>
You have suites for different projects within the same suite group.
<br/>
Suite groups can be used only for combining suites with the same project.
Please check you suite groups in the following list:<br/>


<c:forEach items="${errorGroups}" var="group">
    <br/>
    <br/>
    <a href="../grid/edit-task?id=${group.group.taskId}&groupId=${group.group.id}">
        <img src="../images/workflow-icon-test-group.png"/>
        <tag:escape text="${group.group.name}"></tag:escape>
    </a>
    
    <tag:table columns="Suite,Project" width="100%">
        <c:forEach items="${group.suites}" var="suite">
            <tag:table-row>
                <tag:table-cell style="padding:4px;">
                    <a href="../grid/edit-suite?id=${suite.id}">
                        <c:choose>
                            <c:when test="${suite.uniteTests==true}">
                                <img src="../images/workflow-icon-united-suite.png"/>
                            </c:when>
                            <c:otherwise>
                                <img src="../images/workflow-icon-suite.png"/>
                            </c:otherwise>
                        </c:choose>
                        <tag:escape text="${suite.name}"></tag:escape>                
                    </a>
                </tag:table-cell>
                <tag:table-cell style="padding:4px;">
                    <a href="../project/display-${suite.projectPath}">
                        <img src="../images/workflow-icon-project.png"/>
                        <tag:escape text="${suite.projectName}"></tag:escape>
                    </a>
                </tag:table-cell>
            </tag:table-row>
        </c:forEach>
    </tag:table>
</c:forEach>