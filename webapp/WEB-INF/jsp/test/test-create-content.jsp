<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<jsp:directive.page import="net.mindengine.oculus.frontend.web.SessionViewHandler"/>
<%@ include file="/session-handler.jsp" %>


<tag:pickuser-setup></tag:pickuser-setup>
<script>
$(function() {
	$("#testTabs").tabs();
});
	
function onSubmitTest() {
	$("#testContentField").val(testCaseEditor.exportContent());
}
</script>
	
<input id="testContentField" type="hidden" name="content" value="${test.content}"/>
<div id="testTabs">
	<ul>
		<li><a href="#test-details-tab">Details</a></li>
		<li><a href="#test-content-tab">Content</a></li>
		<li><a href="#test-settings-tab"><img src="../images/workflow-icon-settings.png"/> Settings</a></li>
	</ul>
	<div id="test-details-tab">
		<p>
			Name: <br/>
			<tag:edit-field name="name" value="${test.name}" width="100%"></tag:edit-field>
			<c:if test="${testCommand=='Create'}">
            	<input type="hidden" name="projectId" value="${project.id}"/>
            </c:if>
		</p>
		<p>
			Description: <br/>
			<textarea name="description" style="width:100%;" rows="10" class="custom-edit-text"><tag:escape text="${test.description}"/></textarea>
		</p>
		<p>
			Mapping: <br/>
			<tag:edit-field name="mapping" width="100%" value="${test.mapping}"></tag:edit-field>
		</p>
		<p>
			Test Group: <br/>
			<select name="groupId">
	            <option value="0" style="color:gray;">No group...</option>
	            <c:forEach items="${groups}" var="g">
	                <option value="${g.id}" <c:if test="${group!=null &&group.id == g.id }">selected="selected"</c:if>><tag:escape text="${g.name}"/></option>
	            </c:forEach>
	        </select>
		</p>
	</div>
	<div id="test-content-tab">
		<%@ include file="/WEB-INF/jsp/test/testcase-editor.jsp" %>
	</div>
	<div id="test-settings-tab">
		<c:forEach items="${customizationGroups}" var="cgs">
            <h2><tag:escape text="${cgs.name}"></tag:escape> </h2>
            <c:forEach items="${cgs.customizations}" var="c">
               <p>
                  <tag:escape text="${c.customization.name}"/>
                  <br/>
                  <span class="small-description"><tag:escape text="${c.customization.description}"/></span>
                  <tag:customization-edit customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"></tag:customization-edit>
               </p>        
            </c:forEach>
        </c:forEach>
	</div>
</div>
<br/>
<tag:submit value="${testCommand }" onclick="return onSubmitTest();"></tag:submit>


<div class="error">
	<tag:spring-form-error field="" command="test"></tag:spring-form-error>
</div>