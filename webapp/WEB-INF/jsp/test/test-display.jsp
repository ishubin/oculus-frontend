<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${parentProject.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${parentProject.name}"/></a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-subproject.png"/> <tag:escape text="${project.name}"/></a>
    <c:if test="${test.groupId>0}">
        <img src="../images/breadcrump-arrow.png"/>
        <a href="../project/display-${project.path}?groupId=${test.groupId}">
            <img src="../images/iconTestGroup.png"/>
            <tag:escape text="${test.groupName}"/>
        </a>
    </c:if>
    <img src="../images/breadcrump-arrow.png"/> 
    <span style="white-space: nowrap;"><img src="../images/iconTest.png"/> ${test.name}</span>
</div>

<table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <td class="issue-table" colspan="2">
                Test - <b><tag:escape text="${test.name}"/></b>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr class="odd">
            <td class="issue-table-left">
                Name
            </td>
            <td class="issue-table">
               <tag:escape text="${test.name}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Description
            </td>
            <td class="issue-table">
                <tag:bbcode-render>${test.description}</tag:bbcode-render>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Mapping
            </td>
            <td class="issue-table">
                <tag:escape text="${test.mapping}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Project
            </td>
            <td class="issue-table">
                <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Author
            </td>
            <td class="issue-table">
                <a href="../user/profile-${testAuthor.login}">
                    <img src="../images/workflow-icon-user.png"/>
                    <tag:escape text="${testAuthor.name}"/>
                </a>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Group
            </td>
            <td class="issue-table">
                <c:if test="${test.groupId>0}">
                    <a href="../project/display-${project.path}?groupId=${test.groupId}">
                        <img src="../images/workflow-icon-test-group.png"/>
                        <tag:escape text="${test.groupName}"/>
                    </a>
                </c:if>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Automated
            </td>
            <td class="issue-table">
                <c:choose>
                    <c:when test="${test.automated==true }">Yes</c:when>
                    <c:otherwise>No</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <c:if test="${testInputParametersCount>0}">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <img src="../images/workflow-icon-settings.png"/> Input Parameters
                </td>
            </tr>
            <c:forEach items="${testInputParameters}" var="p" varStatus="pVarStatus">
                 <tr class="${pVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         <tag:escape text="${p.name}"></tag:escape>
                     </td>
                     <td class="issue-table">
                         <tag:nl2br><tag:escape text="${p.description}"></tag:escape></tag:nl2br>
                     </td>
                 </tr>
            </c:forEach>
        </c:if>
        <c:if test="${testOutputParametersCount>0}">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <img src="../images/workflow-icon-settings.png"/> Output Parameters
                </td>
            </tr>
            <c:forEach items="${testOutputParameters}" var="p" varStatus="pVarStatus">
                 <tr class="${pVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         <tag:escape text="${p.name}"></tag:escape>
                     </td>
                     <td class="issue-table">
                         <tag:nl2br><tag:escape text="${p.description}"></tag:escape></tag:nl2br>
                     </td>
                 </tr>
            </c:forEach>
        </c:if>
        <c:forEach items="${customizationGroups}" var="cg">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <c:choose>
                        <c:when test="${cg.isMain==true}">
                             Main
                        </c:when>
                        <c:otherwise>
                             ${cg.name}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:forEach items="${cg.customizations}" var="c" varStatus="cVarStatus">
                 <tr class="${cVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         <tag:escape text="${c.customization.name}"/>
                     </td>
                     <td class="issue-table">
                         <tag:customization-display customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"/>
                     </td>
                 </tr>
            </c:forEach>
        </c:forEach>
    </tbody>
</table>

<c:if test="${testContent!=null && testContent.steps!=null}">
	<p>
		<table class="document-test-case-table-preview" cellspacing="0">
			<thead>
				<tr>
					<td>Step</td>
					<td width="50%">Action</td>
					<td width="50%">Expected</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${testContent.steps}" var="step" varStatus="stepStatus">
					<tr>
						<td class="dtctp-number">${stepStatus.index+1}</td>
						<td class="dtctp-content">${step.action}</td>
						<td class="dtctp-content">${step.expected}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</p>
</c:if>

<!-- Comments layout -->
<br/>
<tag:comments-layout user="${user}" comments="${comments}" redirect="../test/display?id=${test.id}" unitId="${test.id}" unit="test"></tag:comments-layout>
