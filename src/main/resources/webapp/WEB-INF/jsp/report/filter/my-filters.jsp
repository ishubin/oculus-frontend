<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="breadcrump">
    My Filters
</div>

<script language="javascript">
function onRemoveFilter(filterId)
{
	if(confirm("Are you sure you want to delete this filter?"))
	{
		document.forms.removeFilter.filterId.value = filterId;
		document.forms.removeFilter.submit();
	}
}
</script>

<form name="removeFilter" action="../report/remove-filter" method="post">
    <input type="hidden" name="filterId" value=""/>
</form>
<table id="filtersTable" border="0">
    <tr>
        <th>Name</th>
        <th width="120px">Created</th>
        <th width="120px"> </th>
    </tr>
    <c:forEach items="${filters}" var="filter">
        <tr>
            <td><a href="../${filter.filter}"><img src="../images/workflow-icon-filter.png"/><tag:escape text="${filter.name}" ext="html"/></a><c:if test="${filter.description!=null && filter.description!=''}"><br/><tag:escape text="${filter.description}" ext="html"/></c:if></td>
            <td><tag:date date="${filter.date}"/></td>
            <td><a href="javascript:onRemoveFilter(${filter.id})">Remove</a></td>
        </tr>
    </c:forEach>
</table>
<script>
$(document).ready(function(){
    tableToGrid("#filtersTable",{
        height:'auto',
        width:'auto',
        hidegrid:true,
        caption:'Sub-Projects'
    });
});
</script>