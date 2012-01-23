<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <a href="../test-run-manager/main">Test Run Manager</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Customize Settings
</div>


<div class="project-layout" onclick="window.location='../test-run-manager/upload-project-choose-project';">
    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td width="128px">
                <a href="../test-run-manager/customize-suite-parameters">
                    <img src="../images/trm-main-customize-suite-parameters.png"/>
                </a>
            </td>
            <td valign="top" align="left">
                <a style="font-size:12pt;font-weight:bold;width:100%;height:100%;display:block;color:black;"
                    href="../test-run-manager/customize-suite-parameters">Customize Suite Parameters
                </a>
            </td>
        </tr>
    </table>
</div>

<br/>
<br/>

<div class="project-layout" onclick="window.location='../test-run-manager/upload-project-choose-project';">
    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td width="128px">
                <a href="../test-run-manager/upload-project-choose-project">
                    <img src="../images/trm-upload-project-icon.png"/>
                </a>
            </td>
            <td valign="top" align="left">
                <a style="font-size:12pt;font-weight:bold;width:100%;height:100%;display:block;color:black;"
                    href="../test-run-manager/upload-project-choose-project">
                    Upload Project
                </a>
            </td>
        </tr>
    </table>
</div>
