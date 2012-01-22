<%@ include file="/include.jsp" %>


User <b>${createdUser.name}</b> was created successfully
<br/>
<c:choose>
    <c:when test="${isMailSent==true }">
        The e-mail with account settings will be sent to <b>${createdUser.email}</b>
    </c:when>
    <c:otherwise>
        <div style="color:red;">
            e-mail wasn't sent due to following:<br/>
            ${mailExceptionMessage}
        </div>
    </c:otherwise>
</c:choose>
