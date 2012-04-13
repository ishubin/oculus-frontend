<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<br/>

<tag:panel title="Remind Password" align="center">
    <form method="post">
        <p>
            Email:<br/>
            <tag:edit-field name="email" value=""/>
        </p>
        <p>
            <tag:submit value="Send me password"/>
        </p>
        <div class="error">
        ${errorMessage}
        </div>
    </form>
</tag:panel>