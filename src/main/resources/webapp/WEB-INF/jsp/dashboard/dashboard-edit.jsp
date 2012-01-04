<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:panel align="center" title="Edit Dashboard">
	<form:form commandName="editDashboard" method="post" >
	    <table border="0" width="100%">
	       <tr>
	           <td>
	               Runner Id: <br/>
	               <tag:edit-field path="runnerId"></tag:edit-field>
	           </td>
	       </tr>
	       <tr>
               <td>
                   Days period: <br/>
                   <tag:edit-field path="daysPeriod"></tag:edit-field>
               </td>
           </tr>
           <tr>
               <td>
                   Summary Statistics:<form:checkbox path="summaryStatistics"/>
               </td>
           </tr>
           <tr>
               <td>
                   Health chart for each project:<form:checkbox path="healthChart"/>
               </td>
           </tr>
	       <tr>
	           <td align="center">
	                <tag:submit value="Change"/>
	                <br/>
	                <form:errors path=""/>
	           </td>
           </tr>
	    </table>
	</form:form>
</tag:panel>