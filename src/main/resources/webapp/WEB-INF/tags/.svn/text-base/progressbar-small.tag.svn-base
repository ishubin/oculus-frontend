<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="width" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="top" required="true" %>
<%@ attribute name="left" required="true" %>
<%@ attribute name="value" required="true" type="java.lang.Integer"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div style="position:relative;">
    <div class="progressbar-back" style="z-index:10;left:${left};top:${top};width:${width};height:16px;">
        <div style="position:relative;width:100%;height:100%;">
            <div align="center" style="width:${width};z-index:12;vertical-align:middle;height:16px;position:absolute;">
	           <div  class="progressbar-text" id="progressBarText_${id}"
	            >
	            ${value}%
	            </div>
	        </div>
	        <div id="progressBarFill_${id}" class="progressbar-fill" style="width:${value}%;">
	        </div>
        </div>
    </div>
</div>