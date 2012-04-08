<%@ tag body-content="scriptless" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="name" required="name" %>
<%@ attribute name="onclick" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="align" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<input  class="custom-button-text" 
                        type="submit"  
                        <c:if test="${onclick!=null}">onclick="${onclick}"</c:if>
                        <c:if test="${name!=null}">name="${name}"</c:if>
                        value="${value}"
                        <c:if test="${id!=null}">id="${id}"</c:if>
                         
                        />