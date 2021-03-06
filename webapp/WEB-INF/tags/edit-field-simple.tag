<%@ tag body-content="scriptless" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="password" required="false" %>
<%@ attribute name="value" required="false" %>
<%@ attribute name="escapeHtml" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<input name="${name}" 
    style="width:100%;" 
    class="custom-edit-text"
    <c:if test="${id!=null}">id="${id}"</c:if> 
    
    
    <c:choose>
	     <c:when test="${password==true}">
	        type="password"       
	     </c:when>
	     <c:otherwise>
	        type="text"
	     </c:otherwise>
	 </c:choose>
    
    
    
    <c:if test="${value!=null}">
        <c:choose>
            <c:when test="${escapeHtml==true}">
                value="<%out.print(org.apache.commons.lang.StringEscapeUtils.escapeHtml(value)); %>"
            </c:when>
            <c:otherwise>
                value="${value}"
            </c:otherwise>
        </c:choose>
    </c:if>
    />