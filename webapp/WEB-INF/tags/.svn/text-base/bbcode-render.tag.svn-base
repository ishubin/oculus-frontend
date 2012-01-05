<%@ tag body-content="scriptless" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:doBody var="body"/>
<%
String text = (String) jspContext.findAttribute("body");
if(text!=null){
	text = org.apache.commons.lang.StringEscapeUtils.escapeHtml(text);
	text = text.replaceAll("(\r\n|\n\r|\n|\r)","<br/>");
	text = text.replaceAll("\\[b\\](.*?)\\[/b\\]", "<b>$1</b>");
	text = text.replaceAll("\\[i\\](.*?)\\[/i\\]", "<i>$1</i>");
	text = text.replaceAll("\\[u\\](.*?)\\[/u\\]", "<u>$1</u>");
	text = text.replace("[ul]","<ul>");
	text = text.replace("[/ul]","</ul>");
	text = text.replace("[li]","<li>");
	text = text.replace("[/li]","</li>");
	text = text.replaceAll("\\[url\\](.*?)\\[/url\\]", "<a class=\"custom-link\" href=\"$1\" target=\"_blank\">$1</a>");
	text = text.replaceAll("\\[url=(.*?)\\](.*?)\\[/url\\]", "<a class=\"custom-link\" href=\"$1\" target=\"_blank\">$2</a>");
	text = text.replaceAll("\\[size=?([0-9]|[1-2][0-9])?\\](.*?)\\[/size\\]","<font size=\"$1\">$2</font>");
	text = text.replaceAll("\\[color=(.*?)\\](.*?)\\[/color\\]","<span style=\"color:$1\">$2</span>");
	text = text.replaceAll("\\[img\\](.*?)\\[/img\\]", "<img src=\"$1\"/>");
	text = text.replaceAll("\\[parameter\\](.*?)\\[/parameter\\]", "<img src=\"../images/small-gear.png\"/><font color=\"#ff00ff\">$1</font>");
	
	out.print(text);
}
%>