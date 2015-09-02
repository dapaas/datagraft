<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>

<jsp:useBean id="datasetbean" class="eu.dapaas.bean.DatasetBean"/>
<jsp:setProperty name="datasetbean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="datasetbean" property="session" value="${pageContext.session}"/>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:forEach var="pageParameter" items="${param}">
<c:set var="portal" value="${datasetbean.getPortalByParameter(param['stat']) }"/>
</c:forEach>

<template:genericpage title="Data Driven portal">

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
	
<script>

	Service.param="${param['stat']}";

</script>   

 

		<div class="feedback">
			<label for="feedback">Please rate us</label>
			<input name="star1" type="radio" class="hover-star" value="1"
				title="Very Poor" /> 
			<input name="star1" type="radio" class="hover-star" value="2"
				title="Poor" />
			<input name="star1" type="radio" class="hover-star" value="3"
				title="Ok" /> 
			<input name="star1" type="radio" class="hover-star" value="4"
				title="Good" /> 
			<input name="star1" type="radio" class="hover-star" value="5"
				title="Very Good" />
			<span id="feedback-rate"></span>
		</div>
		<h1><c:if test="${not empty portal.title }">${portal.title }</c:if></h1>	
		
		<div id="chartsprogress" class="progress progress-striped active">
              <div class="progress-bar theme-bg" ></div>
        </div>
		
		<div id="container" ></div>
		
		
		<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
		<script type="text/javascript" src="//code.highcharts.com/highcharts.js" defer></script>
		<script type="text/javascript" src="//code.highcharts.com/highcharts-more.js" defer></script>
		<script src="//code.highcharts.com/maps/modules/map.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/common.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/properties.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/highchartLine.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/highchartBar.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/highchartPie.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/highchartScatter.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/highchartBubble.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/drawTable.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/googleMaps.js" defer></script>
		<script type="text/javascript" src="${contextPath}/scripts/main.js" defer></script>				
	</jsp:body>
</template:genericpage>