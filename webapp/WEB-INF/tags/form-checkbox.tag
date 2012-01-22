<%@tag import="java.util.Enumeration"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="id" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<input type="hidden" id="hiddenCheckbox_${name}" name="${name}" value="${value==true?'1':'0'}"/>
<input id="${id!=null?id:''}" name="customCheckBox${name}" type="checkbox" <c:if test="${value==true}">checked="checked"</c:if>
	onchange="$('#hiddenCheckbox_${name}').val(this.checked?'1':'0')"
/>
