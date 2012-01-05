<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="userId" required="true" %>
<%@ attribute name="userName" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" name="${id}" id="${id}" value="${userId}"/>
<div style="width:134px;height:19px;margin-bottom:5px;">
  <a class="pick-button" id="linkPickUser${id}" href="javascript:onPickUserClick('${id}','linkPickUser${id}');">
    <c:choose>
        <c:when test="${userName!=null && userId!=null && userId!=''}">
            ${userName}
        </c:when>
        <c:otherwise>
            Pick User...
        </c:otherwise>
    </c:choose>
  </a>
</div> 