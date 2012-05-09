<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="panel-border">
<span class="panel-border-title"><tag:escape text="${title}"></tag:escape></span></br>
<jsp:doBody></jsp:doBody>
</div>