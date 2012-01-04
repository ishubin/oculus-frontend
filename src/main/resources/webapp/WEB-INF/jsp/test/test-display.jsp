<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:directive.page import="net.mindengine.oculus.frontend.web.*"/>
<%@ include file="/session-handler.jsp" %>

<div class="breadcrump" align="center">
    <a href="../project/display-${parentProject.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${parentProject.name}"/></a>
    <img src="../images/breadcrump-arrow.png"/> 
    <a href="../project/display-${project.path}"><img src="../images/workflow-icon-subproject.png"/> <tag:escape text="${project.name}"/></a>
    <c:if test="${test.groupId>0}">
        <img src="../images/breadcrump-arrow.png"/>
        <a href="../project/display-${project.path}?groupId=${test.groupId}">
            <img src="../images/iconTestGroup.png"/>
            <tag:escape text="${test.groupName}"/>
        </a>
    </c:if>
    <img src="../images/breadcrump-arrow.png"/> 
    <span style="white-space: nowrap;"><img src="../images/iconTest.png"/> ${test.name}</span>
</div>

<script language="javascript">
var tree = null;
var currentProjectId = 0;
function loadFolderTree(projectId)
{
    currentProjectId = projectId;
    tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("../dhtmlxTree/imgs/csh_dhx_skyblue/");
    tree.enableDragAndDrop(0);
    tree.enableTreeLines(true);
    tree.setXMLAutoLoading("../document/display-folders");
    
    var d = new Date();
    var str = ""+d.getDate()+""+d.getMonth()+""+d.getSeconds()+""+d.getMilliseconds();
    tree.loadXML("../document/display-folders?projectId="+projectId+"&tmstp="+str);

    tree.attachEvent("onSelect", onTreeElementSelect);
}
function onTreeElementSelect()
{
    var selectId = tree.getSelectedItemId();
    if(selectId.substring(1,3)=="tc")
    {
        var testCaseId = selectId.substring(3);
        document.forms.linkWithTestCase.testCaseId.value = testCaseId;
        document.forms.linkWithTestCase.submit();
    }
}
function onLinkTestWithTestCaseClick()
{
	if(tree==null){
		loadFolderTree(<%out.print(((net.mindengine.oculus.frontend.domain.project.Project)pageContext.findAttribute("project")).getId());%>);
	}
    showPopup("divLinkTestWithTestCaseDialog",400,500);
}
</script>

<form name="linkWithTestCase" action="../test/link-with-test-case" method="post">
    <input type="hidden" name="testId" value="${test.id}"/>
    <input type="hidden" name="testCaseId" value=""/>
</form>
<div id="divLinkTestWithTestCaseDialog" style="display:none;">
    <tag:panel align="center" title="Select test-case..." width="100%" height="100%" closeDivName="divLinkTestWithTestCaseDialog">
        <div id="treeboxbox_tree" style="border:1px solid #cccccc;width:350; height:450; overflow:auto;"></div>
    </tag:panel>
</div>

<table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <td class="issue-table" colspan="2">
                Test - <b><tag:escape text="${test.name}"/></b>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr class="odd">
            <td class="issue-table-left">
                Name:
            </td>
            <td class="issue-table">
               <tag:escape text="${test.name}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Description:
            </td>
            <td class="issue-table">
                <tag:bbcode-render>${test.description}</tag:bbcode-render>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Mapping:
            </td>
            <td class="issue-table">
                <tag:escape text="${test.mapping}"/>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Project:
            </td>
            <td class="issue-table">
                <a href="../project/display-${project.path}"><img src="../images/workflow-icon-project.png"/> <tag:escape text="${project.name}"/></a>
            </td>
        </tr>
        <tr class="odd">
            <td class="issue-table-left">
                Author:
            </td>
            <td class="issue-table">
                <a href="../user/profile-${testAuthor.login}">
                    <img src="../images/workflow-icon-user.png"/>
                    <tag:escape text="${testAuthor.name}"/>
                </a>
            </td>
        </tr>
        <tr class="even">
            <td class="issue-table-left">
                Group:
            </td>
            <td class="issue-table">
                <c:if test="${test.groupId>0}">
                    <a href="../project/display-${project.path}?groupId=${test.groupId}">
                        <img src="../images/workflow-icon-test-group.png"/>
                        <tag:escape text="${test.groupName}"/>
                    </a>
                </c:if>
            </td>
        </tr>
        <c:if test="${testInputParametersCount>0}">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <img src="../images/workflow-icon-settings.png"/> Input Parameters
                </td>
            </tr>
            <c:forEach items="${testInputParameters}" var="p" varStatus="pVarStatus">
                 <tr class="${pVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         <tag:escape text="${p.name}"></tag:escape>
                     </td>
                     <td class="issue-table">
                         <tag:nl2br><tag:escape text="${p.description}"></tag:escape></tag:nl2br>
                     </td>
                 </tr>
            </c:forEach>
        </c:if>
        <c:if test="${testOutputParametersCount>0}">
            <tr>
                <td class="issue-table-separator" colspan="2">
                    <img src="../images/workflow-icon-settings.png"/> Output Parameters
                </td>
            </tr>
            <c:forEach items="${testOutputParameters}" var="p" varStatus="pVarStatus">
                 <tr class="${pVarStatus.index % 2 == 0 ? 'even' : 'odd'}">
                     <td class="issue-table-left">
                         <tag:escape text="${p.name}"></tag:escape>
                     </td>
                     <td class="issue-table">
                         <tag:nl2br><tag:escape text="${p.description}"></tag:escape></tag:nl2br>
                     </td>
                 </tr>
            </c:forEach>
        </c:if>
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
                         <tag:escape text="${c.customization.name}"/>
                     </td>
                     <td class="issue-table">
                         <tag:customization-display customization="${c.customization}" possibleValues="${c.possibleValues}" assignedUser="${c.assignedUser}"/>
                     </td>
                 </tr>
            </c:forEach>
        </c:forEach>
    </tbody>
</table>


<c:if test="${linkedTCDocument!=null}">
    <br/>
    <br/>
    <table class="issue-table" width="100%" border="0" cellpadding="0" cellspacing="0">
       <thead>
           <tr>
               <td class="issue-table" colspan="2">
               		<img src="../images/workflow-icon-link-to-file.png"/>Test-Case: 
               		<a href="../document/project-${parentProject.path}?testCaseId=${linkedTCDocument.id}">
               			<tag:escape text="${linkedTCDocument.name}"/>
               		</a>
               </td>
           </tr>
       </thead>
       <tbody>
           <tr class="odd">
               <td class="issue-table-left">Description:</td>
               <td class="issue-table">
                    <tag:bbcode-render>${linkedTCDocument.description}</tag:bbcode-render>
               </td>
           </tr>
           <tr class="even" >
               <td class="issue-table-left">Steps:</td>
               <td class="issue-table" style="padding:0px;">
                   <c:choose>
                       <c:when test="${linkedTCTestcase.stepsAmount > 0}">
                          <table class="test-case" width="100%" border="0" cellpadding="0" cellspacing="0">
                              <thead>
                                  <tr>
                                      <td width="50px" class="test-case" style="border-left:none;">#</td>
                                      <td width="50%" class="test-case" style="border-left:none;">Action</td>
                                      <td width="50%" class="test-case" style="border-right:none;">Expected Result</td>
                                  </tr>
                              </thead>
                              <tbody>
                                  <c:forEach items="${linkedTCTestcase.steps}" var="testCaseStep" varStatus="testCaseStepStatus">
                                      <tr>
                                          <td class="test-case" style="border-left:none;">${testCaseStepStatus.index+1}</td>
                                          <td class="test-case" style="border-left:none;height:50px;"><tag:bbcode-render>${testCaseStep.action}</tag:bbcode-render>
                                          <td class="test-case" style="border-right:none;"><tag:bbcode-render>${testCaseStep.expected}</tag:bbcode-render>
                                      </tr>
                                  </c:forEach>
                              </tbody>
                          </table>    
                       </c:when>
                       <c:otherwise>
                           There are no steps        
                       </c:otherwise>
                   </c:choose>
               </td>
           </tr>
           <tr class="odd">
            <td class="issue-table-left">
                Author:
            </td>
            <td class="issue-table">
                <a href="../user/profile-${linkedTCAuthor.login}"><tag:escape text="${linkedTCAuthor.name}"/></a>
            </td>
        </tr>
       </tbody>
    </table>
</c:if>

<c:if test="${linkedTCError!=null}">
    <div style="color:red;">${linkedTCError}</div>
</c:if>

<!-- Comments layout -->
<br/>
<tag:comments-layout user="${user}" comments="${comments}" redirect="../test/display?id=${test.id}" unitId="${test.id}" unit="test"></tag:comments-layout>
