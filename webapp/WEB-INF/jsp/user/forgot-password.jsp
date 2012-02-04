<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Remind Password" align="center">
    <form method="post">
        <table border="0" align="center">
            <tr>
                <td class="small-description">Email:</td>
                <td><tag:edit-field name="email" value=""/></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <tag:submit value="Send me password"/>
               </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div class="error">
                    ${errorMessage}
                    </div>
                </td>
            </tr>
        </table>
    </form>
</tag:panel>