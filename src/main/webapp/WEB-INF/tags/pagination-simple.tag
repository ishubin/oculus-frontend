<%@ tag body-content="scriptless" %>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="numberOfResults" required="true" type="java.lang.Integer"%>
<%@ attribute name="pageLimit" required="true" type="java.lang.Integer"%>
<%@ attribute name="pageUrlParameter" required="true" type="java.lang.String"%>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<%
//current offset

int c = currentPage;

int maxd = pageLimit; 
//calculating maximum number of pages
int n = (int)(numberOfResults/maxd);
if(numberOfResults % maxd > 0) n++;

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

java.util.List  pageList = new java.util.ArrayList();
java.util.Map m = new java.util.HashMap();
m.put("name","&lt;&lt;");
m.put("id",1);
m.put("current",false);
pageList.add(m);

for(int i = p1; i<=p2; i++)
{
    m = new java.util.HashMap();
    m.put("name",i);
    m.put("id",i);
    if(i==c)
    {
       m.put("current",true);
    }
    else m.put("current",false);
    
    pageList.add(m);
}

m = new java.util.HashMap();
m.put("name","&gt;&gt;");
m.put("id",n);
m.put("current",false);
pageList.add(m);

jspContext.setAttribute("pageList", pageList);

if(numberOfResults>pageLimit)
{
    jspContext.setAttribute("displayPagination", true);
}

String urlSeparator = "?";
if(url.contains("?"))
{
    urlSeparator = "&";
}

jspContext.setAttribute("urlSeparator", urlSeparator);

%>
<c:if test="${displayPagination == true}">
    <table class="pagination">
        <tr>
            <c:forEach items="${pageList}" var="p">
                <td>
                    <c:choose>
                        <c:when test="${p.current==true}">
                            <b>${p.name}</b>
                        </c:when>
                        <c:otherwise>
                           <a href="${url}${urlSeparator}${pageUrlParameter}=${p.id}">${p.name}</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </c:forEach>
        </tr>
    </table>
</c:if>
