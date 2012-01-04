<%@ tag body-content="scriptless" %>
<%@ attribute name="align" required="true" %>
<%@ attribute name="width" required="false" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="cssClass" required="true" %>
<%@ attribute name="disclosurePanel" required="false" %>
<%@ attribute name="opened" required="false" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%
HttpSession s = request.getSession();
Integer uniqueDivId = (Integer)s.getAttribute("uniqueDivId");
if(uniqueDivId==null)
{
    uniqueDivId = 0;
}
else uniqueDivId++;
s.setAttribute("uniqueDivId",uniqueDivId);

jspContext.setAttribute("uniqueDivId","uniqueDivId_"+uniqueDivId);
 %>
<table border="0" class="${cssClass}" align="${align}"   
        cellspacing="0" cellpadding="0" <%if(width!=null){%>width="${width}"<%}%>>
    <thead>
        <tr>
            <td class="${cssClass}-corner-1"><i></i></td>
            <td class="${cssClass}-top">
                <c:choose>
                    <c:when test="${disclosurePanel == true}">
                        <a href="javascript:onDisclosurePanelClick('${uniqueDivId}');">${title}</a>
                    </c:when>
                    <c:otherwise>
                        ${title}
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="${cssClass}-corner-2"><i></i></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="${cssClass}-left"><i></i></td>
            <td class="${cssClass}-body">
                <c:choose>
                    <c:when test="${disclosurePanel == true}">
                        <div id="${uniqueDivId}" style="display:<c:choose><c:when test="${opened==true}">block</c:when><c:otherwise>none</c:otherwise></c:choose>;">
                            <jsp:doBody/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <jsp:doBody/>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="${cssClass}-right"><i></i></td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <td class="${cssClass}-corner-3"><i></i></td>
            <td class="${cssClass}-bottom"></td>
            <td class="${cssClass}-corner-4"><i></i></td>
        </tr>
    </tfoot>
</table>
