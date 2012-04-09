<%@ include file="/include.jsp" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>

<html>

    <head>
        <title>Oculus - <tag:escape text="${title}"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
        <link rel="stylesheet" type="text/css" href="../main.css"></link>
        <link rel="stylesheet" type="text/css" href="../controls.css"></link>
        <link rel="stylesheet" type="text/css" href="../document-project.css"></link>
        <link rel="stylesheet" type="text/css" href="../browse-report.css"></link>
        
        <script type="text/javascript" src="../scripts/controls.js"></script>
        <script src="../scripts/popup.js"></script>
        <script src="../scripts/ajax.js"></script>
        <link rel="STYLESHEET" type="text/css" href="../scripts/jquery-ui-custom/css/jquery-ui-1.8.14.custom.css"/>
        <script type="text/javascript"  src="../scripts/jquery-1.6.2.min.js"></script>
        <script type="text/javascript"  src="../scripts/jquery.ui.position.js"></script>
        <script type="text/javascript"  src="../scripts/jquery-ui-custom/js/jquery-ui-1.8.14.custom.min.js"></script>
        
        <link rel="STYLESHEET" type="text/css" href="../scripts/superfish/css/superfish.css"/>
        <script type="text/javascript" src="../scripts/superfish/js/hoverIntent.js"></script>
        <script type="text/javascript"  src="../scripts/superfish/js/superfish.js"></script>
        <script type="text/javascript"  src="../scripts/superfish/js/supersubs.js"></script>
        
        <link rel="STYLESHEET" type="text/css" href="../scripts/jquery-grid/css/ui.jqgrid.css"/>
        <script type="text/javascript"  src="../scripts/jquery-grid/js/grid.locale-en.js"></script>
        <script type="text/javascript"  src="../scripts/jquery-grid/js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript"  src="../scripts/jquery-grid/js/grid.treegrid.js"></script>
        
        <link rel="STYLESHEET" type="text/css" href="../scripts/context-menu/jquery.contextMenu.css"/>
        <script type="text/javascript"  src="../scripts/context-menu/jquery.contextMenu.js"></script>
        
        <!-- <script type="text/javascript"  src="../scripts/jquery.tools.min.js"></script> -->
        <script type="text/javascript"  src="../scripts/nicedit/nicEdit.js"></script>
        
        <script type="text/javascript"  src="../scripts/jquery.easing.1.3.js"></script>
        <script type="text/javascript"  src="../scripts/stickysidebar.jquery.js"></script>
        
        <script type="text/javascript"  src="../scripts/jquery.sort.js"></script>
        
        <link rel="STYLESHEET" type="text/css" href="../dhtmlxTree/dhtmlxtree.css"/>
        <script  src="../dhtmlxTree/dhtmlxcommon.js"></script>
        <script  src="../dhtmlxTree/dhtmlxtree.js"></script>
        
        <script>
        $(function (){
        	$(".custom-button-text").button().css("font-size","9pt");
        })
        </script>  
    </head>
    
    <body  <c:if test="${bodyOnLoad!=null}">onload="${bodyOnLoad}"</c:if>>
        <div id="divShadow" class="shadow" style="display:none;">
        </div>
        
    	<table width="100%" height="100%" border="0" cellpadding="0"  cellspacing="0">
    	   <tr>
    	       <td>
    	           <table width="100%" align="center" height="100%" border="0" cellpadding="0" cellspacing="0">
		            <tr>
		                <td height="40px" width="100%">
		                    <%@ include file="/WEB-INF/jsp/header.jsp" %>
		                </td>
		            </tr>
		            <tr>
		                <td style="background:#2779AA;padding:0px;margin:0px;">
		                    <%@ include file="/WEB-INF/jsp/menu.jsp" %>
		                </td>
		            </tr>
		        </table>
    	       </td>
    	   </tr>
    	   <tr>
    	       <td width="100%" height="100%">
    	           <table width="100%" height="100%" align="center" border="0" cellpadding="5" cellspacing="0">
			            <tr>
			                <td valign="top" class="container-left-panel" height="100%">
			                    <tiles:insert name="left-panel"/>
			                </td>
			                <td valign="top" align="left" class="container-body-panel">
			                    <div id="divGlobalWarnDialog" style="display:none;">
			                         <tag:panel align="center" title="Warning" logo="../images/workflow-icon-warn.png" closeDivName="divGlobalWarnDialog" width="400px" height="100px">
			                             <div id="divGlobalWarnDialogBody">
			                             </div>
			                             <br/>
			                             <br/>
			                             <tag:submit value="Close" onclick="closePopup('divGlobalWarnDialog');" align="center"></tag:submit>
			                         </tag:panel>
			                    </div>
			                    <div id="divGlobalInfoDialog" style="display:none;">
                                     <tag:panel align="center" title="Info"  closeDivName="divGlobalInfoDialog" width="400px" height="100px">
                                         <div id="divGlobalInfoDialogBody">
                                         </div>
                                         <br/>
                                         <br/>
                                         <tag:submit value="Close" onclick="closePopup('divGlobalInfoDialog');" align="center"></tag:submit>
                                     </tag:panel>
                                </div>
			                    <%@ include file="/WEB-INF/jsp/temporary-message.jsp" %>
			                    <tiles:insert name="content"/>
			                </td>
			                <tiles:importAttribute name="rightPanelEnable"/>
			                <c:if test="${rightPanelEnable=='true'}">
			                    <td valign="top" class="container-right-panel">
	                                <tiles:insert name="right-panel" />
	                            </td>
			                </c:if>
			            </tr>
			      </table>
    	       </td>
    	   </tr>
    	</table>
    </body>
    
    
</html>