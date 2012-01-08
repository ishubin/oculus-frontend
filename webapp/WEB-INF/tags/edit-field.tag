
<%@tag import="java.util.Enumeration"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="password" required="false" %>
<%@ attribute name="width" required="false"%>
<%@ attribute name="value" required="true"%>
<%@ attribute name="id" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<input id="${id!=null?id:''}" class="custom-edit-text" name="${name}" value="${value}" type="${password==true?'password':'text'}" <c:if test="${width!=null }">style="width:${width};"</c:if>/>
