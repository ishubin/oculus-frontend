<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<h1>Create user</h1>

<tag:panel title="Create User" align="center">
<form method="post">
	<p>
	   Login:<br/>
	   <tag:edit-field name="login" width="100%" value="${createUser.login}"/>
    </p>
    <p>
        Password:<br/>
        <tag:edit-field name="password" width="100%" value="${createUser.password}"/>
    </p>
    <p>
        Name:<br/>
        <tag:edit-field name="name" width="100%"  value="${createUser.name}"/>
    </p>
    <p>
        Mail:<br/><tag:edit-field name="email" width="100%"  value="${createUser.email}"/>
    </p>
    <tag:submit value="Create"/>
    <div class="error">
    		<tag:spring-form-error field="" command="createUser"></tag:spring-form-error>
    </div>
</form>
</tag:panel>