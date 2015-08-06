<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:useBean id="querydetails" class="eu.dapaas.bean.QyeryDatasetBean" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- <c:set var="result" value="${querydetails.executeQuery(pageContext.request, pageContext.response, pageContext.session)}" /> --%>

<c:set var="propertyArray" value="${querydetails.getDatasetProperties(pageContext.request, pageContext.response, pageContext.session)}"></c:set>
<template:genericpage title="DataGraft Publisher portal">
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
			<h2 class="table-label">Results</h2>
			<ul id="query-tabs" class="nav nav-tabs theme-bg">
			    <li><a href="#query-results">Query</a></li>
			    <li><a href="#query-builder-results">Query Builder</a></li>
			</ul>
			<div id="query-results" class="tab-section">
				<form id="queryform" action="${contextPath }/pages/dataset_queryresult.jsp" method="post">
					<input type="hidden" name="id" id="id" value="${param['id']}">
					<input type="hidden" name="publisher" id="publisher" value="${param['publisher']}">
					<div id="textarea">
						<textarea id="line_numbers" name="query">${param['query']}</textarea>
					</div>
				</form>
			</div>
			<div id="query-builder-results" class="tab-section">
				<form id="query-buider-form" action="${contextPath }/pages/dataset_queryresult.jsp" method="post">
					<%-- <c:set var="pro" value="${propertyArray}"></c:set> --%>
					<c:forEach items="${propertyArray}" var="prop">
						<div class="checkbox-wrapper">
							<div class="query-checkbox">
						  		<input type="checkbox" value="${prop}" id="${prop}" name="${prop}" />
							  	<label for="${prop}"><span class="query-property-value">${prop}</span></label>
		  					</div>
	  					</div>
  					</c:forEach>
				</form>
			</div>
			<div class="linkbar">
				<a type="button" class="btn btn-primary btn-raised theme-bg" id="querybutton" href="#"> Execute </a>
			</div>
			<ul id="tabs" class="nav nav-tabs margtop theme-bg">
			    <li ><a href="#table-results">Table Results</a></li>
			    <li class="charts"><a href="#barchart-results">Bar chart Results</a></li>
			    <li class="charts"><a href="#linechart-results">Line chart Results</a></li>
			    <li class="charts"><a href="#piechart-results">Pie chart Results</a></li>
			    <li class="charts"><a href="#scatter-results">Scatter chart Results</a></li>
			    <li class="b-chart"><a href="#bubble-results">Bubble chart Results</a></li>
			    <!-- <li class="maps"><a href="#map-results">Map Results</a></li> -->
			    <li class="maps"><a href="#googlemap-results">Google map Results</a></li>
			    
			</ul>
			<div id="table-results" class="result-tab-section">
				<div id="tResult" ></div>	
			</div>
			<div id="linechart-results" class="result-tab-section">
				<div id="lChart">
				</div>
			</div>
			<div id="barchart-results" class="result-tab-section">
				<div id="bChart"></div>
			</div>
			<div id="piechart-results" class="result-tab-section">
				<div id="pChart"></div>
			</div>
			<div id="scatter-results" class="result-tab-section">
				<div id="sChart"></div>
			</div>
			<div id="bubble-results" class="result-tab-section">
				<div id="bubbleChart"></div>
			</div>
			<div id="googlemap-results" class="result-tab-section">
				<div id="gMap"></div>
			</div>
			
		</div>
		<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
		<script type="text/javascript" src="//code.highcharts.com/highcharts.js" defer></script>
		<script type="text/javascript" src="//code.highcharts.com/highcharts-more.js" defer></script>
		<script src="//code.highcharts.com/maps/modules/map.js" defer></script>
		<script type="text/javascript" src="../scripts/common.js" defer></script>
		<script type="text/javascript" src="../scripts/properties.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartLine.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartBar.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartPie.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartScatter.js" defer></script>
		<script type="text/javascript" src="../scripts/highchartBubble.js" defer></script>
		<script type="text/javascript" src="../scripts/drawTable.js" defer></script>
		<script type="text/javascript" src="../scripts/googleMaps.js" defer></script>
		<script type="text/javascript" src="../scripts/drawService.js" defer></script>
	</jsp:body>
</template:genericpage>