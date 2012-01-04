<%@ tag body-content="scriptless" %>
<%@ attribute name="node" required="true"  type="net.mind_engine.oculus.testrunframework.reporter.nodes.ReportNode"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>
<div id="reportNode${node.id}"
    <c:if test="${node.metaData.hasError==true || node.type == 'error'}">style="background:#ff9999;"</c:if>
    >
	<table class="report-node-table" border="0" cellpadding="0px" cellspacing="0px" width="100%">
	    <tr>
	        <td width="20px" rowspan="2">
	            <c:if test="${node.hasChildren == true && node.metaData.isSimple == false}">
	                 <a class="collapse" href="javascript:onReportNodeClick(${node.id});">
	                     <img id="imgCollapse${node.id}" src="../images/report-expand-button.png"/>
	                 </a>
	            </c:if>
	        </td>
	        
	        <td width="1px">
	           <div id="reportNodeIcon${node.id}">
		           <c:if test="${node.hasChildren==true}"><a class="collapse" href="javascript:onReportNodeClick(${node.id});"></c:if>
			           <c:choose>
			               <c:when test="${node.logo.logo!=null}">
			                   <img src="../images/report-logo-${node.logo.logo}.png"/>
			               </c:when>
			               <c:otherwise>
			                   <img src="../images/report-logo-simple.png"/>
			               </c:otherwise>
			           </c:choose>
			       <c:if test="${node.hasChildren==true}"></a></c:if>
		       </div>
	        </td>
	        <td style="padding:4px;" valign="top" align="left">
	           <c:choose>
	               <c:when test="${node.class.simpleName == 'ComponentReportNode'}">
                       <span class="component-name">${node.name}</span>
                       <c:if test="${node.text!=null}">
                           <span class="component-description">
                               (<tag:report-node-format text="${node.text}"/>)
                           </span>
                       </c:if>
                   </c:when>
                   <c:when test="${node.class.simpleName == 'ExceptionReportNode'}">
                       <span class="exception-name">${node.name}: </span>
                       <span class="exception-text">${node.text}</span>
                   </c:when>
                   <c:otherwise>
                       <tag:report-node-format text="${node.text}"/>
                   </c:otherwise>
	           </c:choose>
	        </td>
	        <td style="padding:4px;white-space: nowrap;font-size:8pt;width:142px;" width="142px">
	           <tag:date date="${node.time}"></tag:date>
	        </td>
	        <td width="42px" style="padding:4px;white-space: nowrap;width:42px;font-size:8pt;">
	           <c:if test="${node.metaData.step!=null && node.metaData.step>0}">
	               ${node.metaData.step}
	           </c:if>
            </td>
            <td width="92px" style="padding:4px;white-space: nowrap;font-size:8pt;width:92px;">
                <div id="reportNodeStatus${node.id}">
                    <c:choose>
	                    <c:when test="${node.type == 'error' || node.metaData.hasError==true}">
	                        <div style="color:#ff0000;">Failed<img src="../images/report-status-logo-error.png"/></div>
	                    </c:when>
	                    <c:when test="${node.type == 'warn' || node.metaData.hasWarn==true}">
	                        <div style="color:#FCB100;">Warn<img src="../images/report-status-logo-warn.png"/></div>
	                    </c:when>
                    </c:choose>
                </div>
            </td>
	    </tr>
	    <c:if test="${node.hasChildren == true}">
	        <tr>
	            <td colspan="5">
	                <div id="childrenFor${node.id}" style="display:none;">
	                    <c:forEach items="${node.children}" var="childNode" varStatus="childNodeVarStatus">
	                        <c:choose>
	                           <c:when test="${childNode.class.simpleName == 'DescriptionReportNode'}">
	                               <tag:report-node-format text="${childNode.text }"/>
	                           </c:when>
	                           <c:otherwise>
	                               <div class="separator"></div>
                                   <tag:report-node node="${childNode}"></tag:report-node>
	                           </c:otherwise>
	                        </c:choose>
	                    </c:forEach>
	                </div>
	            </td>
	        </tr>
	    </c:if>
	</table>
</div>
