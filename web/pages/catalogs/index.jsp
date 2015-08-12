<%@page import="eu.dapaas.utils.Utils"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<jsp:useBean id="datasetBean" class="eu.dapaas.bean.DatasetAllBean" />
<jsp:setProperty name="datasetBean" property="response" value="${pageContext.response}" />
<jsp:setProperty name="datasetBean" property="session" value="${pageContext.session}" />

<jsp:useBean id="transformationBean" class="eu.dapaas.bean.TransformationBean" />
<jsp:setProperty name="transformationBean" property="response" value="${pageContext.response}" />
<jsp:setProperty name="transformationBean" property="session" value="${pageContext.session}" />

<%
  String searchValue = request.getParameter("searchvalue");
  if (searchValue != null && searchValue.length() > 0) {
    datasetBean.setSearchValue(searchValue);
    datasetBean.setPageNumber(1);
  }

  String mytransformationsearch = request.getParameter("searchvalue");
  if (mytransformationsearch != null && mytransformationsearch.length() > 0) {
    transformationBean.setSearchValue(mytransformationsearch);
    transformationBean.setPageNumber(1);
  }
  String owner = request.getParameter("owner");
  if (!Utils.isEmpty(owner)) {
    datasetBean.setOwner(owner);
    transformationBean.setOwner(owner);
    transformationBean.setPageNumber(1);
    datasetBean.setPageNumber(1);
  }
  
  String pagetransformationStr = request.getParameter("pagetransformation");
  if (!Utils.isEmpty(pagetransformationStr)) {
  	transformationBean.setPageNumber(new Integer(pagetransformationStr));
  }
  
  String pagedataset = request.getParameter("pagedataset");
  if (!Utils.isEmpty(pagedataset)) {
    datasetBean.setPageNumber(new Integer(pagedataset));
  }
%>

<c:remove var="wizard" />
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

<div class="row">
	<c:set var="owner" value="${param['owner'] }"/>
	<c:set var="titleDataset" value="Latest public data pages" />
	<c:set var="emptylabel" value="No public data pages in the system. <a  class='theme-text' href='${contextPath }/pages/register'>Register</a> and create the first one." />
	<c:if test="${not empty owner }">
		<c:set var="titleDataset" value=" <span style='text-transform: capitalize;'>${owner }'s </span> public data pages" />
		<c:set var="emptylabel" value="No matching data pages found. <a class='theme-text' href='${contextPath }/pages/catalogs'>Show all?</a> " />
	</c:if>
	
	
	
	<c:if test="${ not empty param['searchvalue']}">
	
	<c:set var="titleDataset" value="Data pages (filtered by &quot;${param['searchvalue']}&quot;)" />
	<c:set var="emptylabel" value="No matching data pages found. <a class='theme-text' href='${contextPath }/pages/catalogs'>Show all?</a> " />
	</c:if>
	<form id="selectdataset" action="${contextPath}/pages/publish/preview.jsp" method="get">
		<input type="hidden" id="id" name="id" />
	</form>
	<form id="pagedataset" method="post" action="${contextPath }/pages/catalogs/index.jsp">
		<input type="hidden" id="page" name="pagedataset" />
	</form>
		<div class="col-lg-6 col-md-6">
			<template:latestdatasets id='catalogresult' col1="Data page" col2="User" col3="Published" col4="Portal" emptylabel="${emptylabel}" label="${titleDataset}" templateData="${datasetBean.getCatalogDatasetByPage(datasetBean.getPageNumber())}" sizepage="${datasetBean.getPageCount()}" page="${datasetBean.getPageNumber()}" />
		</div>
		
		
	<c:set var="titleApplication" value="Latest public data transformations" />
	<c:set var="emptylabel" value="No public transformation in the system. <a  class='theme-text' href='${contextPath }/pages/register'>Register</a> and create the first one." />
	<c:if test="${not empty owner }">
		<c:set var="titleApplication" value="<span style='text-transform: capitalize;'>${owner }'s </span> public data transformations" />
		<c:set var="emptylabel" value="No matching transformations found. <a class='theme-text' href='${contextPath }/pages/catalogs'>Show all?</a>" />
	</c:if>
	
	<c:if test="${ not empty param['searchvalue']}">
	<c:set var="titleApplication" value="Transformations (filtered by  &quot;${param['searchvalue']}&quot;)" />
	<c:set var="emptylabel" value="No matching transformations found. <a class='theme-text' href='${contextPath }/pages/catalogs'>Show all?</a>" />
	</c:if>
    <form id="selecttransformation" action="${contextPath}/pages/transformations/preview.jsp" method="get">
		<input type="hidden" id="id" name="id" />
	</form>
	<form id="pagetransformation" method="post" action="${contextPath }/pages/catalogs/index.jsp">
		<input type="hidden" id="page" name="pagetransformation" />
	</form>
	
		<div class="col-lg-6 col-md-6">
			<template:latesttransformation id='transformationresult' col1="Transformation" col2="User" col3="Published" emptylabel="${emptylabel}" label="${titleApplication}" templateData="${transformationBean.getCatalogTransformationsByPage(transformationBean.getPageNumber())}" sizepage="${transformationBean.getPageCount()}" page="${transformationBean.getPageNumber()}"/>
		</div>
</div>
    </jsp:body>
</template:genericpage>