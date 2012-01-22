<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<tr>
    <td>
        Name:
        <br/>
        <tag:edit-field name="name" width="100%"  value="${customStatistic.name}"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Short name:
        <br/>
        <tag:edit-field name="shortName" width="100%" value="${customStatistic.shortName}"></tag:edit-field>
    </td>
</tr>
<tr>
    <td>
        Description:
        <br/>
        <textarea name="description" style="width:100%;" rows="7"><tag:escape text="${customStatistic.description}"/></textarea>
    </td>
</tr>
<tr>
    <td>
        Value type:
        <br/>
        <select name="valueType">
            <option value="time">Time</option>
            <option value="number">Number</option>
        </select>
    </td>
</tr>