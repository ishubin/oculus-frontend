<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<table border="0" width="100%" height="100%">
    <tr>
        <td align="center" valign="middle">
            <tag:panel title="Login" width="250px" align="center" logo="../images/workflow-icon-login.png">
			    <form name="login" method="post">
			        <table border="0" align="center">
			            <tr>
			                <td class="small-description">Login:</td>
			            </tr>
			            <tr>
			                <td><tag:edit-field name="login" value="${login.login}" width="100%"/></td>
			            </tr>
			            <tr>
			                <td class="small-description">Password:</td>
			            </tr>
			            <tr>
			                <td><tag:edit-field name="password" password="true" value="" width="100%"/></td>
			            </tr>
			            <tr>
			                <td align="center">
			                    <tag:submit value="Login"></tag:submit>
			               </td>
			            </tr>
			            <tr>
			                <td align="center">
			                    <div class="error">
			                    	<tag:spring-form-error field="login" command="login"></tag:spring-form-error> 
			                    </div>
			                </td>
			            </tr>
			            <tr>
			              <td colspan="2" >
			                  <div align="center">
			                      <a class="nice-link" href="../user/forgot-password">Remind me password</a>
			                  </div>
			              </td>
			            </tr>
			        </table>
			    </form>
			</tag:panel> 
        </td>
    </tr>
</table>
<script>
document.forms.login.login.focus();
</script>