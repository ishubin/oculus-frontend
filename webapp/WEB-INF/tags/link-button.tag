<%@ tag body-content="scriptless" %>
<%@ attribute name="href" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a class="custom-link-button" href="${href}">
	<jsp:doBody/>
</a>
<script>
$(".custom-link-button").button();
</script>