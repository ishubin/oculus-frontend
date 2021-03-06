<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true"  %>
<%@ attribute name="title" required="true"  %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="divDisclosureIcon_${id}" class="disclosure-icon-close" style="float:left;"></div>
<a class="disclosure" href="javascript:onDisclosurePanelClick('divDisclosureBody_${id}','divDisclosureIcon_${id}');">
${title}
</a>
<div id="divDisclosureBody_${id}" style="display:none;" class="disclosure-body">
<jsp:doBody></jsp:doBody>
</div>