<%@ tag body-content="scriptless" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="align" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ attribute name="logo" required="false" %>
<%@ attribute name="closeDivName" required="false" %>
<%@ attribute name="disclosure" required="false" type="java.lang.Boolean"%>
<%@ attribute name="maximizing" required="false" type="java.lang.Boolean"%>
<%@ attribute name="maximizeDivName" required="false" type="java.lang.String"%>
<%@ attribute name="closed" required="false" type="java.lang.Boolean"%>
<%@ attribute name="id" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<table border="0" <c:if test="${align!=null}">align="${align}"</c:if>
    <c:if test="${width!=null}">width="${width}"</c:if>
    <c:if test="${height!=null}">height="${height}"</c:if>
>
    <tr>
        <td>
            <div class="custom-panel-wrap">
			    <div class="custom-panel-title ${disclosure==true?'custom-panel-title-active':''}"
			        <c:if test="${disclosure==true}">onclick="onPanelToggle('${id}');"</c:if>
			    >
			        <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
			            <tr>
			                <c:if test="${disclosure==true}">
			                    <td width="16px">
			                        <a class="small-button-${closed==true?'show':'hide'}" id="panel_${id}_toggleButton" href="javascript:onPanelToggle('${id}');">
			                        </a>
			                    </td>
			                </c:if>
			                
			                <td>
			                 <div id="panel_${id}_title">
			                     <c:if test="${logo!=null && logo!=''}"><img src="${logo}"/></c:if>
			                     ${title}
			                 </div>
			                 </td>
			                
			                  <c:if test="${closeDivName!=null}">
			                      <td width="20px">
			                          <a class="small-button-close" href="javascript:closePopup('${closeDivName}');"></a>
			                      </td>
			                  </c:if>
			          
			                  <c:if test="${maximizing==true}">
			                      <td width="20px">
			                          <a class="small-button-maximize" id="panel_${id}_maximizeButton" href="javascript:onPanelMaximize('${maximizeDivName}','${id}');"></a>
			                          <a class="small-button-minimize" id="panel_${id}_minimizeButton" style="display:none;" href="javascript:onPanelMinimize('${maximizeDivName}','${id}');"></a>
			                      </td>
			                  </c:if>
			            </tr>
			        </table>
			    </div>
			    <div class="custom-panel-body" id="panel_${id}_body">
			        <jsp:doBody></jsp:doBody>
			    </div>
			</div>
        </td>
    </tr>
</table>