<%@tag import="java.util.HashMap"%>
<%@tag import="java.util.LinkedList"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.List"%>
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
if(pageLimit>=pageLimitArray.length) pageLimit = pageLimitArray.length-1;
if(pageLimit<0)pageLimit = 0;
int spp = pageLimitArray[pageLimit]; //show per page;

int pages = numberOfResults/spp + 1; //total pages according to number of results and show per page information;
//if(numberOfResults % spp >0)pages++;


if (pages>1) {
	int startPage = Math.max(currentPage - 4, 1);
	int endPage = Math.min(currentPage + 4, pages);


	List<Map<String, Object>> list = new LinkedList<Map<String, Object>> ();
	
	if ( currentPage > 1) {
		Map<String, Object > page = new HashMap<String, Object>();
		page.put("name", "&lt;&lt;");
		page.put("id", 1);
		page.put("current", false);
		list.add(page);
	}
	
	for( int i=startPage; i<= endPage; i++) {
		Map<String, Object > page = new HashMap<String, Object>();
		page.put("name", i);
		page.put("id", i);
		if ( i==currentPage) {
			page.put("current", true);
		}
		else {
			page.put("current", false);
		}
		list.add(page);
	}
	
	if ( currentPage < pages) {
		Map<String, Object > page = new HashMap<String, Object>();
		page.put("name", "&gt;&gt;");
		page.put("id", pages);
		page.put("current", false);
		list.add(page);
	}
	
	jspContext.setAttribute("pageList", list);
	
	jspContext.setAttribute("displayPagination", true);
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
    <div class="pagination">
        <ul>
            <c:forEach items="${pageList}" var="p">
                <li>
                    <c:choose>
                        <c:when test="${p.current==true}">
                            <span>${p.name}</span>
                        </c:when>
                        <c:otherwise>
                           <a href="javascript:${onPageScript}(${p.id});">${p.name}</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>
<br/>Page limit 
<select id="paginationPageLimit" onchange="${onPageLimitScript}(this);">
    <c:forEach items="${pageLimitOptions}" var="option">
        <option value="${option.id}" <c:if test="${option.selected == true}">selected="true"</c:if>>${option.label}</option>
    </c:forEach>
</select>
