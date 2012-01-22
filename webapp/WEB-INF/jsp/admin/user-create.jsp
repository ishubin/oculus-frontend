<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<h1>Create user</h1>

<tag:panel title="Create User" align="center">
<form method="post">
	<table border="0" width="500px" align="center">
	   <tbody>
	       <tr>
               <td>
                   Login:<br/><tag:edit-field name="login" width="100%" value="${createUser.login}"/>
               </td>
           </tr>
           <tr>
               <td>
                   Password:<br/><tag:edit-field name="password" width="100%" value="${createUser.password}"/>
               </td>
           </tr>
           <tr>
               <td>
                   Name:<br/>
                   <tag:edit-field name="name" width="100%"  value="${createUser.name}"/>
               </td>
           </tr>
           <tr>
               <td>
                   Mail:<br/><tag:edit-field name="email" width="100%"  value="${createUser.email}"/>
               </td>
           </tr>
	       <tr>
               <td align="center">
                   <tag:submit value="Create"/>
               </td>
           </tr>
	       <tr>
	           <td>
	               <div class="error">
	               		<tag:spring-form-error field="" command="createUser"></tag:spring-form-error>
	               </div>
	           </td>
	       </tr>
	   </tbody>
	</table>
</form>
</tag:panel>