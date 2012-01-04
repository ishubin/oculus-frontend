<%@ tag body-content="scriptless" %>
<%@ attribute name="url" required="false" %>
<%@ attribute name="img" required="true" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="tooltip" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a
    <c:choose>
      <c:when test="${onclick!=null}">
          onClick="${onclick}"
      </c:when>
      <c:otherwise>
          href="..${url}"
      </c:otherwise>
    </c:choose>
    
    
    class="custom-link-icon"
    title="${tooltip}"
    >
    <img class="custom-link-icon" src="${img}"/>
</a>
