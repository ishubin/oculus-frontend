<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Remind Password" align="center">
    <form:form method="post" commandName="forgotPassword">
        <table border="0" align="center">
            <tr>
                <td class="small-description">Email:</td>
                <td><tag:edit-field path="email"/></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <tag:submit value="Send me password"/>
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