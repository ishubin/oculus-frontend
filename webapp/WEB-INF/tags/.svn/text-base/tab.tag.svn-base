<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="text" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="selected" required="true"  type="java.lang.Boolean"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<div id="${id}" class="${selected==true?'custom-tab-selected':'custom-tab'}">
    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="custom-tab-left"><span> </span></td>
            <td class="custom-tab-fill"><a id="${id}ActiveLink" href="${href}">${text}</a><span><a id="${id}Header" href="javascript:void(0);">${text}</a></span></td>
            <td class="custom-tab-right"><span> </span></td>
        </tr>
    </table>
</div>
