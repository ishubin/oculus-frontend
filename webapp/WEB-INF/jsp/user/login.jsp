<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<table border="0" width="100%" height="100%">
    <tr>
        <td align="center" valign="middle">
            <tag:panel title="Authentication" width="250px" align="center" logo="../images/workflow-icon-login.png">
			    <form name="login" method="post">
			        <p>
			            Login:<br/>
			            <tag:edit-field name="login" value="${login.login}" width="100%"/>
			        </p>
			        <p>
			            Password:<br/>
			            <tag:edit-field name="password" password="true" value="" width="100%"/>
			        </p>
			        <div align="center">
			            <tag:submit value="Login"></tag:submit>
			        </div>
			        <div class="error">
                        <tag:spring-form-error field="login" command="login"></tag:spring-form-error> 
			        </div>
			        <div align="center">
                        <a class="nice-link" href="../user/forgot-password">Remind me password</a>
			        </div>
			    </form>
			</tag:panel> 
        </td>
    </tr>
</table>
<script>
document.forms.login.login.focus();
</script>