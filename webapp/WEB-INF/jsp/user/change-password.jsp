<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Change Password" align="center" width="350px">
    <form method="post">
        <table border="0" align="center" width="100%">
            <tr>
                <td class="small-description">Old Password:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" name="oldPassword" value="" width="100%"/></td>
            </tr>
            <tr>
                <td class="small-description">New Password:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" name="newPassword" value=""  width="100%"/></td>
            </tr>
            <tr>
                <td class="small-description">New Password Confirmation:</td>
            </tr>
            <tr>
                <td><tag:edit-field password="true" name="newPasswordConfirmation" value="" width="100%"/></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <tag:submit value="Change"/>
               </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div class="error">
                    	<tag:spring-form-error field="" command="changePassword"></tag:spring-form-error>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel>