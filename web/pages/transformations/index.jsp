<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page import="eu.dapaas.utils.Utils"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="transformationId" value='<%= Utils.escapeJS(request.getParameter("id")) %>' />
<c:set var="distributionId" value='<%= Utils.escapeJS(request.getParameter("distribution")) %>' />
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
			.go('transformations');
	});
	</script>
	</c:if>
	<c:if test="${not empty param['id'] }">
                <c:if test="${empty distributionId }">
                <script async>
                $(window).load(function() {
                        var graftInstance = new Grafterizer("https://grafterizer.datagraft.net", document.body)
                                .setAuthorization("${basicAuth}")
                                .go('transformations.transformation', {
                                        id: encodeURI('${transformationId}')
                                });
                });
                </script>
                </c:if>
                <c:if test="${not empty distributionId }">
                <script async>
                $(window).load(function() {
                        var graftInstance = new Grafterizer("https://grafterizer.datagraft.net", document.body)
                                .setAuthorization("${basicAuth}")
                                .go('transformations.transformation.preview', {
                                        id: encodeURI('${transformationId}'),
                                        distribution: '${distributionId}'
                                });
                });
                </script>
                </c:if>
        </c:if>
	<link rel='stylesheet' type='text/css' href="${contextPath}/css/grafterizer.css"/>
	<script type="text/javascript" src="${contextPath}/scripts/grafterizerPostMessage.js" async></script>
	</jsp:body>
</template:genericpage>
