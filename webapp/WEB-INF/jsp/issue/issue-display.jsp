<%@page import="net.mindengine.oculus.frontend.domain.issue.IssueCollationTest"%>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>


<%@page import="java.util.Collection"%>
<%@page import="net.mindengine.oculus.frontend.domain.issue.IssueCollation"%>

<div class="breadcrump" align="center">
    <c:if test="${project!=null}">
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
    <img src="../images/breadcrump-arrow.png"/> 
    </c:if>
    <c:choose>
        <c:when test="${issue.name!=null && issue.name!=''}">
            <img src="../images/workflow-icon-bug.png"/> ${issue.name}
        </c:when>
        <c:otherwise>
            <a href="${issue.link}"><img src="../images/workflow-icon-bug.png"/> ${issue.link}</a>
        </c:otherwise>
    </c:choose>
</div>

<table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <td class="issue-table" colspan="2">
                <b><img src="../images/workflow-icon-bug.png"/> ${issue.name}</b>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr class="odd">
            <td class="issue-table-left">
                Name:
            </td>
            <td class="issue-table">
                <tag:escape text="${issue.name}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Summary:
            </td>
            <td class="issue-table">
                <tag:escape text="${issue.summary}"/>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                External Link:
            </td>
            <td class="issue-table">
                <c:if test="${issue.link!=null && issue.link!=''}">
                    <a href="<tag:escape text="${issue.link}"/>"><tag:escape text="${issue.link}"/></a>
                </c:if>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Description:
            </td>
            <td class="issue-table">
                <tag:bbcode-render>${issue.description}</tag:bbcode-render>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Author:
            </td>
            <td class="issue-table">
                <c:if test="${author!=null}">
                    <a href="../user/profile-${author.login}"><img src="../images/workflow-icon-user.png"/> ${author.name}</a>
                </c:if>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Project:
            </td>
            <td class="issue-table">
                <c:if test="${project!=null}">
                    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> ${project.name}</a>
                </c:if> 
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Sub-Project:
            </td>
            <td class="issue-table">
                <c:if test="${subproject!=null}">
                    <a href="../project/display-${subproject.path}"><img src="../images/workflow-icon-project.png"/> ${subproject.name}</a>
                </c:if> 
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Date:
            </td>
            <td class="issue-table">
                <tag:date date="${issue.date}"/> 
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Dependent Tests:
            </td>
            <td class="issue-table">
                ${issue.dependentTests} 
            </td>
        </tr>
        <c:forEach items="${customizationGroups}" var="cg">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <c:choose>
                        <c:when test="${cg.isMain==true}">
                             Main
                        </c:when>
                        <c:otherwise>
                             ${cg.name}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:forEach items="${cg.customizations}" var="c" varStatus="cVarStatus">
                 <tr class="${cVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         ${c.customization.name}
                     </td>
                     <td class="issue-table">
                         <tag:customization-display customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"/>
                     </td>
                 </tr>
            </c:forEach>
        </c:forEach>
    </tbody>
</table>

<br/>
<br/>

<script language="javascript">
<%
Collection<IssueCollation> collations = (Collection<IssueCollation>) pageContext.findAttribute("collations");
if(collations!=null)
{
    StringBuffer buff = new StringBuffer();
    for(IssueCollation collation : collations)
    {
        if(collation.getTests()!=null && collation.getTests().size()>0)
        {
            buff.append("var _collation_"+collation.getId()+" = [");
            boolean comma = false;
            for(IssueCollationTest test : collation.getTests())
            {
                if(comma) buff.append(",");
                comma = true;
                buff.append(test.getId());
            }
            buff.append("];\n");
        }
    }
    out.println(buff.toString());
}
%>
function onCollationCheckboxAll(collation, checkbox, collationId)
{
	
	var input;
	for(var i=0; i<collation.length; i++)
	{
		input = document.getElementById("chkTest_"+collationId+"_"+collation[i]);
		input.checked = checkbox.checked;
	}		
}
</script>

<c:if test="${issue.dependentTests > 0}">
    <a href="../issue/display?id=${issue.id}&displayDependentTests=true">Show dependent tests (${issue.dependentTests})</a>
    <form action="../issue/remove-issue-collations">
        <input type="hidden" name="redirect" value="../issue/display?id=${issue.id}&displayDependentTests=true"/>
        <input type="hidden" name="issueId" value="${issue.id}"/>
	    <c:if test="${collations!=null}">
	        <table id="collationsTable" border="0">
	            <tr>
	                <th> </th>
	                <th>Tests</th>
	                <th>Suite parameters</th>
	                <th>Reason pattern</th>
	            </tr>
	            <c:forEach items="${collations}" var="collation">
	                <tr>
	                    <td> </td>
	                    <td><tag:remove-white-space>
	                        <input type="checkbox" name="chkAllTests_${collation.id}" onchange="onCollationCheckboxAll(_collation_${collation.id},this, ${collation.id});" id="chkAllTests_${collation.id}"/><label for="chkAllTests_${collation.id}">Select all</label>
	                        <ul style="list-style-image: url(../images/iconTest.png);">
	                            <c:forEach items="${collation.tests}" var="test">
	                                <li>
	                                    <input type="checkbox" name="chkTest_${collation.id}_${test.id}" id="chkTest_${collation.id}_${test.id}"/>
	                                    <c:choose>
	                                       <c:when test="${test.testId>0}">
	                                           <a href="../test/display?id=${test.testId}"><tag:escape text="${test.testName}"></tag:escape></a>
	                                           (from <a href="../project/display-${test.projectPath}"><img src="../images/workflow-icon-project.png"></img> <tag:escape text="${test.projectName}"></tag:escape></a>)
	                                       </c:when>
	                                       <c:otherwise>
	                                           <span style="font-size: 10pt;font-weight: bold;"><tag:escape text="${test.testName}"></tag:escape></span>
	                                       </c:otherwise>
	                                    </c:choose>
	                                </li>
	                            </c:forEach>
	                        </ul>
	                    </tag:remove-white-space></td>
	                    <td><tag:remove-white-space>
	                        <c:choose>
	                            <c:when test="${fn:length(collation.conditions) == 0}">
                                   <span style="color:#aaaaaa;"><i>Not specified</i></span>
                                </c:when>
                                <c:otherwise>
                                   <ul style="list-style: none;margin: 0;">
		                                <c:forEach items="${collation.conditions}" var="condition">
		                                    <li>
		                                        <img src="../images/workflow-icon-settings.png"></img> 
		                                        <b>${condition.trmPropertyName}</b>
		                                        <span style="color:gray;">=</span> 
		                                        ${condition.value}
		                                    </li>
		                                </c:forEach>
		                            </ul>
                                </c:otherwise>
                            </c:choose>
	                    </tag:remove-white-space></td>
	                    <td><tag:remove-white-space>
	                       <c:choose>
	                           <c:when test="${collation.reasonPattern!=null && collation.reasonPattern!=''}">
	                               <tag:escape text="${collation.reasonPattern}"/>
	                           </c:when>
	                           <c:otherwise>
	                               <span style="color:#aaaaaa;"><i>Not specified</i></span>
	                           </c:otherwise>
	                       </c:choose>
	                    </tag:remove-white-space></td>
	                </tr>
	            </c:forEach>
	        </table>
	        <br/>
	        <tag:submit value="Remove Selected Tests" name="Submit" width="200px"></tag:submit>
	        <script>
		    $(document).ready(function(){
		        tableToGrid("#collationsTable",{
		            height:'auto',
		            width:'auto',
		            hidegrid:true,
		            caption:'Dependent tests'
		        });
		    });
		    </script>
	    </c:if>
    </form>
</c:if>


<!-- Comments layout -->
<br/>
<tag:comments-layout user="${user}" comments="${comments}" redirect="../issue/display?id=${issue.id}" unitId="${issue.id}" unit="issue"></tag:comments-layout>
