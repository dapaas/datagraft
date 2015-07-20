<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>

<jsp:useBean id="datasetbean" class="eu.dapaas.bean.DatasetBean"/>
<jsp:setProperty name="datasetbean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="datasetbean" property="session" value="${pageContext.session}"/>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:forEach var="pageParameter" items="${param}">
<c:set var="portal" value="${datasetbean.getPortalByParameter(pageParameter.key) }"/>
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
<c:forEach var="pageParameter" items="${param}">
	Service.param="${pageParameter.key}";
</c:forEach>
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
		<h1>${portal.title }</h1>	
		
		<div id="chartsprogress" class="progress progress-striped active">
              <div class="progress-bar theme-bg" ></div>
        </div>
		
		<div id="container" ></div>
		
		
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
		<script type="text/javascript" src="http://code.highcharts.com/highcharts.js" defer></script>
		<script type="text/javascript" src="http://code.highcharts.com/highcharts-more.js" defer></script>
		<script src="http://code.highcharts.com/maps/modules/map.js" defer></script>
		<script type="text/javascript" src="../scripts/common.js" defer></script>
		<script type="text/javascript" src="../scripts/properties.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartLine.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartBar.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartPie.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartScatter.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartBubble.js" defer></script>
		<script type="text/javascript" src="../scripts/drawTable.js" defer></script>
		<script type="text/javascript" src="../scripts/googleMaps.js" defer></script>
		<script type="text/javascript" src="../scripts/main.js" defer></script>				
	</jsp:body>
</template:genericpage>