<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<jsp:useBean id="datasetBean" class="eu.dapaas.bean.DatasetAllBean"/>
<jsp:setProperty name="datasetBean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="datasetBean" property="session" value="${pageContext.session}"/>

<%
	String searchValue = request.getParameter("searchvalue");
	if (searchValue != null && searchValue.length()>0){
		datasetBean.setSearchValue(searchValue);
	}
%>

<c:remove var="wizard"/>
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
<!-- 
	<c:if test="${empty param['searchvalue']}">		
		<div class="row">
			<div class="col-lg-6 col-md-12 hidden-xs">
			<div class="well">
		          <h2>Featured Dataset</h2>
		          <p><em>PLUQI</em> <a class="theme-text" href="${contextPath }/pages/ddp.jsp?pluqi-ddp">portal</a> shows various statistical data from South Korea.</p>
		          <p><a class="theme-text" href="${contextPath }/pages/ddp.jsp?pluqi-ddp" target="_blank"><img src="${contextPath }/images/featured-ddp.png" class="featured" alt="PLQUI" title="PLQUI"></a></p>
		          <a class="theme-text" href="${contextPath }/pages/ddp.jsp?pluqi-ddp" class="btn btn-sup btn-material-pink btn-raised" data-toggle="tooltip" data-placement="right" title="" data-original-title="Open PLUQI data driven portal">
		            <span>VIEW PORTAL</span>
		            <i class="mdi-av-fast-forward theme-text"></i>
		          </a>
        	</div>
			</div>

			
			<div class="col-lg-6 col-md-12 hidden-xs">
			<div class="well">
	          <h2>Featured Application</h2>
	          <p><em>PLUQI</em> <a class="theme-text" href="${contextPath }/PLUQI">application</a> shows various statistical data from South Korea.</p>
	          <p><a class="theme-text" href="${contextPath }/PLUQI" target="_blank"><img src="${contextPath }/images/featured-app.jpg" class="featured" alt="PLQUI" title="PLQUI"></a></p>
	          <a class="theme-text" href="${contextPath }/PLUQI" class="btn btn-sup btn-material-deeppurple btn-raised">
	            <span>VIEW APPLICATION</span>
	            <i class="mdi-av-fast-forward theme-text"></i>
	          </a>
	        </div>
			</div>
			
			
			
		</div>
	</c:if>
 -->
<div class="row">
	<c:set var="titleDataset" value="Latest data pages"/>
	<c:set var="emptylabel" value="No data pages in the system. <a  class='theme-text' href='${contextPath }/pages/publish/index.jsp'>Register</a> and create the first one." />
	<c:if test="${ not empty param['searchvalue']}">
	Data pages (filtered by ‘test’)
	<c:set var="titleDataset" value="Data pages (filtered by &quot;${param['searchvalue']}&quot;)" />
	<c:set var="emptylabel" value="No matching data pages found. <a class='theme-text' href='${contextPath }/pages/catalogs/index.jsp'>Show all?</a> " />
	</c:if>
	<form id="selectdataset" action="${contextPath}/pages/publish/details.jsp" method="get">
	<input type="hidden" id="id" name="id"/>
	</form>
		<div class="col-lg-6 col-md-6">
			<template:latestdatasets id='catalogresult' col1="Dataset" col2="User" col3="Published"  col4="Portal" emptylabel="${emptylabel}" label="${titleDataset}" templateData="${datasetBean.getCatalogSharedDataset()}"/>
		</div>
		
		
	<c:set var="titleApplication" value="Latest portals"/>
	<c:set var="emptylabel" value="No portals in the system. <a  class='theme-text' href='${contextPath }/pages/publish/index.jsp'>Register</a> and create the first one." />
	<c:if test="${ not empty param['searchvalue']}">
	<c:set var="titleApplication" value="Portals (filtered by  &quot;${param['searchvalue']}&quot;)"/>
	<c:set var="emptylabel" value="No matching portals found. <a class='theme-text' href='${contextPath }/pages/catalogs/index.jsp'>Show all?</a>" />
	</c:if>

		<div class="col-lg-6 col-md-6">
			<template:latestdatasets id='portalresult' col1="Dataset" col2="User" col3="Published"  col4="Portal" emptylabel="${emptylabel}" label="${titleApplication}" templateData="${datasetBean.getCatalogDatasetPortals()}"/>
		</div>
</div>
    </jsp:body>
</template:genericpage>