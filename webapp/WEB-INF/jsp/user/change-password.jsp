<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Change Password" align="center" width="350px">
    <form:form method="post" commandName="changePassword">
        <table border="0" align="center" width="100%">
            <tr>
                <td class="small-description">Old Password:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" path="oldPassword" width="100%"/></td>
            </tr>
            <tr>
                <td class="small-description">New Password:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" path="newPassword" width="100%"/></td>
            </tr>
            <tr>
                <td class="small-description">New Password Confirmation:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" path="newPasswordConfirmation" width="100%"/></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <tag:submit value="Change"/>
               </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <form:errors cssClass="error"/>
                    <div class="error">${errorMessage}</div>
                </td>
            </tr>
        </table>
    </form:form>
</tag:panel>