<%@page import="java.util.List"%>
<%@page import="net.mindengine.oculus.frontend.domain.trm.TrmSuite"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="breadcrump">
    <a href="../test-run-manager/shared-tasks"><img src="../images/workflow-icon-shared-task.png"/>  Shared Tasks</a>
    <img src="../images/breadcrump-arrow.png"/> 
    ${task.name} 
</div>

<script language="javascript">
function showSopySharedTaskDialog()
{
	showPopup("divCopySuiteDialog",300,250);
}
</script>

<div id="divCopySuiteDialog" style="position:absolute;display:none;width:300px;height:250px;">
    <tag:panel align="center" title="Copy this task" closeDivName="divCopySuiteDialog" width="300px" height="250px">
        <form action="../test-run-manager/copy-shared-task">
            <input type="hidden" name="taskId" value="${task.id}"/>
	        <table border="0" cellpadding="5" width="100%">
	            <tr>
	                <td colspan="2">
	                    Name:
	                    <br/>
	                    <tag:edit-field-simple width="100%" name="name" value="${task.name}" escapeHtml="true"></tag:edit-field-simple>
	                </td>
	            </tr>
	            <tr>
	               <td colspan="2">
	                   Description:<br/>
	                   <textarea name="description" style="width:100%;" rows="5"><tag:escape text="${task.description}"/></textarea>
	               </td>
	            </tr>
	            <tr>
	               <td>
	                   <tag:submit value="Copy" name="Submit"></tag:submit>
	               </td>
	               <td>
	                   <tag:submit value="Cancel" name="Submit" onclick="closePopup('divCopySuiteDialog'); return false;"></tag:submit>
	               </td>
	            </tr>
	        </table>
        </form>
    </tag:panel>
</div>

<table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <td class="issue-table" colspan="2">
                Task - <b><tag:escape text="${task.name}"/></b>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr class="odd">
            <td class="issue-table-left">
                Name:
            </td>
            <td class="issue-table">
               <tag:escape text="${task.name}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Description:
            </td>
            <td class="issue-table">
                <tag:nl2br><tag:escape text="${task.description}"/></tag:nl2br>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Created:
            </td>
            <td class="issue-table">
                <tag:date date="${task.date }"></tag:date>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                User:
            </td>
            <td class="issue-table">
                <a href="../user/profile-${task.userLogin}">
                    <img src="../images/workflow-icon-user.png"/>
                    <tag:escape text="${task.userName}"/>
                </a>
            </td>
        </tr>
        
    </tbody>
</table>


<h2>Suites:</h2>

<table id="tasksTable">
    <tr>
        <th>Name</th>
    </tr>
    <c:forEach items="${suites}" var="suite">
        <tr>
            <td><tag:remove-white-space>
                <b>
                    <c:choose>
                        <c:when test="${suite.uniteTests==true }">
                            <img src="../images/workflow-icon-united-suite.png"/>
                        </c:when>
                        <c:otherwise>
                            <img src="../images/workflow-icon-suite.png"/>
                        </c:otherwise>
                    </c:choose>
	                <tag:escape text="${suite.name}"></tag:escape>
                </b>
                <br/>
                <span class="small-description"><tag:cut-text text="${suite.description}" maxSymbols="50" escapeHTML="true"/></span>
            </tag:remove-white-space></td>
        </tr>
    </c:forEach>
</table>
<script>
$(document).ready(function(){
    tableToGrid('#tasksTable',{
        height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'Tasks'
    });
});
</script>


