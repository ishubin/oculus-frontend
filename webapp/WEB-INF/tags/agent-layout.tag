<%@ tag body-content="scriptless" %>
<%@ attribute name="agent" required="true" type="net.mindengine.oculus.grid.domain.agent.AgentStatus" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<div class="grid-agent-layout">
   	<table class="agent-table" width="100%" border="0" cellspacing="0">
	    <tr>
	        <td class="icon" rowspan="2">
	            <img src="../images/trm-agent-computer.png"/>
	            <br/>
	            <c:if test="${agent.state==1}">
	               <span style="color:green;">Free</span>
	            </c:if>
	            <c:if test="${agent.state==2}">
                   <span style="color:blue;">Busy</span>
                </c:if>
	        </td>
	        <td class="title">
	        	<a href="#" class="agent-tag agent-click" agent-name="<tag:escape text="${agent.agentInformation.name}"/>">
	        		<img src="../images/workflow-icon-monitor.png"/>
	        		<span><tag:escape text="${agent.agentInformation.name}"/></span>
	        	</a>
	        </td>
	    </tr>
	    <tr>
	       <td class="description">
	           <b>HostName:</b> <tag:escape text="${agent.agentInformation.host}"></tag:escape>
	           <br/>
	           <b>Description:</b><br/>
	           <tag:escape text="${agent.agentInformation.description}" nl2br="true"></tag:escape>
	       </td>
	    </tr>
	    <c:if test="${agent.agentInformation.tags!=null}">
	    <tr>
	    	<td colspan="2">
	    		<c:forEach items="${agent.agentInformation.tags }" var="tag">
	    			<p>
	    			<b><tag:escape text="${tag.name}"/>: </b>
	    			<c:choose>
	    				<c:when test="${tag.type=='string'}">
	    					<a href="#" class="agent-tag agent-tag-click" agent-tag="${tag.wrappedValue.value}">
	    						<c:if test="${tag.wrappedValue.iconImage!=null}"><img src="${tag.wrappedValue.iconImage}" <c:if test="${tag.wrappedValue.iconSize!=null}">width="${tag.wrappedValue.iconSize.width}px" height="${tag.wrappedValue.iconSize.height}px"</c:if> /></c:if>
	    						<span><tag:escape text="${tag.wrappedValue.value}"/></span>
	    					</a>
	    				</c:when>
	    				<c:when test="${tag.type=='list'}">
	    					<c:forEach items="${tag.wrappedValues}" var="tagItem" varStatus="tagItemStatus">
	    						<a href="#" class="agent-tag agent-tag-click" agent-tag="${tagItem.value}">
		    						<c:if test="${tagItem.iconImage!=null}"><img src="${tagItem.iconImage}" <c:if test="${tagItem.iconSize!=null}">width="${tagItem.iconSize.width}px" height="${tagItem.iconSize.height}px"</c:if> /></c:if>
		    						<span><tag:escape text="${tagItem.value}"/></span>
	    						</a>
	    					</c:forEach>
	    				</c:when>
	    			</c:choose>
	    			</p>
	    		</c:forEach>
	    	</td>
	    </tr>
	    </c:if>
	</table>
</div>
