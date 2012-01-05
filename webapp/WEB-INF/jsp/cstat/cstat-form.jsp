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
        Short name:
        <br/>
        <tag:edit-field path="shortName" width="100%"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Description:
        <br/>
        <form:textarea path="description" cssStyle="width:100%;" rows="7"/>
    </td>
</tr>
<tr>
    <td>
        Value type:
        <br/>
        <form:select path="valueType">
            <form:option value="time">Time</form:option>
            <form:option value="number">Number</form:option>
        </form:select>
    </td>
</tr>