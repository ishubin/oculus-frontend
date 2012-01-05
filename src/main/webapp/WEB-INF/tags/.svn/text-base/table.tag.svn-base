<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="false" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="height" required="false" %>
<%@ attribute name="columns" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
String strColumns = jspContext.getAttribute("columns").toString();
String[] arr = strColumns.split(",");
java.util.List<String> columnList = new java.util.ArrayList<String>();

for(int i=0;i<arr.length;i++)
{
    if(!arr[i].isEmpty())
    {
        columnList.add(arr[i]);
    }
}
jspContext.setAttribute("columnList", columnList);
%>
<table class="table-custom" border="0" cellpadding="0px" cellspacing="0px"
        <c:if test="${align!=null}">align="${align}"</c:if>
        <c:if test="${width!=null}">width="${width}"</c:if>
        <c:if test="${height!=null}">height="${height}"</c:if>
    >
    <tr>
        <c:forEach items="${columnList}" var="column">
        <td class="table-cell">
            <div class="table-header-column">
            <table border="0" width="100%" cellpadding="0px" cellspacing="0px">
                <tr>
                    <td class="table-header-column-left"></td>
                    <td class="table-header-column-fill"><pre>${column} </pre></td>
                    <td class="table-header-column-right"></td>
                </tr>
            </table>
            </div>
        </td>
        </c:forEach>
    </tr>
    <jsp:doBody/>
    
</table>