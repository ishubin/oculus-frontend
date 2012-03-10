<jsp:directive.page import="net.mindengine.oculus.experior.reporter.nodes.ReportNode"/>
<%@ include file="/include.jsp" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<%
net.mindengine.oculus.frontend.domain.run.SuiteRun suiteRun = (net.mindengine.oculus.frontend.domain.run.SuiteRun)pageContext.findAttribute("suiteRun");
java.util.List<Object> suiteParameters = new java.util.ArrayList<Object>();
if(suiteRun!=null)
{
    String parameters = suiteRun.getParameters();
    if(parameters!=null && !parameters.isEmpty())
    {
        String[] array = parameters.split("<p>");
        for(int i=0; i<array.length; i++)
        {
            if(!array[i].isEmpty())
            {
                String temp = array[i].replace("<n>","");
                int id = temp.indexOf("<v>");
                String name = temp.substring(0,id);
                String value = temp.substring(id+3);
                java.util.Map<String,String> parameter = new java.util.HashMap<String,String>();
                parameter.put("name",name);
                parameter.put("value",value);
                
                suiteParameters.add(parameter);
            }
        }
    }
}
pageContext.setAttribute("suiteParameters", suiteParameters);
%>
<html>
    <head>
        <title>Oculus Report:
            <c:choose>
                <c:when test="${test!=null}"> 
	               ${test.name} 
	            </c:when>
	            <c:otherwise>
	               ${testRun.name}
	            </c:otherwise>
            </c:choose>
            - ${testRun.status}
        </title>
        <link rel="STYLESHEET" type="text/css" href="../scripts/jquery-ui-custom/css/jquery-ui-1.8.14.custom.css">
        <script language="javascript" src="../scripts/jquery-1.6.2.min.js"></script>
        <script language="javascript" src="../scripts/jquery-ui-custom/js/jquery-ui-1.8.14.custom.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../report.css"></link>
        <script language="javascript" src="../scripts/report.js"></script>
    </head>
    <body onload="initializeReportNodes();initializeMenuLevels();">
        
        <table class="description-table" width="100%" border="0">
            <thead>
                <tr>
                    <td colspan="2" class="description-table">
                        <c:choose>
                            <c:when test="${test!=null}"> 
                               ${test.name} 
                            </c:when>
                            <c:otherwise>
                               ${testRun.name}
                            </c:otherwise>
                        </c:choose>
                        - ${testRun.status}
                    </td>
                </tr>
            </thead>
            <tbody>
                <tr class="description-table-even">
                    <td class="description-table-left">
                        Test
                    </td>
                    <td class="description-table">
                        <c:if test="${test!=null}">
                            <a href="../test/display?id=${test.id}">${test.name}</a>
                        </c:if>
                    </td>
                </tr>
                <tr class="description-table-odd">
                    <td class="description-table-left">
                        Project
                    </td>
                    <td class="description-table">
                        <c:if test="${project!=null}">
                            <a href="../project/display-${project.path}">${project.name}</a>
                        </c:if>
                    </td>
                </tr>
                <tr class="description-table-even">
                    <td class="description-table-left">
                        Start Time
                    </td>
                    <td class="description-table">
                        <tag:date date="${testRun.startTime}"/>
                    </td>
                </tr>
                <tr class="description-table-odd">
                    <td class="description-table-left">
                        End Time
                    </td>
                    <td class="description-table">
                        <tag:date date="${testRun.endTime}"/>
                    </td>
                </tr>
                <tr class="description-table-even">
                    <td class="description-table-left">
                        Suite
                    </td>
                    <td class="description-table">
                        <c:if test="${suiteRun!=null}">
                            <a href="../report/browse?suite=${suiteRun.id}"><tag:escape text="${suiteRun.name}"/></a>
                        </c:if>
                    </td>
                </tr>
                <c:if test="${issue!=null}">
                    <tr class="description-table-odd">
	                    <td class="description-table-left">
	                        Issue
	                    </td>
	                    <td class="description-table">
	                        <c:if test="${suiteRun!=null}">
	                            <a href="../issue/display?id=${issue.id}"><img src="../images/workflow-icon-bug.png"/> <tag:escape text="${issue.name}"/></a>
	                            <c:if test="${issue.summary!=null}"><tag:escape text="${issue.summary}"/></c:if>
	                        </c:if>
	                    </td>
	                </tr>
                </c:if>
                <c:forEach items="${suiteParameters}" var="parameter" varStatus="parameterVarStatus">
                    <tr
                        <c:choose>
                            <c:when test="${parameterVarStatus.index % 2 == 0 }">
                                class="description-table-${issue!=null?'even':'odd'}"
                            </c:when>
                            <c:otherwise>
                                class="description-table-${issue!=null?'odd':'even'}"
                            </c:otherwise>
                        </c:choose> 
                    >
                        <td class="description-table-left">
	                        <img src="../images/workflow-icon-settings.png"/> ${parameter.name }
	                    </td>
	                    <td class="description-table">
	                        ${parameter.value }
	                    </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <c:if test="${testRunParameters!=null && fn:length(testRunParameters) > 0}">
            <table class="description-table" width="100%" border="0">
                <thead>
                    <tr>
                        <td colspan="2" class="description-table">
                            Test Parameters
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr class="description-table-even">
                        <td>Name</td>
                        <td>Value</td>
                    </tr>
                    <c:forEach items="${testRunParameters}" var="parameter" varStatus="parameterVarStatus">
	                    <tr
	                        <c:choose>
	                            <c:when test="${parameterVarStatus.index % 2 == 0 }">
	                                class="description-table-odd"
	                            </c:when>
	                            <c:otherwise>
	                                class="description-table-even"
	                            </c:otherwise>
	                        </c:choose> 
	                    >
	                        <td class="description-table-left">
	                            <img src="../images/workflow-icon-settings.png"/> ${parameter.name }
	                        </td>
	                        <td class="description-table">
	                           <tag:escape text="${parameter.value}"></tag:escape>
	                        </td>
	                    </tr>
	                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <br/>
        <script language="javascript">
        var report = <%out.print(pageContext.findAttribute("json"));%>;
        </script>
        
        <table class="report-table" border="0" cellpadding="0px" cellspacing="0px" width="100%">
	        <tr>
	            <th>
	                <a class="menu-item" href="javascript:collapseAllSteps();" title="Collapse all steps">
                        <img src="../images/report-menu-collapse-all-steps.png"/>
                    </a>
	                <div id="divMenuExpandLevels" style="display: inline;">
	                </div>
	                <a class="menu-item" href="javascript:expandAllSteps();" title="Expand all steps">
                        <img src="../images/report-menu-expand-all-steps.png"/>
                    </a>
                    <a class="menu-item" href="javascript:expandAllErrorSteps();" title="Expand all Error steps">
                        <img src="../images/report-menu-expand-all-error-steps.png"/>
                    </a>
                    <input type="checkbox" onchange="onShowIconsChange(this);" id="showIcons" name="showIcons" checked="checked"/><label for="showIcons" style="font-weight:8pt;">Show icons</label>
                </th>
	            <th width="150px">
	                Time:
	            </th>
	            <th width="50px">
	                Step:
	            </th>
	            <th width="100px">
	                Status:
	            </th>
	        </tr>
	    </table>
	    <div style="background:white;">
	        <br/>
	        <div class="separator"></div>
	        <c:forEach items="${report.childNodes}" var="childNode">
	        	<c:set var="node" value="${childNode}" scope="request"/>
	            <jsp:include page="/WEB-INF/jsp/report/report-node.jsp"></jsp:include>
	        </c:forEach>
	        <br/>
	    </div>
    </body>
</html>