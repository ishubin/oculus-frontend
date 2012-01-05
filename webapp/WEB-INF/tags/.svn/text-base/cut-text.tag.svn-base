
<%@tag import="org.apache.commons.lang.StringEscapeUtils"%><%@ tag body-content="scriptless" %>
<%@ attribute name="text" required="false"  type="java.lang.String"%>
<%@ attribute name="escapeHTML" required="false"  type="java.lang.Boolean"%>
<%@ attribute name="maxSymbols" required="true"  type="java.lang.Integer"%>
<%@ attribute name="endWith" required="false"  type="java.lang.String"%>
<jsp:doBody var="body"/>
<%
if(text==null){
	text = (String)jspContext.findAttribute("body");
}
String res="";
text = text.trim();
if(text.length()>maxSymbols)
{
    res=text.substring(0,maxSymbols);
    if(endWith!=null)res+=endWith;
}
else res = text;
if(escapeHTML!=null && escapeHTML.booleanValue())res = StringEscapeUtils.escapeHtml(res);
out.print(res);
%>