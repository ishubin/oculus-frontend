
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<table border="0" class="report-head-wrap custom-panel"   
        cellspacing="0" cellpadding="0" width="95%" align="center">
    <thead>
        <tr>
            <td class="report-head-corner-1"><i></i></td>
            <td class="report-head-title">${report.name}</td>
            <td class="report-head-corner-2"><i></i></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="report-head-left"><i></i></td>
            <td class="report-head-body">
                <b>Time: </b> 
                <br/>
                <b>Author: </b>
                <br/><b>Supporters:</b>
                <br/><b>Project: </b>  
            </td>
            <td class="report-head-right"><i></i></td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <td class="report-head-corner-3"><i></i></td>
            <td class="report-head-bottom"></td>
            <td class="report-head-corner-4"><i></i></td>
        </tr>
    </tfoot>
</table>
