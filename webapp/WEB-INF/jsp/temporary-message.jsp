<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>



<c:if test="${temporaryMessage!=null}">
    <div align="left">
	    <tag:messagebox title="Info" type="success">
	        ${temporaryMessage}
	    </tag:messagebox>
    </div>
</c:if>
