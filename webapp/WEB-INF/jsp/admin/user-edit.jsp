
<jsp:directive.page import="org.springframework.web.context.WebApplicationContext"/>
<jsp:directive.page import="org.springframework.web.context.support.WebApplicationContextUtils"/>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.user.*"/>
<jsp:directive.page import="java.util.*"/>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<h2>Edit user</h2>

<%
//Preparing model for permissions list
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
List<Permission> permissions = ((PermissionList)wac.getBean("permissionList")).getPermissions();
//Map<String,String>

//This code is needed because scriptlets cannot be used inside custom tags.
//So the solution is - prepare the permission checkbox model before the tags and then use it via simple jsp tags

User user = (User)pageContext.findAttribute("editUser");
if(user==null)throw new Exception("Cannot get edited user");

List<Map<String,Object>> permissionsModel = new ArrayList<Map<String,Object>>();
for(Permission p: permissions)
{
    Map<String,Object> map = new HashMap<String,Object>();
    map.put("code",p.getCode());
    map.put("description",p.getDescription());
    
    if(user.hasPermission(p.getCode()))
    {
        map.put("selected",true);
    }
    else
    {
        map.put("selected",false);
    }
        
    permissionsModel.add(map);   
}
pageContext.setAttribute("permissions",permissionsModel);

%>

<tag:link name="Delete user" url="/admin/delete-user?id=${editUser.id}" onclick="if(confirm('Are you sure you want to delete this user'))return true; else return false;"></tag:link>

<form method="post">
	<tag:panel title="General Information" align="center">
		<table border="0" width="500px" align="center">
		   <tbody>
		       <tr>
		           <td>
			           Name:<br/>
			           <tag:edit-field name="name" width="100%" value="${editUser.name}"/>
			       </td>
		       </tr>
		       <tr>
	               <td>
	                   Login:<br/><tag:edit-field name="login" width="100%" value="${editUser.login}"/>
	               </td>
	               
	               
	           </tr>
	           <tr>
	               <td>
	                   Mail:<br/><tag:edit-field name="email" width="100%" value="${editUser.email}"/>
	               </td>
	           </tr>
		       <tr>
	               <td align="center">
	                   <tag:submit value="Change"/>
	               </td>
	           </tr>
		       <tr>
		           <td>
		               <div class="error">
		               		<tag:spring-form-error field="" command="editUser"></tag:spring-form-error>
		               </div>
		           </td>
		       </tr>
		   </tbody>
		</table>
	
	</tag:panel>
	<br/>
	
	
	
	<tag:panel title="Permissions" align="center">
	   <table border="0">
	       <c:forEach items="${permissions}" var="p">
	           <tr>
	               <td>
	                   <input type="checkbox" 
	                           id="p_${p.code}" 
	                           name="p_${p.code}"
	                           <c:if test="${p.selected==true}">checked="true"</c:if>
	                       />
	                   <label for="p_${p.code}">${p.description}</label>
	               </td>
	           </tr>
	       </c:forEach>
	   </table>
	   
	</tag:panel>
</form>