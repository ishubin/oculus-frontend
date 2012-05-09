<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump" align="center">
    Upload Project to Grid
</div>

Choose the project

<br/>
<br/>
<c:forEach items="${projects}" var="p">
    <div class="project-layout" onclick="window.location='../grid/upload-project?projectId=${p.id}'">
        <table class="project-browse-project" border="0" cellpadding="0px" cellspacing="0px" width="100%" height="100%">
            <tr>
                <td width="60px" valign="middle" align="center">
                    <a href="../grid/upload-project?projectId=${p.id}">
                        <c:choose>
                            <c:when test="${p.icon!=null && p.icon!=''}">
                                <img width="60px" src="../display/file?type=project-icon&projectId=${p.id}&object=${p.icon}"/>
                            </c:when>
                            <c:otherwise>
                                <img width="60px" src="../images/project-no-icon.png"/>
                            </c:otherwise>
                        </c:choose>
                    </a>
                </td>
                <td>
                    <table border="0" cellpadding="0px" cellspacing="0px" width="100%" height="100%">
                        <tr>
                             <td height="10px"  valign="top" align="left" style="font-size:10pt;font-weight:bold;">
                                 <a style="color:black;font-weight:bold;" href="../grid/upload-project?projectId=${p.id}">
                                     ${p.name}
                                 </a>
                             </td>
                        </tr>
                        <tr>
                             <td valign="top" align="left">
                                 <div style="display:block;width:100%;height:100%;">
                                      ${p.description}
                                 </div>
                             </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <br/>
</c:forEach>