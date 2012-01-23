<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">Test Run Manager</div>
<c:if test="${user == null}">
You have to be logged in.
</c:if>
<c:if test="${user != null}">
    <div class="project-layout" onclick="window.location='../test-run-manager/my-tasks';">
	    <table border="0" cellpadding="0" cellspacing="0">
	        <tr>
	            <td>
	               <a href="../test-run-manager/my-tasks">
	                <img src="../images/trm-main-my-tasks.png"/>
	               </a>
	            </td>
	            <td valign="top" align="left">
	                <a style="font-size:12pt;font-weight:bold;width:100%;height:100%;display:block;color:black;" href="../test-run-manager/my-tasks">
	                My Tasks
	                </a>
	            </td>
	        </tr>
	    </table>
	</div>
</c:if>
<c:if test="${user.hasPermissions.trm_administration == true}">
   <div class="project-layout" onclick="window.location='../test-run-manager/customize';">
		<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		        <td>
		            <a href="../test-run-manager/customize">
		               <img src="../images/trm-main-customize.png"/>
		            </a>
		        </td>
		        <td valign="top" align="left">
		            <a style="font-size:12pt;font-weight:bold;width:100%;height:100%;display:block;color:black;" 
		                href="../test-run-manager/customize">
		                Customize Settings
		            </a>
		        </td>
		    </tr>
		</table>
    </div>
</c:if>

