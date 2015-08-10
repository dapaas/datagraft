<%@page import="eu.dapaas.utils.Config"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="catalogdetails" class="eu.dapaas.bean.DatasetAllBean"/>
<jsp:useBean id="querydetails" class="eu.dapaas.bean.QyeryDatasetBean" />
<jsp:setProperty name="catalogdetails" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="catalogdetails" property="session" value="${pageContext.session}"/>

<c:set var="ddpurl" value="<%=Config.getInstance().getPortalURL() %>"></c:set>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<c:set var="user" value="${sessionScope.dapaas_user}" />
<c:set var="dataset" value="${catalogdetails.getCatalogDetails(param['id']) }" />
<c:set var="portalpath" value="<%=Config.getInstance().getPortalURL()%>" />
	

<c:if test="${param['action'] == 'export' }">
	${querydetails.exportRDF(pageContext.response, '', '', 'guest', dataset.id, param['exportcontenttype'])}
</c:if>
<c:if test="${param['action'] == 'exportraw' }">
	${querydetails.exportRaw(pageContext.response, '', '', 'guest', dataset.id)}
</c:if>

<c:set var="propertyArray" value="${querydetails.getDatasetProperties( '', '', dataset.id)}"></c:set>

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
		<div class="container">
			<ul class="breadcrumb">
    			<li><a class="theme-text" href="${contextPath }/pages/catalogs">Explore</a></li>
    			
    			<li class="active">Data Page</li>
			</ul>
			
		
				<h1 class="add-label">Preview ${dataset.title}  </h1>
						
			
			
			<div id="content"  class="well">
<h2 class="dataset-label">Data page properties</h2>
  		
  		<div class="form-horizontal">
								<div class="form-group">
                                        <label for="datasetname" class="col-lg-2 control-label">Name:</label>
                                        <div class="col-lg-10">
                                            <div class="form-control-wrapper">
                                            <label id="datasetname" class="form-control empty" >${dataset.title} </label>
                                            
                                            </div>
                                           
                                        </div>
                                  </div>


  		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Description:</label>
			 <div class="col-lg-10">
	             <div class="form-control-wrapper">
				<div id="description" class="form-control empty" >${ dataset.description }</div>
				
				</div>
				 
			</div>
		</div>
		
		
		
		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Owner:</label>
			 <div class="col-lg-10">
             <div class="form-control-wrapper">
				<label  class="form-control empty">
					${dataset.publisher }
				</label>
			</div>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Date:</label>
			 <div class="col-lg-10">
             <div class="form-control-wrapper">
				<label  class="form-control empty">
				<c:if test="${wizard.action =='edit' }">
					<fmt:parseDate value="${wizard.details.issued}" pattern="yyyy-MM-dd" var="issuedDate"/>
					<fmt:formatDate value="${issuedDate }" pattern="d MMM yyyy"/>
				</c:if>
				<c:if test="${wizard.action =='new' }">
					<c:set var="now" value="<%=new java.util.Date()%>" />
					<fmt:formatDate value="${now }" pattern="d MMM yyyy"/>
				</c:if>
				
				</label>
			</div>
			</div>
		</div>

		<c:set var="kw" value="" />
		<c:forEach items="${dataset.keyword}" var="k" >
			<c:set var="kw" value="${kw}${k}," />
		</c:forEach>
		<div class="form-group">
			<label class="col-lg-2 control-label" for="keyword">Keyword:</label>
			<div class="col-lg-10">
            <div class="form-control-wrapper">
			<label class="form-control empty" >${kw } </label>
			
			</div>
			</div>
		</div>
		
</div>

<form id="exportrawform" method="POST">
	<input type="hidden" id="action" name="action" /> 
</form>

<c:if test="${not empty dataset.accessURL }" >
		<!--  SPARQL  -->

		<div class="dataset-wrapper" id="sparqueryresult">
			<h2 class="table-label">SPARQL</h2>
			<h5>Endpoint: ${dataset.accessURL }</h5>
			<ul id="query-tabs" class="nav nav-tabs theme-bg">
			    <li><a href="#query-results">Query</a></li>
			    <li><a href="#query-builder-results">Query Builder</a></li>
			</ul>
			<div id="query-results" class="tab-section">
				<form id="queryform"  method="post" action="#table-results">
					<input type="hidden" name="id" id="id" value="${dataset.id}">
					<input type="hidden" name="action" id="action" value="execute">
					
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
				<a type="button" class="btn btn-primary btn-raised theme-bg" id="querybutton"> Execute </a>
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
				<div id="tResult" >No result found</div>	
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
			
			<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
			<script type="text/javascript" src="//code.highcharts.com/highcharts.js" defer></script>
			<script type="text/javascript" src="//code.highcharts.com/highcharts-more.js" defer></script>
			
			<script src="//code.highcharts.com/maps/modules/map.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/common.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/properties.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartLine.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartBar.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartPie.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartPoligon.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartScatter.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/highchartBubble.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/drawTable.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/googleMaps.js" defer></script>
			<script type="text/javascript" src="${contextPath }/scripts/drawService.js" defer></script>
		</div>
		
		<!-- END SPARQL -->
</c:if>
		<div class="publish-div">
		<c:if test="${not empty dataset.accessURL }" >
			<a type="button" class="btn btn-primary btn-raised theme-bg" data-toggle="modal" data-target="#dialog-export" id="exportrdf">Export RDF</a>
			</c:if>
			<c:if test="${not empty dataset.fileId }">
				<a type="button" class="btn btn-primary btn-raised theme-bg "  id="exportraw">Export Raw</a>
			</c:if>
			<c:if test="${not empty dataset.accessURL }" >
			<a type="button" class="btn btn-primary btn-raised theme-bg add-btn" target="_blank" href="${contextPath}/${portalpath}${dataset.portalParameter}" title="View portal at /${dataset.portalParameter}" > View portal </a>
			</c:if>
		</div>
		
</div>

</div>
 
<!-- end dataset properties -->
 

  
  <div id="dialog-export" title="" class="modal fade" tabindex="-1">
<div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">Export RDF</h4>
      </div>
    <div class="modal-body">
	<form id="dialogexport" method="POST">
		<input type="hidden" id="action" name="action" /> 
		<fieldset>
			<label>Export File Type</label> 
			<select id="exportcontenttype" name="exportcontenttype">
				<option value="application/rdf+xml">RDF/XML</option>
				<option value="text/plain">N-TRIPLE</option>
				<option value="text/turtle">TURTLE</option>
				<option value="text/rdf+n3">N3</option>
				<option value="text/x-nquads">NQUADS</option>
				<option value="application/rdf+json">RDF/JSON</option>
			</select>
		</fieldset>
		
	</form>
	</div>
	
	<div class="modal-footer">
	<input type="button" value="Export" class="btn btn-primary btn-raised theme-bg" id="exportfilebutton">
	</div>
	</div>
	</div>
</div>
  
  

	</jsp:body>
</template:genericpage>