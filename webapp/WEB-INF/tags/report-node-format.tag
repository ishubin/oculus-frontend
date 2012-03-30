
<%@tag import="java.util.regex.Pattern"%>
<%@tag import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="text" required="true"  type="java.lang.String"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
if(text!=null)
{
    text = StringEscapeUtils.escapeHtml(text);
    text = text.replaceAll("\\[b\\](.*?)\\[/b\\]", "<b>$1</b>");
    text = text.replaceAll("\\[i\\](.*?)\\[/i\\]", "<i>$1</i>");
    
    text = text.replaceAll("\\[xpath\\](.*?)\\[/xpath\\]", "<code class=\"xpath\">$1</code>");
    text = text.replaceAll("\\[code\\](.*?)\\[/code\\]", "<code>$1</code>");
    
    text = text.replace("[br/]", "<br/>");
    text = text.replace("[tr]", "<tr>");
    text = text.replace("[/tr]", "</tr>");
    text = text.replace("[td]", "<td>");
    text = text.replace("[/td]", "</td>");
    text = text.replace("[tbody]", "<tbody>");
    text = text.replace("[/tbody]", "</tbody>");
    text = text.replace("[thead]", "<thead>");
    text = text.replace("[/thead]", "</thead>");
    text = text.replace("[table]", "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"custom-table\">");
    text = text.replace("[/table]", "</table>");
    
    text = text.replace("[null-value/]", "<span style=\"color:#444400;\"><i>null</i></span>");
    
    text = Pattern.compile("\\[stack-trace\\](.*?)\\[/stack-trace\\]", Pattern.DOTALL).matcher(text).replaceAll("<code class=\"exception-stack-trace\">$1</code>");
    
    text = Pattern.compile("\\[string\\](.*?)\\[/string\\]", Pattern.DOTALL).matcher(text).replaceAll("<span class=\"string\">$1</span>");
    text = Pattern.compile("\\[number\\](.*?)\\[/number\\]", Pattern.DOTALL).matcher(text).replaceAll("<span class=\"number\">$1</span>");
    text = Pattern.compile("\\[tip\\](.*?)\\[/tip\\]", Pattern.DOTALL).matcher(text).replaceAll("<span class=\"tip\">$1</span>");

    text = text.replaceAll("\\[chart\\](.*?)\\[/chart\\]", "<a href=\"../chart/display?chartId=$1&width=1024&height=768\" target=\"_blank\"><img src=\"../chart/display?chartId=$1&width=600&height=400\"/></img>");
    
    text = text.replaceAll("\\[url\\](.*?)\\[/url\\]", "<a class=\"custom-link\" href=\"$1\" target=\"_blank\">$1</a>");
    
    text = text.replaceAll("\\[clink=['\"](.*?)['\"]\\](.*?)\\[/clink\\]", "<a class=\"custom-link\" href=\"$1\" target=\"_blank\">$2</a>");
    
    text = text.replace("[textarea]", "<textarea rowspan=\"10\" style=\"width:100%;\">");
    text = text.replace("[/textarea]", "</textarea>");
    
    
    text = text.replaceAll("\\[screenshot\\](.*?)\\[/screenshot\\]", "<a class=\"screenshot-link\" href=\"../report/screenshot$1\" target=\"_blank\"><img class=\"screenshot\" src=\"../display/file$1\"/></a>");
    
    out.println(text);
}
%>