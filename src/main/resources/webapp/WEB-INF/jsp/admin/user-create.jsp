<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<h1>Create user</h1>

<tag:panel title="Create User" align="center">
<form:form method="post" commandName="createUser">
	<table border="0" width="500px" align="center">
	   <tbody>
	       <tr>
               <td>
                   Login:<br/><tag:edit-field path="login" width="100%"/>
                   <form:errors path="login" cssClass="error"/>
               </td>
           </tr>
           <tr>
               <td>
                   Password:<br/><tag:edit-field path="password" width="100%"/>
                   <form:errors path="password" cssClass="error"/>
               </td>
           </tr>
           <tr>
               <td>
                   Name:<br/>
                   <tag:edit-field path="name" width="100%"/>
                   <form:errors path="name" cssClass="error"/>
               </td>
           </tr>
           <tr>
               <td>
                   Mail:<br/><tag:edit-field path="email" width="100%"/>
                   <form:errors path="email" cssClass="error"/>
               </td>
           </tr>
	       <tr>
               <td align="center">
                   <tag:submit value="Create"/>
               </td>
           </tr>
	       <tr>
	           <td>
	               <form:errors cssClass="error"/>
	               
	           </td>
	       </tr>
	   </tbody>
	</table>
</form:form>
</tag:panel>