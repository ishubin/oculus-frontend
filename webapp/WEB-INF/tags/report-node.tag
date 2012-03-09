<%@ tag body-content="scriptless" %>
<%@ attribute name="node" required="true"  type="net.mindengine.oculus.experior.reporter.nodes.ReportNode"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div id="reportNode${node.id}"
    <c:if test="${node.metaData.hasError==true || node.level == 'error'}">style="background:#ff9999;"</c:if>
    >
	<table class="report-node-table" border="0" cellpadding="0px" cellspacing="0px" width="100%">
	    <tr>
	        <td width="20px" rowspan="2">
	            <c:if test="${node.metaData.hasChildren == true && node.metaData.isSimple == false}">
	                 <a class="collapse" href="javascript:onReportNodeClick(${node.id});">
	                     <img id="imgCollapse${node.id}" src="../images/report-expand-button.png"/>
	                 </a>
	            </c:if>
	        </td>
	        
	        <td width="1px">
	           <div id="reportNodeIcon${node.id}">
		           <c:if test="${ node.metaData.hasChildren == true }"><a class="collapse" href="javascript:onReportNodeClick(${node.id});"></c:if>
			           <c:choose>
			               <c:when test="${ node.icon != null && node.icon != '' }">
			                   <img src="../images/report-logo-${node.icon}.png"/>
			               </c:when>
			               <c:otherwise>
			                   <img src="../images/report-logo-simple.png"/>
			               </c:otherwise>
			           </c:choose>
			       <c:if test="${ node.metaData.hasChildren == true }"></a></c:if>
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
                   </c:otherwise>
	           </c:choose>
	        </td>
	        <td style="padding:4px;white-space: nowrap;font-size:8pt;width:142px;" width="142px">
	           <c:if test="${ node.date != null }"><tag:date date="${node.date}"></tag:date></c:if>
	        </td>
	        <td width="42px" style="padding:4px;white-space: nowrap;width:42px;font-size:8pt;">
	           <c:if test="${node.metaData.step!=null && node.metaData.step>0}">
	               ${node.metaData.step}
	           </c:if>
            </td>
            <td width="92px" style="padding:4px;white-space: nowrap;font-size:8pt;width:92px;">
                <div id="reportNodeStatus${node.id}">
                    <c:choose>
	                    <c:when test="${node.level == 'error' || node.metaData.hasError==true}">
	                        <div style="color:#ff0000;">Failed<img src="../images/report-status-logo-error.png"/></div>
	                    </c:when>
	                    <c:when test="${node.level == 'warn' || node.metaData.hasWarn==true}">
	                        <div style="color:#FCB100;">Warn<img src="../images/report-status-logo-warn.png"/></div>
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
	                		<c:when test="${ node.metaData.type == 'branch' }">
	                			<c:forEach items="${node.childNodes}" var="childNode" varStatus="childNodeVarStatus">
	                				<div class="separator"></div>
	                				<tag:report-node node="${childNode}"></tag:report-node>
		                        	    
		                        </c:forEach>
	                		</c:when>
	                		<c:when test="${ node.metaData.type == 'text' }">
	                			<tag:report-node-format text="${node.details }"/>
	                		</c:when>
	                		<c:when test="${ node.metaData.type == 'exception' }">
	                			<tag:exception-stacktrace exception="${node.exception}"></tag:exception-stacktrace>
	                		</c:when>
	                	</c:choose>
	                </div>
	            </td>
	        </tr>
	    </c:if>
	</table>
</div>
