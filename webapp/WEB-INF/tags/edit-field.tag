<%@ tag body-content="scriptless" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="password" required="false" %>
<%@ attribute name="width" required="false"%>
<%@ attribute name="id" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:choose>
    <c:when test="${password==true}">
        <form:password path="${path}" 
            cssStyle="width:${width};"  
            cssClass="custom-edit-text" 
            cssErrorClass="custom-edit-text-error"
            id="${id}"
            />
    </c:when>
    <c:otherwise>
        
        <form:input path="${path}" 
            cssStyle="width:${width};" 
            cssClass="custom-edit-text" 
            cssErrorClass="custom-edit-text-error"
            id="${id}"
            />
            
    </c:otherwise>
</c:choose>