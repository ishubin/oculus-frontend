<jsp:directive.page import="net.mindengine.oculus.experior.reporter.nodes.ReportNode"/>
<%@ include file="/include.jsp" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div style="${node.debug==true?'display:none':''}"  class="report-node-layout" x-debug="${node.debug}">
<div class="separator"></div>
<div id="reportNode${node.id}" class="report-node ${(node.level=='error')?'report-error report-shadow':''} ${(node.level=='warn')?'report-warn report-shadow':''}"
    
    >
    
	<table class="report-node-table" border="0" cellpadding="0px" cellspacing="0px" width="100%">
	    <tr>
	        <td width="20px" rowspan="2">
	            <c:if test="${node.metaData.hasChildren == true || node.metaData.isSimple == false}">
	                 <a class="collapse" href="javascript:onReportNodeClick(${node.id});">
	                     <img id="imgCollapse${node.id}" src="../images/report-expand-button.png"/>
	                 </a>
	            </c:if>
	        </td>
	        
	        <td width="1px">
	           <div id="reportNodeIcon${node.id}">
		           <c:if test="${ node.metaData.hasChildren == true || node.metaData.isSimple == false }"><a class="collapse" href="javascript:onReportNodeClick(${node.id});"></c:if>
			           <c:choose>
			               <c:when test="${ node.icon != null && node.icon != '' }">
			                   <img src="../images/report-logo-${node.icon}.png"/>
			               </c:when>
			               <c:otherwise>
			                   <img src="../images/report-logo-simple.png"/>
			               </c:otherwise>
			           </c:choose>
			       <c:if test="${ node.metaData.hasChildren == true || node.metaData.isSimple == false}"></a></c:if>
		       </div>
	        </td>
	        <td style="padding:4px;" valign="top" align="left">
	           <c:choose>
	               <c:when test="${node.metaData.type == 'exception'}">
                       <span class="exception-name"><tag:escape text="${node.exception.className}"/>: </span>
                       <span class="exception-text"><tag:escape text="${node.exception.messageName}"/></span>
                   </c:when>
                   <c:otherwise>
                       <tag:report-node-format text="${node.title}"/>
                       <c:if test="${node.hint!=null}"><span class="hint"><tag:escape text="${node.hint}"/></span></c:if>
                   </c:otherwise>
	           </c:choose>
	        </td>
	        <td width="142px" class="table-cell-time">
	           <c:if test="${ node.date != null }"><tag:date date="${node.date}"></tag:date></c:if>
	        </td>
	        <td width="42px" class="table-cell-step">
	           <c:if test="${node.metaData.step!=null && node.metaData.step>0}">
	               <span>${node.metaData.step}</span>
	           </c:if>
            </td>
            <td width="92px" class="table-cell-status">
                <div id="reportNodeStatus${node.id}">
                    <c:choose>
	                    <c:when test="${node.level == 'error' || node.metaData.hasError==true}">
	                        <div style="color:#ff0000;">Failed <img src="../images/filter-failed.png"/></div>
	                    </c:when>
	                    <c:when test="${node.level == 'warn' || node.metaData.hasWarn==true}">
	                        <div style="color:#ff0000;">Warn <img src="../images/filter-warning.png"/></div>
	                    </c:when>
                    </c:choose>
                </div>
            </td>
	    </tr>
	    <c:if test="${node.metaData.isSimple == false }">
	        <tr>
	            <td colspan="5">
	                <div id="childrenFor${node.id}" style="display:none;">
	                	<c:choose>
	                		<c:when test="${ node.metaData.type == 'text' }">
	                			<tag:report-node-format text="${node.details }"/>
	                		</c:when>
	                		<c:when test="${ node.metaData.type == 'exception' }">
	                			<c:set var="exception" value="${node.exception}" scope="request"/>
						        <jsp:include page="/WEB-INF/jsp/report/exception-render.jsp"></jsp:include>
	                		</c:when>
	                		<c:when test="${ node.metaData.type == 'branch' }">
	                			<c:forEach items="${node.childNodes}" var="childNode">
						        	<c:set var="node" value="${childNode}" scope="request"/>
						        	<jsp:include page="/WEB-INF/jsp/report/report-node.jsp"></jsp:include>
						        </c:forEach>
	                		</c:when>
	                	</c:choose>
	                </div>
	            </td>
	        </tr>
	    </c:if>
	</table>
</div>
</div>