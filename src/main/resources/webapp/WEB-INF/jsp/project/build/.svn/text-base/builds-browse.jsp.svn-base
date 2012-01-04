<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div class="breadcrump">
    <a href="../project/display-${project.path }"><img src="../images/workflow-icon-project.png"/> ${project.name }</a>
    <img src="../images/breadcrump-arrow.png"/> 
    Builds 
    
</div>


<table id="buildsTable">
    <tr>
        <th>Build</th>
        <th>Date</th>
        <th> </th>
    </tr>
    <c:forEach items="${builds}" var="build">
        <tr>
            <td><tag:remove-white-space>
                <a href="../project/build-display?id=${build.id}">
                    <img src="../images/workflow-icon-build.png"/> <tag:escape text="${build.name}"/>
                </a>
                <c:if test="${build.description!=null && build.description!=''}">
                    <br/> 
                    <tag:escape text="${build.description}"/>
                </c:if>
            </tag:remove-white-space></td>
            <td width="100px"><tag:date date="${build.date}"/></td>
            <td width="80px"><tag:remove-white-space>
                <c:if test="${manageBuildAllowed==true}">
                    <a href="javascript:onRemoveBuild('${build.name}', ${build.id});">Remove</a>
                </c:if>
            </tag:remove-white-space></td>
        </tr>
    </c:forEach>
</table>
<script>
$(document).ready(function(){
    tableToGrid("#buildsTable",{
        height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'Builds'
    });
});
</script>