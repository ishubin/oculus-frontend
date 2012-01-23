<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="left-panel">
    
    <form:form commandName="projectBrowseFilter" name="projectBrowseFilter" method="post">
        <form:hidden path="pageLimit"/>
        <form:hidden path="pageOffset"/>
        
    </form:form>
</div>