<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Change Password" align="center" width="350px">
    <form method="post">
        <p>
            Old Password:<br/>
            <tag:edit-field password="true" name="oldPassword" value="" width="100%"/>
        </p>
        <p>
            New Password:<br/>
            <tag:edit-field password="true" name="newPassword" value=""  width="100%"/>
        </p>
        <p>
            New Password Confirmation:<br/>
            <tag:edit-field password="true" name="newPasswordConfirmation" value="" width="100%"/>
        </p>
        <tag:submit value="Change"/>
        <div class="error">
        	<tag:spring-form-error field="" command="changePassword"></tag:spring-form-error>
        </div>
    </form>
</tag:panel>