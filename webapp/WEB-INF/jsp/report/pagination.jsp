<jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.SearchFilter"/>
<jsp:directive.page import="net.mindengine.oculus.frontend.domain.report.TestRunSearchResult"/>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.HashMap"/>
<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<script language="javascript">

function onPage(nPage)
{
    document.forms.browseFilter.pageOffset.value = nPage;
    redirectBrowseFilterForm(document.forms.browseFilter);
}

</script>
<%

SearchFilter filter = (SearchFilter)pageContext.findAttribute("reportSearchFilter");
TestRunSearchResult searchResult = (TestRunSearchResult)pageContext.findAttribute("searchResult");

if(filter!=null)
{
    //current offset
    int c = filter.getPageOffset();
    
    //maximum pagination. can be only 10 pages
    int maxd = 10; 
    
    //calculating maximum number of pages
    int n = (int)(searchResult.getNumberOfResults()/maxd);
    if(searchResult.getNumberOfResults()%maxd > 0) n++;
    
    int d = Math.min(n,maxd)/2;
    
    int p1 = c-d;
    int p2 = c+d;
    
    int delta1=0;
    int delta2=0;
    if(p1<1)
    {
        p1 = 1;
        delta2 = 1-p1;
    }
    if(p2>n)
    {
        p2 = n;
        delta1 = p2-n;
    }
    
    if((delta2+p2)>n)
    {
        p2 = n;
    }
    else p2+=delta2;
    
    if((p1-delta1)<1)
    {
        p1 = 1;
    }
    else p1-=delta1;
    
    List  pageList = new ArrayList();
    Map m = new HashMap();
    m.put("name","&lt;&lt;");
    m.put("id",1);
    m.put("current",false);
    pageList.add(m);
    
    for(int i = p1; i<=p2; i++)
    {
        m = new HashMap();
	    m.put("name",i);
	    m.put("id",i);
	    if(i==c)
	    {
	       m.put("current",true);
	    }
	    else m.put("current",false);
	    
	    pageList.add(m);
    }
    
    m = new HashMap();
    m.put("name","&gt;&gt;");
    m.put("id",n);
    m.put("current",false);
    pageList.add(m);
    
    pageContext.setAttribute("pageList", pageList);
}
%>
<c:if test="${searchResult.numberOfResults > reportSearchFilter.convertedPageLimit}">
	<table class="pagination">
	    <tr>
	        <c:forEach items="${pageList}" var="p">
	            <td>
	                <c:choose>
		                <c:when test="${p.current==true}">
		                    <b>${p.name}</b>
		                </c:when>
		                <c:otherwise>
		                   <a href="javascript:onPage(${p.id});">${p.name}</a>
		                </c:otherwise>
	                </c:choose>
	            </td>
	        </c:forEach>
	    </tr>
	</table>
</c:if>