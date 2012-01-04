<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<table border="0" class="panel-border" align="${align}"   
        cellspacing="0" cellpadding="0" 
        <%if(width!=null){%>width="${width}"<%}%>
        <%if(height!=null){%>height="${height}"<%}%>
        >
    <thead>
        <tr>
            <td class="panel-border-corner-1"><i> </i></td>
            <td valign="top">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="panel-border-top-fill" width="30px">
                        </td>
                        <td valign="top" style="white-space:nowrap;padding-left:5px;color:#555555;padding-right:5px;" width="10px">
                           ${title}
                        </td>
                        <td class="panel-border-top-fill" width="100%">
                        </td>
                    </tr>
                </table>
            </td>
            <td class="panel-border-corner-2"><i> </i></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="panel-border-left-fill"><i></i></td>
            <td class="panel-border-body">
                <jsp:doBody></jsp:doBody>
            </td>
            <td class="panel-border-right-fill"><i></i></td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <td class="panel-border-corner-3"><i></i></td>
            <td class="panel-border-bottom-fill"></td>
            <td class="panel-border-corner-4"><i></i></td>
        </tr>
    </tfoot>
</table>
