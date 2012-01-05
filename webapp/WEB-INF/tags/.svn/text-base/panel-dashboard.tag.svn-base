<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<table border="0" class="panel-dashboard" align="${align}"   
        cellspacing="0" cellpadding="0" 
        <%if(width!=null){%>width="${width}"<%}%>
        <%if(height!=null){%>height="${height}"<%}%>
        >
    <thead>
        <tr>
            <td class="panel-dashboard-corner-1"><i></i></td>
            <td class="panel-dashboard-top-fill">
            </td>
            <td class="panel-dashboard-corner-2"><i></i></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="panel-dashboard-left-fill"><i></i></td>
            <td class="panel-dashboard-body">
                <jsp:doBody></jsp:doBody>
            </td>
            <td class="panel-dashboard-right-fill"><i></i></td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <td class="panel-dashboard-corner-3"><i></i></td>
            <td class="panel-dashboard-bottom-fill"></td>
            <td class="panel-dashboard-corner-4"><i></i></td>
        </tr>
    </tfoot>
</table>
