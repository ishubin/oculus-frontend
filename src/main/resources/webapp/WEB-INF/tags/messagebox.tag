<%@ tag body-content="scriptless" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="custom-msgbox-wrap">
    <div class="custom-msgbox-title">
        ${title}
    </div>
    <jsp:doBody></jsp:doBody>
</div>