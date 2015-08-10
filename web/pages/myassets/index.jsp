<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="datasetBean" class="eu.dapaas.bean.DatasetBean"/>
<jsp:useBean id="transformationBean" class="eu.dapaas.bean.TransformationBean"/>
<jsp:setProperty name="datasetBean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="datasetBean" property="session" value="${pageContext.session}"/>
<jsp:setProperty name="transformationBean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="transformationBean" property="session" value="${pageContext.session}"/>

${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<%

	String mydatapagesearch = request.getParameter("mydatapagesearch");
	if (mydatapagesearch != null && mydatapagesearch.length()>0){
	  datasetBean.setSearchValue(mydatapagesearch);
	}

	String mytransformationsearch = request.getParameter("mytransformationsearch");
	if (mytransformationsearch != null && mytransformationsearch.length()>0){
	  transformationBean.setSearchValue(mytransformationsearch);
	}


%>
<c:set var="user" value="${sessionScope.dapaas_user }" />
<c:remove var="wizard"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<template:validation/>
<template:genericpage title="DataGraft portal">
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

<c:set var="delete" value="${param['delete'] }" />
<c:if test="${delete == 'dataset'}">
	${datasetBean.catalogDelete(user.apiKey, user.apiSecret, param['id'])}
</c:if>

<c:if test="${delete == 'transformation'}">
	${transformationBean.delete(user, param['id'])}
</c:if>

<form id="deletedataset" method="post">
	<input type="hidden" id="delete" name="delete" />
	<input type="hidden" id="id" name="id"/>
</form>

<ul class="breadcrumb">
    <li><a class="theme-text" href="${contextPath }/pages/catalogs">Explore</a></li>
    <li class="active">Dashboard</li>
</ul> 
		<div class="dataset-wrapper">
			
			<c:set var="titleDataset" value="My data pages"/>
			<c:set var="emptylabel" value="No user data pages found. <a class='theme-text'  href='${contextPath }/pages/publish'>Create your first data page?</a> " />
			<c:if test="${ not empty param['mydatapagesearch']}">
			<c:set var="titleDataset" value="My data pages (filtered by &quot;${param['mydatapagesearch']}&quot;)" />
			<c:set var="emptylabel" value="No matching data pages found. <a class='theme-text' href='${contextPath }/pages/myassets'>Show all?</a> " />
			</c:if>
			<div class="col-lg-6 col-md-6">
				<h2>${titleDataset }</h2>
				<!-- search data page -->
				<form id="formmydatapagesearch" method="post" action="${contextPath }/pages/myassets/index.jsp">
				   <div class="form-group">
				   <div class="input-group">
				        <input type="text" id="mydatapagesearch" name="mydatapagesearch" class="form-control col-lg-8" placeholder="Search">
				    </div> 
				    </div>
				</form>
				<template:dataset_catalog id="mycatalogresult" templateData="${datasetBean.getCatalogDataset(user.apiKey, user.apiSecret)}" action="${true }" emptylabel="${emptylabel }"/>
			</div>
			<c:set var="titleTransformation" value="My transformations"/>
			<c:set var="emptylabel1" value="No user transformations found. <a class='theme-text'  href='${contextPath }/pages/transformations'>Create your first transformation?</a> " />
			<c:if test="${ not empty param['mytransformationsearch']}">
			<c:set var="titleTransformation" value="My transformations (filtered by &quot;${param['mytransformationsearch']}&quot;)"/>
			<c:set var="emptylabel1" value="No matching transformations found. <a class='theme-text' href='${contextPath }/pages/myassets'>Show all?</a> " />
			</c:if>
			<div class="col-lg-6 col-md-6">
				<h2>${titleTransformation }</h2>
				<!-- search transformation -->
				<form id="formmytransformationsearch" method="post" action="${contextPath }/pages/myassets/index.jsp">
				   <div class="form-group">
				   <div class="input-group">
				        <input type="text" id="mytransformationsearch" name="mytransformationsearch" class="form-control col-lg-8" placeholder="Search">
				    </div> 
				    </div>
				</form>
				<template:transformation_catalog id="mytransformationresult" templateData="${transformationBean.getCatalogTransformations(user)}" emptylabel="${emptylabel1 }"/>
			</div>
		</div>
	

		
		<div id="dialog-confirm" title="Delete?"></div>
	</jsp:body>
</template:genericpage>