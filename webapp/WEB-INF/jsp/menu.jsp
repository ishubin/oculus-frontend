<%@ include file="/include.jsp" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/struts-tiles.tld" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<ul id="menu"  class="sf-menu">
    <li>
        <a class="sf-with-ul sf-header" href="<c:url value="../display/home"/>"><img src="../images/menu-icon-home.png"/> Home</a>
    </li>
    <li>
        <a  class="sf-with-ul sf-header" href="#"><img src="../images/menu-icon-project.png"/>  Projects</a>
        <ul>
        	<li>
        		<a  class="sf-with-ul" href="../project/browse"><img src="../images/workflow-icon-project.png"/> All Projects</a>
        	</li>
            <li>
                 <a class="sf-with-ul" href="../project/search">
                      <img src="../images/workflow-icon-search.png"/>
                      Sub-Projects
                 </a>
            </li>
            <li>
                 <a class="sf-with-ul" href="../test/search">
                      <img src="../images/workflow-icon-search.png"/>
                      Tests
                 </a>
            </li>
            <li>
                 <a class="sf-with-ul" href="../project/build-search">
                      <img src="../images/workflow-icon-search.png"/>
                      Builds
                 </a>
            </li>
        </ul>
    </li>
    <li>
        <a class="sf-with-ul sf-header" href="#"><img src="../images/menu-icon-report.png"/> Reports</a>
        <ul>
        	<li>
        		<a class="sf-with-ul" href="../report/browse"><img src="../images/workflow-icon-search.png"/> Search Reports</a>
        	</li>
        	<c:if test="${user!=null}">
            	<li><a class="sf-with-ul" href="<c:url value="../report/my-filters"/>"><img src="../images/workflow-icon-filter.png"/> My Filters</a></li>
            </c:if>
        </ul>
    </li>
    <li>
        <a class="sf-with-ul sf-header" href="#"><img src="../images/menu-icon-issue.png"/> Issues</a>
        <ul>
            <li><a class="sf-with-ul" href="../issue/search"><img src="../images/workflow-icon-search.png"/> Search Issues</a></li>
            <c:if test="${user.hasPermissions.issue_managment == true}">
                <li><a class="sf-with-ul" href="../issue/create"><img src="../images/workflow-icon-bug-create.png"/> Create Issue</a></li>
            </c:if>
        </ul>
    </li>
    <c:if test="${user!=null}">
	    <li>
	        <a class="sf-with-ul sf-header" href="#"><img src="../images/menu-icon-trm.png"/> Grid</a>
            <ul>
                  <li>
                      <a class="sf-with-ul" href="../grid/create-task">
                          <img src="../images/workflow-icon-task.png"/>
                          Create Task
                      </a>
                  </li>
                  <li>
                      <a class="sf-with-ul" href="../grid/my-active-tasks">
                          <img src="../images/workflow-icon-monitor.png"/>
                          My Active Tasks
                      </a>
                  </li>
                  <li>
                      <a class="sf-with-ul" href="../grid/my-tasks">
                          <img src="../images/workflow-icon-save.png"/>
                          My Saved Tasks
                      </a>
                  </li>
                  <li>
                      <a class="sf-with-ul" href="../grid/shared-tasks">
                          <img src="../images/workflow-icon-shared-task.png"/>
                          Shared Tasks
                      </a>
                  </li>
                  <li>
                      <a class="sf-with-ul" href="../grid/agents">
                          <img src="../images/workflow-icon-agents.png"/>
                          Agents
                      </a>
                  </li>
                  <c:if test="${user.hasPermissions.trm_administration == true}">
                   <li>
                       <a class="sf-with-ul" href="#">
                           <img src="../images/workflow-icon-settings.png"/>
                           Settings
                       </a>
                       <ul>
                              <li>
                                  <a class="sf-with-ul" href="../grid/customize-suite-parameters">
                                      <img src="../images/workflow-icon-settings.png"/>
                                      Suite Parameters
                                  </a>
                              </li>
                              <li>
                                  <a class="sf-with-ul" href="../grid/upload-project-choose-project">
                                      <img src="../images/workflow-icon-upload.png"/>
                                      Upload Project
                                  </a>
                              </li>
                          </ul>
                   </li>
                  </c:if>
            </ul>
	    </li>
	</c:if>
    <li>
        <a class="sf-with-ul sf-header" href="#"><img src="../images/menu-icon-user.png"/> User</a>
        <ul>
            <c:choose>
                <c:when test="${user != null}">
                    <li><a class="sf-with-ul" href="<c:url value="../user/profile-${user.login}"/>">
                            <img src="../images/workflow-icon-user.png"/>
                            My Profile
                        </a>
                    </li>
                    <li><a class="sf-with-ul" href="<c:url value="../user/logout"/>"><img src="../images/workflow-icon-exit.png"/> Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li><a class="sf-with-ul" href="<c:url value="../user/login"/>"><img src="../images/workflow-icon-login.png"/> Login</a></li>
                </c:otherwise>
            </c:choose>
            
            <c:if test="${user!=null && user.hasPermissions.user_managment}">
                <li>
                    <a class="sf-with-ul" href="#"><img src="../images/workflow-icon-usergroup.png"/> User Management</a>
                    <ul>
                        <li>
                            <a class="sf-with-ul" href="../admin/user-create">
                                <img src="../images/workflow-icon-add-user.png"/>
                                Create User
                            </a>
                        </li>
                    </ul>
                </li>
            </c:if>
            <c:if test="${user!=null && user.hasPermissions.project_managment}">
                <li>
                    <a class="sf-with-ul" href="#"><img src="../images/workflow-icon-project.png"/> Project Management</a>
                    <ul>
                        <li>
                            <a class="sf-with-ul" href="<c:url value="../project/create"/>">New Project</a>
                        </li>
                    </ul>
                </li>
            </c:if>
        </ul>
    </li>
</ul>
<script>
$("#menu").supersubs({ 
    minWidth:    12, 
    maxWidth:    27, 
    extraWidth:  1  
}).superfish();
</script>
