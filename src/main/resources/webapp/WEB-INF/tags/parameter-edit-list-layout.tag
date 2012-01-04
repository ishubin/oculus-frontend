<%@tag import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="layoutName" required="true" type="java.lang.String" %>
<%@ attribute name="parameter" required="false" type="net.mind_engine.oculus.domain.test.TestParameter" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%-- 
    This tag is used to render the list layout for editing possible values for test parameter
    Used only in test-customize.jsp
    All the javascript handlers for this layout are specified in controls.js
 --%>
 
 <script language="javascript">
 var _testParametrizationPossibleValueDefault<%=layoutName%> = <%=(parameter==null?"null":"\""+StringEscapeUtils.escapeJavaScript(parameter.getDefaultValue())+"\"")%>;
 var _testParametrizationPossibleValueList<%=layoutName%> = [
<%
Boolean hasPossibleValues = false;

if(parameter!=null && parameter.getControlType().equals("list"))
{
    boolean bSep = false;
    java.util.List<String> possibleValues = parameter.getPossibleValuesList();
    for(String pv : possibleValues)
    {
        if(bSep)out.print(",");
        bSep=true;
        out.print("\""+StringEscapeUtils.escapeJavaScript(pv)+"\"");
    }
    
    if(possibleValues.size()>0)hasPossibleValues = true;
}
%>
];
 </script>
 <div id="divPVListPossibleValuesLayout${layoutName}">
 
    
 </div>
 <script language="javascript">
 onPVListRenderList('<%=layoutName%>');
 </script>
 New Possible Value:<br/>
 <table border="0">
     <tr>
         <td><input id="pv_list_parameter_name_${layoutName}" name="pv_list_new_parameter_name" type="text" /></td>
         <td><input type="button" value="Add" onclick="onPVListAddPossibleValue('${layoutName}'); return false;"/></td>
     </tr>
 </table>