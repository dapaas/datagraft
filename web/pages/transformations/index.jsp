<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }

<template:validation/>
<template:genericpage title="DataGraft Public Portal">

<jsp:attribute name="navbar">
	<template:menu />
</jsp:attribute>
	<jsp:attribute name="footer">
		<template:footer />
	</jsp:attribute>
	<jsp:attribute name="search">
		<template:search />
	</jsp:attribute>
	<jsp:body>
	<c:set var="basicAuth" value="${userbean.getTemporaryBasicAuth(pageContext.request, pageContext.response,pageContext.session, 'grafterizer') }"/>
	<!-- id=${data.id} -->
	<c:if test="${empty param['id'] }">
	<script async>
	$(window).load(function() {
		var graftInstance = new Grafterizer("https://grafterizer.datagraft.net", document.body)
			.setAuthorization("${basicAuth}")
			.go('transformations.new');
	});
	</script>
	</c:if>
	<c:if test="${not empty param['id'] }">
	<iframe src="http://ec2-54-154-72-62.eu-west-1.compute.amazonaws.com/#/transformations/${param['id'] }" style="width: 100%; height: 900px; border:0;">
	</c:if>
	</jsp:body>
<link rel='stylesheet' type='text/css' href="${contextPath}/css/grafterizer.css"/>
<script type="text/javascript" src="${contextPath}/scripts/grafterizerPostMessage.js" async></script>
</template:genericpage>