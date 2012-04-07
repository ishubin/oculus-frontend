<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <c:if test="${parentProject!=null}">
        <a href="../project/display-${parentProject.path}"><img src="../images/workflow-icon-project.png"/> ${parentProject.name}</a>
        <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    
    
    <c:choose>
        <c:when test="${group==null}">
            <img src="../images/workflow-icon-${parentProject==null?'project':'subproject'}.png"/> ${project.name}
        </c:when>
        <c:otherwise>
            <a href="../project/display-${project.path}">
                <img src="../images/workflow-icon-${parentProject==null?'project':'subproject'}.png"/> ${project.name}
            </a>
            <img src="../images/breadcrump-arrow.png"/>
            <img src="../images/workflow-icon-test-group.png"/> ${group.name}
        </c:otherwise>
    </c:choose>
    
</div>

<table border="0" cellspacing="0" cellpadding="2" width="100%">
    <tr>
        <td width="120px" valign="top">
            <c:choose>
                <c:when test="${project.icon!=null && project.icon!=''}">
                    <img src="../display/file?type=project-icon&projectId=${project.id}&object=${project.icon}"/>
                </c:when>
                <c:otherwise>
                    <img src="../images/project-no-icon.png"/>
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <td class="issue-table" colspan="2">
                            <c:if test="${parentProject!=null}"><a href="../project/display-${parentProject.path}">${fn:escapeXml(parentProject.name)}</a> - </c:if> 
                            <b>${fn:escapeXml(project.name)}</b>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="odd">
                        <td class="issue-table-left">
                            #Path:
                        </td>
                        <td class="issue-table">
                            ${project.path}
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            #ID:
                        </td>
                        <td class="issue-table">
                            ${project.id}
                        </td>
                    </tr>
                    <tr class="odd">
                        <td class="issue-table-left">
                            Name:
                        </td>
                        <td class="issue-table">
                            ${fn:escapeXml(project.name)}
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            Description:
                        </td>
                        <td class="issue-table">
                            <tag:bbcode-render>${project.description}</tag:bbcode-render>
                        </td>
                    </tr>
                    <tr class="odd">
                        <td class="issue-table-left">
                            SubProjects:
                        </td>
                        <td class="issue-table">
                            ${project.subprojectsCount}
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            Tests:
                        </td>
                        <td class="issue-table">
                            ${project.testsCount} 
                        </td>
                    </tr>
                    <tr class="odd">
                        <td class="issue-table-left">
                            Author:
                        </td>
                        <td class="issue-table">
                            <a href="../user/profile-${fn:escapeXml(projectAuthor.login)}">
                                <img src="../images/workflow-icon-user.png"/>
                                ${fn:escapeXml(projectAuthor.name)}
                            </a> 
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            Date:
                        </td>
                        <td class="issue-table">
                            <tag:date date="${project.date}"/> 
                        </td>
                    </tr>
                    <c:if test="${parentProject!=null}">
                        <tr class="odd">
                            <td class="issue-table-left">
                                Parent project:
                            </td>
                            <td class="issue-table">
                                <a href="../project/display-${fn:escapeXml(parentProject.path)}">${fn:escapeXml(parentProject.name)}</a> 
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${customizationGroups}" var="cg">
                        <tr>
                            <td class="issue-table-separator" colspan="2">
                                <c:choose>
                                    <c:when test="${cg.isMain==true}">
                                         Main
                                    </c:when>
                                    <c:otherwise>
                                         ${fn:escapeXml(cg.name)}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:forEach items="${cg.customizations}" var="c" varStatus="cVarStatus">
                             <tr class="${cVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                                 <td class="issue-table-left">
                                     ${fn:escapeXml(c.customization.name)}
                                 </td>
                                 <td class="issue-table">
                                     <tag:customization-display customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"/>
                                 </td>
                             </tr>
                        </c:forEach>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<c:if test="${subprojects!=null}">
    <br/>
    <br/>
    <table id="subprojectsTable" border="0">
        <tr>
            <th> </th>
            <th>Name</th>
            <th>Tests</th>
            <th>Created</th>
        </tr>
        <tbody>
            <c:forEach items="${subprojects}" var="sp">
                <tr>
                    <td width="60px" style="vertical-align:top;text-align:left;"><a href="../project/display-${sp.path}"><c:choose><c:when test="${sp.icon!=null && sp.icon!=''}"><img width="60px" src="../display/file?type=project-icon&projectId=${sp.id}&object=${sp.icon}"/></c:when><c:otherwise><img width="60px" src="../images/project-no-icon.png"/></c:otherwise></c:choose></a></td>
                    <td><a class="big-link" href="../project/display-${sp.path}"><tag:escape text="${sp.name}"/></a></td>
                    <td>${sp.testsCount}</td>
                    <td><tag:date date="${sp.date}"></tag:date></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <script>
    $(document).ready(function(){
    	tableToGrid("#subprojectsTable",{
            height:'auto',
            width:'auto',
            hidegrid:true,
            caption:'Sub-Projects'
        });
    });
    </script>
</c:if>

<c:if test="${group!=null }">
    <h1><img src="../images/iconTestGroup.png"/><tag:escape text="${group.name}"></tag:escape> </h1>
    <tag:nl2br><tag:escape text="${group.description}"></tag:escape> </tag:nl2br>
</c:if>

<c:if test="${groups!=null && group==null}">
    <br/>
    <br/>
    <table id="groupsTable" border="0">
        <tr>
            <th>Test groups</th>
        </tr>
        <tbody>
	        <c:forEach items="${groups}" var="group">
	            <tr>
	                <td><a class="big-link" href="../project/display-${project.path}?groupId=${group.id}"><tag:escape text="${group.name}"/></a></td>
	            </tr>
	        </c:forEach>
        </tbody>
    </table>
    <script>
    $(document).ready(function(){
        tableToGrid("#groupsTable",{
            height:'auto',
            width:'auto',
            hidegrid:true,
            caption:'Test groups'
        });
    });
    </script>
</c:if>

<c:if test="${tests!=null}">
	<br/>
	<br/>
    <table id="testsTable" border="0">
        <tr>
            <th>Name</th>
            <th>Author</th>
            <th>Created</th>
        </tr>
        <tbody>
            <c:forEach items="${tests}" var="t">
	            <tr>
	                <td><a href="../test/display?id=${t.id}"><img src="../images/iconTest.png"/> ${fn:escapeXml(t.name)} </a></td>
	                <td><a href="../user/profile-${t.authorLogin}"><img src="../images/workflow-icon-user.png"/> ${fn:escapeXml(t.authorName)} </a></td>
	                <td><tag:date date="${t.date}"/></td>
	            </tr>
	        </c:forEach>
        </tbody>
    </table>
    
    <script>
    $(document).ready(function(){
        tableToGrid('#testsTable',{
            height:'auto',
            width:'auto',
            hidegrid:true,
            caption:'Tests'
        });
    });
    </script>
</c:if>


<!-- Comments layout -->
<br/>
<tag:comments-layout user="${user}" comments="${comments}" redirect="../project/display-${project.path}" unitId="${project.id}" unit="project"></tag:comments-layout>
