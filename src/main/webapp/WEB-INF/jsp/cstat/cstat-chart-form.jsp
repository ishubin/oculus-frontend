<%@ include file="/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<tr>
    <td>
        Name:
        <br/>
        <tag:edit-field path="name" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Type:
        <form:select path="type">
            <form:option value="line">Line</form:option>
            <form:option value="bar">Bar</form:option>
        </form:select>
    </td>
</tr>