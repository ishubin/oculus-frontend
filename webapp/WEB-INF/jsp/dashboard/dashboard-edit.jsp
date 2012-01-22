<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<tag:panel align="left" title="Edit Dashboard" width="250px">
	<form method="post" >
	    <table border="0" width="100%">
	       <tr>
	           <td>
	               Runner Id: <br/>
	               <tag:edit-field width="100%" name="runnerId" value="${editDashboard.runnerId}"></tag:edit-field>
	           </td>
	       </tr>
	       <tr>
               <td>
                   Days period: <br/>
                   <tag:edit-field width="100%" name="daysPeriod" value="${editDashboard.daysPeriod}"></tag:edit-field>
               </td>
           </tr>
           <tr>
               <td>
                   Summary Statistics:
                   <tag:form-checkbox name="summaryStatistics" value="${editDashboard.summaryStatistics}"></tag:form-checkbox>
               </td>
           </tr>
           <tr>
               <td>
                   Health chart for each project:
                   <tag:form-checkbox name="healthChart" value="${editDashboard.healthChart}"></tag:form-checkbox>
               </td>
           </tr>
	       <tr>
	           <td align="center">
	                <tag:submit value="Change"/>
	                <br/>
	                <div class="error">
	                	<tag:spring-form-error field="" command="editDashboard"></tag:spring-form-error>
	                </div>
	           </td>
           </tr>
	    </table>
	</form>
</tag:panel>