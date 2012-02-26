<%@ tag body-content="scriptless" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="cols" required="false" %>
<%@ attribute name="rows" required="false" %>
<%@ attribute name="style" required="false" %>
<%@ attribute name="cssClass" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<textarea class="custom-textarea" name="${name}"
    <c:if test="${id!=null }">id="${id}"</c:if>
    <c:if test="${cols!=null }">cols="${cols}"</c:if>
    <c:if test="${rows!=null }">rows="${rows}"</c:if>
    <c:if test="${style!=null }">style="${style}"</c:if>
    <c:if test="${cssClass!=null }">class="${cssClass}"</c:if>
        
    
    ><%out.print(org.apache.commons.lang.StringEscapeUtils.escapeHtml(value));%></textarea>
