<%@ tag body-content="scriptless" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="build" required="true" %>
<%@ attribute name="projectId" required="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<%
if(build==null || build.isEmpty()) {
    jspContext.setAttribute("build", "Current Version");
}
%>

<input type="hidden" name="${id}" id="${id}" value="<tag:escape text="${build}"/>"/>
<div style="width:134px;height:19px;margin-bottom:5px;">
  <a class="pick-button" id="linkPickBuild${id}" href="javascript:onPickBuildClick('${id}',${projectId});">
      <tag:escape text="${build}"/>
  </a>
</div> 