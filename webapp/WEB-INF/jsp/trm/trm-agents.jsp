<%@ include file="/include.jsp" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>


<div class="breadcrump">
    Agents
</div>

<c:forEach items="${agents}" var="agent">
    <div class="project-layout">
    	<table align="center" class="agent-table" width="100%" border="0" cellspacing="0">
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
		        <td class="title"><tag:escape text="${agent.agentInformation.name}"></tag:escape></td>
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
		    					<c:if test="${tag.wrappedValue.iconImage!=null}"><img src="${tag.wrappedValue.iconImage}"/></c:if>
		    					<tag:escape text="${tag.wrappedValue.value}"/>
		    				</c:when>
		    				<c:when test="${tag.type=='list'}">
		    					<c:forEach items="${tag.wrappedValues}" var="tagItem" varStatus="tagItemStatus">
		    						<c:if test="${!tagItemStatus.first }">, </c:if>
		    						<c:if test="${tagItem.iconImage!=null}"><img src="${tagItem.iconImage}"/></c:if>
		    						<tag:escape text="${tagItem.value}"/>
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
</c:forEach>