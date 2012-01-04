<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<table class="panel-gradient" border="0" cellspacing="0px" cellpadding="0px"
    <%if(width!=null){%>width="${width}"<%}%>
    <%if(height!=null){%>height="${height}"<%}%>
    >
    <tr>
        <td class="panel-gradient-body"><jsp:doBody></jsp:doBody></td>
    </tr>
</table>