<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="panel-inner">
    <jsp:doBody></jsp:doBody>
</div>