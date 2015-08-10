<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="transformationbean" class="eu.dapaas.bean.TransformationBean"/>

<jsp:setProperty name="transformationbean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="transformationbean" property="session" value="${pageContext.session}"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<c:set var="user" value="${sessionScope.dapaas_user}" />

<c:set var="transformation" value="${transformationbean.getDetail(user, param['id']) }" />
<jsp:setProperty name="wizard" property="transformation" value="${transformation}"/>


<c:if test="${param['action'] == 'export' }">
	${transformationbean.executeAndDownload()}
</c:if>



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
	<form id="formexport" method="post">
		<input type="hidden" name="action" id="action" />
	</form>
	<ul class="breadcrumb">
    			<li><a class="theme-text" href="${contextPath }/pages/catalogs">Explore</a></li>
    			<li><a class="theme-text" href="${contextPath }/pages/myassets">Dashboard</a></li>
    			<li>Transformation page</li>
    			
			</ul>
			<h1 class="add-label"> Execute transformation '${transformation.title}'</h1>
			
			<c:if test="${empty wizard.uploadesFile.file}">
				${ wizard.uploadesFile.setFiletype("GRF")}
				${ wizard.uploadesFile.setContentType(param['id'])}
				
				<blockquote>
					${transformation.description}
				</blockquote>
				
				<div id="tr_dropzone_dataset" class="dropzone-dataset well up">
				</div>
			</c:if>
			
			<c:if test="${not empty wizard.uploadesFile.file}">
				
				<blockquote>
					Transformation Description: ${transformation.description}
				</blockquote>
				<blockquote>
					Transformation Type: ${transformation.transformationType}
				</blockquote>
			</c:if>
<c:if test="${wizard.action =='new' }" >			
	<div id="content" class="container ${not empty wizard.uploadesFile.file? 'up': 'down'}" >
		<div class="row">
			<div class="col-xs-4 col-md-6">  <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button ${ (transformation.transformationType == 'pipe') ? 'disabled' : '' }" id="execsrepository" href="#">Continue</a> </div>
			<div class="col-xs-4 col-md-6"> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="execdlresult" href="#"> Download results </a> </div>
		
		</div>
	</div>
</c:if>

<c:if test="${wizard.action =='edit' }" >			
	<div id="content" class="container up" >
		<div class="row">
			<div class="col-xs-4 col-md-6">  <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button ${ (transformation.transformationType == 'pipe') ? 'disabled' : '' }" id="updaterepository" href="#">Continue</a> </div>
			<div class="col-xs-4 col-md-6"> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="execdlresult" href="#"> Download results </a> </div>
		
		</div>
	</div>
</c:if>
	</jsp:body>
</template:genericpage>