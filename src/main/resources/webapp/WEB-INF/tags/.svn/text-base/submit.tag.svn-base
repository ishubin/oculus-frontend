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
<script>
$(".custom-button-text").button();
</script>
<%--
<table class="custom-button-wrap custom-button" 
        onMouseOver="this.className='custom-button-wrap custom-button-over';return true;" 
        onMouseOut="this.className='custom-button-wrap custom-button';return true;" 
        border="0" cellspacing="0" cellpadding="0"
        <c:if test="${align!=null}">align="${align}"</c:if>
        >
    <tr>
        <td class="custom-button-left"><i></i></td>
        <td class="custom-button-center">
            <em unselectable="on">
                
            </em>
        </td>
        <td class="custom-button-right"><i></i></td>
    </tr>
</table>
 --%>