<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="appsdetails" class="eu.dapaas.bean.ApplicationAlexBean"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}" />
<c:set var="applicationDetail" value="${appsdetails.getApplicationDetails(pageContext.request, pageContext.response, pageContext.session)}" />
<c:if test="${applicationDetail.publisher ==  user}">
	<c:set var="editable" value="editable"></c:set>
</c:if>

<template:genericpage title="DaPaaS Developer portal">
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
		<div class="dataset-wrapper">
			<h2 class="table-label">Application</h2>
			<template:application_details templateData="${applicationDetail}" editable="${editable}"/>
		</div>
	</jsp:body>
</template:genericpage>