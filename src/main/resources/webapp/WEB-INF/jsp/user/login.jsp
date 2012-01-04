<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<table border="0" width="100%" height="100%">
    <tr>
        <td align="center" valign="middle">
            <tag:panel title="Login" width="250px" align="center" logo="../images/workflow-icon-login.png">
			    <form:form name="login" method="post" commandName="login">
			        <table border="0" align="center">
			            <tr>
			                <td class="small-description">Login:</td>
			            </tr>
			            <tr>
			                <td><tag:edit-field path="login"  width="100%"/></td>
			            </tr>
			            <tr>
			                <td class="small-description">Password:</td>
			            </tr>
			            <tr>
			                <td><tag:edit-field path="password" password="true" width="100%"/></td>
			            </tr>
			            <tr>
			                <td align="center">
			                    <tag:submit value="Login"></tag:submit>
			               </td>
			            </tr>
			            <tr>
			                <td align="center">
			                    <form:errors path="login" cssClass="error"/>
			                    <form:errors cssClass="error"/>
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
			    </form:form>
			</tag:panel> 
        </td>
    </tr>
</table>
<script language="javascript">
document.forms.login.login.focus();
</script>