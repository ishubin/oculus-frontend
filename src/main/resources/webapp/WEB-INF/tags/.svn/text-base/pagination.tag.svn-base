<%@ tag body-content="scriptless" %>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="numberOfResults" required="true" type="java.lang.Integer"%>
<%@ attribute name="pageLimit" required="true" type="java.lang.Integer"%>
<%@ attribute name="onPageScript" required="true" %>
<%@ attribute name="onPageLimitScript" required="true" %>
<%@ attribute name="pageLimitArray" required="true" type="java.lang.Integer[]"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<%
int c = currentPage;
if(pageLimit>=pageLimitArray.length) pageLimit = pageLimitArray.length-1;
if(pageLimit<0)pageLimit = 0;

int spp = pageLimitArray[pageLimit]; //show per page;

int tp = numberOfResults/spp; //total pages according to number of results and show per page information;
if(numberOfResults % spp >0)tp++;


if(c>tp)c = tp;


int maxPL = c;//maximum number of pages to left side;
int maxPR = tp-c;//maximum number of pages to right side;

//Range of the pagination
int p1 = c - Math.min(4, maxPL);
int p2 = c + Math.min(4, maxPR);
if(p1==0)p1 = 1;

java.util.List<Object>  pageList = new java.util.ArrayList<Object>();
java.util.Map<Object,Object>  m;
if(c>1){
	m = new java.util.HashMap<Object,Object>();
	m.put("name","&lt;&lt;");
	m.put("id",1);
	m.put("current",false);
	pageList.add(m);
}

for(int i = p1; i<=p2; i++)
{
    m = new java.util.HashMap<Object,Object>();
    m.put("name",i);
    m.put("id",i);
    if(i==c)
    {
       m.put("current",true);
    }
    else m.put("current",false);
    
    pageList.add(m);
}

if(c<tp){
	m = new java.util.HashMap<Object,Object>();
	m.put("name","&gt;&gt;");
	m.put("id",tp);
	m.put("current",false);
	pageList.add(m);
}

jspContext.setAttribute("pageList", pageList);

if(pageLimit<pageLimitArray.length)
{
	if(numberOfResults > pageLimitArray[pageLimit])
	{
		jspContext.setAttribute("displayPagination", true);
	}
}


//Preparing the pageLimit model
java.util.List<Object> list = new java.util.ArrayList<Object>();
    
for(int i=0;i<pageLimitArray.length;i++)
{
	java.util.Map<Object,Object> map = new java.util.HashMap<Object,Object>();
    map.put("label",pageLimitArray[i]);
    map.put("id",i);
    if(pageLimit.equals(i))
    {
        map.put("selected",true);
    }
    else map.put("selected",false);
    list.add(map);
}

jspContext.setAttribute("pageLimitOptions",list);

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
                           <a href="javascript:${onPageScript}(${p.id});">${p.name}</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </c:forEach>
        </tr>
    </table>
</c:if>
<br/>Page limit 
<select id="paginationPageLimit" onchange="${onPageLimitScript}(this);">
    <c:forEach items="${pageLimitOptions}" var="option">
        <option value="${option.id}" <c:if test="${option.selected == true}">selected="true"</c:if>>${option.label}</option>
    </c:forEach>
</select>
