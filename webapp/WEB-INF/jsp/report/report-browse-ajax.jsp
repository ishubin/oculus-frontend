
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="net.mindengine.oculus.frontend.domain.report.ReportSearchColumn"%>
<%@page import="net.mindengine.oculus.frontend.db.search.SearchColumn"%>
<%@page import="net.mindengine.oculus.frontend.domain.report.TestRunSearchResult"%>
<%@page import="net.mindengine.oculus.frontend.domain.report.TestRunSearchData"%><%@page import="java.util.List"%>

<%
boolean comma = false;
TestRunSearchResult result = (TestRunSearchResult)pageContext.findAttribute("searchResult");
List<TestRunSearchData> rows = result.getResults();

%>
{
  "total": 1, 
  "page": 1, 
  "records": <%=rows.size() %>,
  "rows" : [
<%
int i=-1;
for(TestRunSearchData row : rows){
    i++;
    if(comma)out.print(",");
    comma = true;
    out.print("{\"id\": "+i+", \"cell\":[");
    
    out.print(row.getTestRunId());
    out.print(","+row.getTestRunId());
    out.print(",\""+row.getTestRunStatus().toLowerCase()+"\"");
    
    
    String testDescription = row.getTestDescription();
    if(testDescription==null) {
    	testDescription = "";
    }
    
    testDescription = "\""+StringEscapeUtils.escapeJavaScript(testDescription)+"\"";
    if(row.getTestId()!=null && row.getTestId() > 0 && row.getTestName()!=null && !row.getTestName().isEmpty()){
        out.print(",{\"testId\":"+row.getTestId()+", \"testName\":\"" + StringEscapeUtils.escapeJavaScript(row.getTestName()) + "\", \"testDescription\":" +testDescription + "}");
    }
    else out.print(",{\"testId\":null, \"testName\":\"" + StringEscapeUtils.escapeJavaScript(row.getTestRunName()) + "\", \"testDescription\":" +testDescription + "}");
    
    
    if(row.getProjectPath()!=null){
        out.print(",{\"path\":\""+StringEscapeUtils.escapeJavaScript(row.getProjectPath())+"\", \"name\":\"" + StringEscapeUtils.escapeJavaScript(row.getProjectName())+ "\"}");
    }
    else out.print(",null");
    
    if(row.getDesignerLogin()!=null){
        out.print(",{\"login\":\""+StringEscapeUtils.escapeJavaScript(row.getDesignerLogin()) +"\", \"name\":\"" + StringEscapeUtils.escapeJavaScript(row.getDesignerName())+ "\"}");
    }
    else out.print(",null");
    
    if(row.getRunnerLogin()!=null){
        out.print(",{\"login\":\""+StringEscapeUtils.escapeJavaScript(row.getRunnerLogin()) +"\", \"name\":\"" + StringEscapeUtils.escapeJavaScript(row.getRunnerName())+ "\"}");
    }
    else out.print(",null");
    
    
    if(row.getTestRunStartTime()!=null){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        out.print(",\"" + sdf.format(row.getTestRunStartTime())+ "\"");
    }
    else out.print(",null");
    
    
    if(row.getSuiteRunId()!=null && row.getSuiteRunId()>0){
        out.print(",{\"id\":"+row.getSuiteRunId());
        out.print(",\"name\":\""+StringEscapeUtils.escapeJavaScript(row.getSuiteRunName())+"\"}");
    }
    else out.print(",null");
    
    String[] reasons = row.getTestRunReasonsList();
    if(reasons!=null && reasons.length>0){
        out.print(",[");
        for(int j=0;j<reasons.length;j++){
            if(j>0)out.print(",");
            out.print("\""+StringEscapeUtils.escapeJavaScript(reasons[j])+"\"");
        }
        out.print("]");
    }
    else out.print(",null");
    
    if(row.getSuiteRunAgentName()!=null){
        out.print(",\"" + StringEscapeUtils.escapeJavaScript(row.getSuiteRunAgentName())+ "\"");
    }
    else out.print(",null");
    
    if(row.getIssueId()!=null && row.getIssueId()>0){
        out.print(",{\"id\":"+row.getIssueId()+",\"name\":\"" + StringEscapeUtils.escapeJavaScript(row.getIssueName())+ "\"}");
    }
    else out.print(",null");
    
    
    
    out.print("]}\n");
}
%>       
]}