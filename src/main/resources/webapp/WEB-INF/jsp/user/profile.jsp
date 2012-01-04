
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.user.User"/><%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<table border="0" cellspacing="0" cellpadding="2" width="100%">
    <tr>
        <td width="50px" valign="top">
            <img src="../images/user.png"/>
        </td>
        <td>
            <table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <td class="issue-table" colspan="2">
                            User Profile:
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="odd">
                        <td class="issue-table-left">
                            Name:
                        </td>
                        <td class="issue-table">
                            ${choosenUser.name}
                        </td>
                    </tr>
                    <tr class="even">
                        <td class="issue-table-left">
                            Email:
                        </td>
                        <td class="issue-table">
                            ${choosenUser.email}
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>
</table>