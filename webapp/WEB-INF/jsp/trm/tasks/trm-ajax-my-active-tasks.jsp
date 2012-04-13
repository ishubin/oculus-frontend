<?xml version='1.0' encoding='utf-8'?><%@ include file="/include.jsp" %><%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %><%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<rows>
    <page>1</page>
    <total>1</total>
    <records>${records}</records>
    <c:forEach items="${rows}" var="r">
	    <row>
	        <cell>${r.id}</cell>
	        <cell>${fn:escapeXml(r.name)}</cell>
	        <cell><c:if test="${r.created!=null}"><tag:date date="${r.created}"></tag:date></c:if></cell>
	        <cell><c:if test="${r.completed!=null}"><tag:date date="${r.completed}"></tag:date></c:if></cell>
	        <cell>${r.percents}</cell>
	        <cell>${r.status}</cell>
	        <cell>${fn:escapeXml(r.message)}</cell>
	        <cell>${r.report}</cell>
	        <cell></cell>
	        <cell>${r.level}</cell>
	        <cell>${r.parentId}</cell>
	        <cell>${r.hasChildren ?'false':'true' }</cell>
	        <cell>false</cell>
	    </row>
    </c:forEach>
</rows>