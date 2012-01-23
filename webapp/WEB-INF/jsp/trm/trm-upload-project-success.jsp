<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>

<div class="breadcrump" align="center">
    <a href="../test-run-manager/main">Test Run Manager</a>
    <img src="../images/breadcrump-arrow.png"/> 
    
    <a href="../test-run-manager/customize">Customize Settings</a>
    <img src="../images/breadcrump-arrow.png"/>
    Upload Project
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
                            Project -
                            <b>${project.name}</b>
                        </td>
                    </tr>
                </thead>
                <tbody>
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
                            <tag:escape text="${project.name}"/>
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
                </tbody>
            </table>
        </td>
    </tr>
</table>
<br/>
<br/>
<b>The project was uploaded successfully</b>
