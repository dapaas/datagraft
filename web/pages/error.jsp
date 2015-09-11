<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="error" value="${sessionScope.error }" />
<template:genericpage title="DataGraft Public Portal | Error">
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
		
		<div class="panel panel-danger">
		    <div class="panel-heading">
		        <h3 class="panel-title">Error</h3>
		    </div>
		    <div class="panel-body">
		       ${error}
		       <div>
		       	<input type="button" value="Explore public catalog" class="btn btn-danger btn-raised" onclick="location.href='${contextPath}/pages/catalogs'">
		       </div>
		    </div>
		</div>
		<c:remove var="error" scope="session" />
	</jsp:body>
	   
</template:genericpage>