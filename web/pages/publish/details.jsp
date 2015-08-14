<%@page import="eu.dapaas.utils.Config"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="catalogdetails" class="eu.dapaas.bean.DatasetBean"/>
<jsp:useBean id="querydetails" class="eu.dapaas.bean.QyeryDatasetBean" />
<jsp:setProperty name="catalogdetails" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="catalogdetails" property="session" value="${pageContext.session}"/>
<%
String israw = request.getParameter("israw");
%>
<template:validation/>
<c:set var="ddpurl" value="<%=Config.getInstance().getPortalURL() %>"></c:set>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }


<c:set var="user" value="${sessionScope.dapaas_user}" />
<c:if test="${not empty user }" >
<c:set var="apiKey" value="${user.apiKey }" />
<c:set var="apiSecret" value="${user.apiSecret }" />
</c:if>
<c:if test="${not empty param['id'] && param['action'] != 'execute' and not empty user}" >
	<c:set var="wizard" value="${catalogdetails.getDatasetForEdit(wizard, apiKey, apiSecret, param['id'])}" scope="session" />
</c:if>

	
<c:set var="filename" value="${fn:substring(wizard.uploadesFile.file.name, 0, fn:indexOf(wizard.uploadesFile.file.name, '.'))}" /> 

<c:if test="${param['action'] == 'export' }">
	${querydetails.exportRDF(pageContext.response, apiKey, apiSecret, user.username, wizard.details.id, param['exportcontenttype'])}
</c:if>
<c:if test="${param['action'] == 'exportraw' }">
	${querydetails.exportRaw(pageContext.response, apiKey, apiSecret, user.username, wizard.details.id)}
</c:if>

<c:set var="poligons" value="${catalogdetails.getPoligons() }" />
<c:set var="propertyArray" value="${querydetails.getDatasetProperties( apiKey, apiSecret, wizard.details.id)}"></c:set>

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
    			<li><a class="theme-text" href="${contextPath }/pages/myassets">Dashboard</a></li>
    			<li class="active">Data Page</li>
			</ul>
			<c:if test="${wizard.action =='new' }">
				<h1 class="add-label">Data page</h1>
			</c:if>
			<c:if test="${wizard.action =='edit' }">
				<c:url value="/pages/publish" var="dwnUrl" scope="request">
					<c:param name="id" value="${wizard.details.id}"/>
				</c:url>
				<h1 class="add-label">Edit ${wizard.details.title}  </h1>
				<a class="theme-text linka" href="${dwnUrl }">Add other data</a>				
			</c:if>
			
			<div id="content"  class="well">
<h2 class="dataset-label">Data page properties</h2>
  		
  		<div class="form-horizontal">
								<div class="form-group">
                                        <label for="datasetname" class="col-lg-2 control-label">Name:</label>
                                        <div class="col-lg-10">
                                            <div class="form-control-wrapper">
                                            <input type="text" id="datasetname" name="datasetname" class="form-control empty" value="${(wizard.action =='new') ? filename : wizard.details.title}" placeholder="Name...">
                                            <span class="material-input"></span>
                                            </div>
                                           
                                        </div>
                                  </div>


  		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Description:</label>
			 <div class="col-lg-10">
	             <div class="form-control-wrapper">
				<textarea id="description" name="description" class="form-control empty" rows="3" placeholder="Description...">${ (wizard.action =='new') ? filename : wizard.details.description }</textarea>
				<span class="material-input"></span>
				</div>
				 <div class="checkbox">
	                    <label>
	                        <input type="checkbox" id="ispublic" ${ (wizard.action =='new') ? '' : ((wizard.details.exposePublic) ? 'checked': '') }> Expose as public
	                    </label>
	             </div>
			</div>
		</div>
		
		<c:if test="${wizard.action =='new' and wizard.uploadesFile.filetype == 'RDF' }">
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">File Content type:</label>
			 <div class="col-lg-10">
             <div class="form-control-wrapper">
             
			<select id="contenttype" class="form-control empty">
				<option value="application/rdf+xml" ${(wizard.uploadesFile.contentType == 'application/rdf+xml') ? 'selected' : '' }>RDF/XML</option>
				<option value="text/plain" ${(wizard.uploadesFile.contentType == 'text/plain') ? 'selected' : '' }>N-Triples</option>
				<option value="text/turtle" ${(wizard.uploadesFile.contentType == 'text/turtle') ? 'selected' : '' }>Turtle</option>
				<option value="text/rdf+n3" ${(wizard.uploadesFile.contentType == 'text/rdf+n3') ? 'selected' : ''}>N3</option>
				<option value="text/x-nquads" ${(wizard.uploadesFile.contentType == 'text/x-nquads') ? 'selected' : ''}>N-Quads</option>
				<option value="application/rdf+json" ${(wizard.uploadesFile.contentType == 'application/rdf+json') ? 'selected' : ''}>RDF/JSON</option>
				<option value="application/trix" ${(wizard.uploadesFile.contentType == 'application/trix') ? 'selected' : ''}>TriX</option>
				<option value="application/x-trig" ${(wizard.uploadesFile.contentType == 'application/x-trig') ? 'selected' : ''}>TriG</option>
				<option value="application/x-binary-rdf" ${(wizard.uploadesFile.contentType == 'application/x-binary-rdf') ? 'selected' : ''}>Sesame Binary RDF</option>
			</select>
			
			<span class="material-input"></span>
			</div>
			</div>
		</div>
		</c:if>
		
		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Owner:</label>
			 <div class="col-lg-10">
             <div class="form-control-wrapper">
				<label  class="form-control empty">
				<c:if test="${wizard.action =='edit' }">
					${wizard.details.publisher }
				</c:if>
				<c:if test="${wizard.action =='new' }">
					${user.name }
				</c:if>
				
				</label>
			</div>
			</div>
		</div>

<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Creation Date:</label>
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
		<c:forEach items="${wizard.details.keyword}" var="k" >
			<c:set var="kw" value="${kw}${k}," />
		</c:forEach>
		<div class="form-group">
			<label class="col-lg-2 control-label" for="keyword">Keyword:</label>
			<div class="col-lg-10">
            <div class="form-control-wrapper">
			<input type="text" id="keyword" name="keyword" class="form-control empty" value="${(wizard.action =='new') ? filename : kw }" placeholder="Keywords..."/>
			<span class="material-input"></span>
			</div>
			</div>
		</div>
		
</div>

<form id="exportrawform" method="POST">
	<input type="hidden" id="action" name="action" /> 
</form>
<c:if test="${not empty wizard.details.accessURL }" >

		<div id="setupdpp" class="${wizard.action =='new'? 'down': 'up' }">
			<h2 class="dataset-setup-label">Setup visualization</h2>
			<div class="form-horizontal">
			<div class="form-group">
				<label class="col-lg-2 control-label" >ID</label>
				<div class="col-lg-10">
		            <div class="form-control-wrapper">
						
							<input type="text" id="portalparam" name="portalparam" value="${ (not empty wizard.portal.parameter)? wizard.portal.parameter :  (wizard.action =='new') ? filename : wizard.details.title }" class="form-control"  placeholder="Portal..." />
						
						<span class="material-input"></span>
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-lg-2 control-label" >Title</label>
				<div class="col-lg-10">
		            <div class="form-control-wrapper">
							<input type="text" id="portaltitle" name="portaltitle" value="${wizard.portal.title }" class="form-control"  placeholder="Title" />
						<span class="material-input"></span>
					</div>
				</div>
			</div>
			
			</div>


			<div id="select-group">
				<table class="table table-striped table-hover table-responsive" id="portal-table">
				<thead>
					<tr>
					  	<th>Widget</th>
			        	<th>Title</th>
			        	<th>Action</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${wizard.portal.portalContent }" var="pc">
					<tr id="${pc.id}">
						<td >${pc.chartLabel}</td>
						<td >${pc.title}</td>
						<td class="tableAction">
							<a class="aimg" title="Edit" href="javascript:editConfiguration({id: '${pc.id}', title: '${pc.title}', description: '${fn:escapeXml(pc.descriptionHtml)}', summary: '${fn:escapeXml(pc.summaryHtml)}',  datePattern: '${fn:escapeXml(pc.datePattern)}', query: '${fn:escapeXml(pc.queryHtml)}', chart: '${pc.chart}'} )" >
								 <i class="mdi-content-create theme-text"></i></a>
			           		<a id="deleteda" title="Delete" class="confirmation aimg" href="javascript:deleteContent('${pc.id }')" >
			           			<i class="mdi-content-remove-circle-outline theme-text"></i></a>
			           	</td>
					</tr>
				</c:forEach>
				</tbody>
				</table>
			
			</div>
			<div class="publish-div">
				<c:if test="${not empty wizard.portal.portalContent }">
			    	<a href="${contextPath}/${ddpurl}${wizard.portal.parameter}" target="_blank" id="viewportal" class="btn btn-primary btn-raised theme-bg">View portal</a>
			    </c:if>
				<a href="javascript:openDialogPortal()" id="addportalrow" class="btn btn-primary btn-raised theme-bg add-btn">Add widget</a>
			</div>
		</div>
		<!--  SPARQL  -->
		
		<div class="dataset-wrapper ${wizard.action =='new'? 'hide' : '' }" id="sparqueryresult">
			<h2 class="table-label">SPARQL</h2>
			<h5 class="table-label theme-bg">Endpoint: ${wizard.details.accessURL }</h5>
			<ul id="query-tabs" class="nav nav-tabs theme-bg">
			    <li><a href="#query-results">Query</a></li>
			    <li><a href="#query-builder-results">Query Builder</a></li>
			</ul>
			<div id="query-results" class="tab-section">
				<form id="queryform"  method="post" action="#table-results">
					<input type="hidden" name="id" id="id" value="${wizard.details.id}">
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
			<c:if test="${not empty wizard.details.accessURL }" >
				<a type="button" class="btn btn-primary btn-raised theme-bg ${wizard.action =='new'? 'hide' : '' }" data-toggle="modal" data-target="#dialog-export" id="exportrdf">Export RDF</a>
			</c:if> 
			<c:if test="${not empty wizard.details.fileId }">
				<a type="button" class="btn btn-primary btn-raised theme-bg ${wizard.action =='new'? 'hide' : '' }"  id="exportraw">Export Raw</a>
			</c:if>
			
			<input type="button" id="savedataset" data-israw="${param['israw'] }" value="${(wizard.action =='new') ? 'Create Data Page' : 'Save Data Page'}" class="btn btn-primary btn-raised publish-button theme-bg" />
			<c:if test="${wizard.action =='edit'}">
				<c:url value="/pages/myassets" var="delUrl" scope="request">
				  <c:param name="id" value="${wizard.details.id}"/>
				  <c:param name="delete" value="dataset"/>
				</c:url>
				<a href="${delUrl }" class="btn btn-primary btn-raised publish-button theme-bg confirmation" >Delete Data Page</a>
			</c:if>
		</div>
		
		</div>

</div>
 
<!-- end dataset properties -->
 
<div id="dialog-portal" title="Configure widget">

    <form id="dialogportal" method="POST" action="${contextPath }/pages/publish" class="form-horizontal">
      <input type="hidden" id="action" name="action" value="${wizard.action}" />
      <!-- input type="hidden" id="portalparam" name="portalparam" value="" /-->
      <input type="hidden" id="portalid" name="portalid" value="" />
      
       <div class="form-group">
        <label for="drawtype" class="col-lg-2 control-label">Type</label>
        <div class="col-lg-10">
        <div class="form-control-wrapper">
        <select name="drawtype" id="drawtype" class="form-control">
        <option value="drawTable">Tabular View</option>
        <option value="drawLineChart">Line Chart</option>
        <option value="drawBarChart">Bar Chart</option>
        <option value="drawPieChart">Pie Chart</option>
        <option value="drawScatterChart">Scatter Chart</option>
        <option value="drawBubbleChart">Bubble Chart</option>
        <option value="googleMaps">Google Maps</option>
        <option value="drawPoligonChart">Poligon Chart</option>
      	</select>
        <span class="material-input"></span>
        </div>
        </div>
      	</div>
      	
      	<div class="form-group">
        <label for="title" class="col-lg-2 control-label">Title</label>
        <div class="col-lg-10">
        <div class="form-control-wrapper">
        <input id="title" name="title" type="text" class="form-control" data-ripple-color="#F0F0F0"/>
        <span class="material-input"></span>
        </div>
        </div>
        </div>
        
       
      	
      <div class=" form-group date-pattern">
      	<label for="datePattern" class="col-lg-2 control-label">Date Pattern</label>
      	<div class="col-lg-10">
        <div class="form-control-wrapper">
      	<input id="datePattern" name="datePattern" type="text" placeholder="%d/%m/%Y %M:%S" class="form-control"/>
      	<span class="material-input"></span>
        </div>
        </div>
	  </div>
	  
	  
	        	
      <div class=" form-group poligon">
      	<label for="poligon" class="col-lg-2 control-label">Poligons</label>
      	<div class="col-lg-10">
        <div class="form-control-wrapper">
        
        <select name="poligon" id="poligon" class="form-control">
        	<c:forEach var="pol" items="${poligons }">
        	<option value="${pol.id }">${pol.title }</option>
        	</c:forEach>
      	</select>
      	<span class="material-input"></span>
        </div>
        </div>
	  </div>
        
        
        <div class=" form-group">
        <label for="pdescription" class="col-lg-2 control-label">Description</label>
        <div class="col-lg-10">
        <div class="form-control-wrapper">
        <textarea id="pdescription" name="pdescription" style="height: 100px" class="form-control"></textarea>
        <span class="material-input"></span>
        </div>
        </div>
        </div>
        
        <div class=" form-group">
        <label for="psummary" class="col-lg-2 control-label">Summary</label>
        <div class="col-lg-10">
        <div class="form-control-wrapper">
        <textarea id="psummary" name="psummary" style="height: 100px" class="form-control"></textarea>
        <span class="material-input"></span>
        </div>
        </div>
         </div>
      
      <p> </p>
      
        <div class=" form-group">
         <label for="textarea" class="col-lg-2 control-label">Query</label>
         <div class="col-lg-10">
        <div class="form-control-wrapper">
        <div id="textarea">
        <textarea id="line_numbers" name="query"></textarea>
        </div>
         <span class="material-input"></span>
        </div>
        </div>
      </div>
    </form>
    
  </div>
 
  
  
  <div id="dialog-preview-portal" title="Preview">
  
	    <div id="container"></div>
		
  </div>
  
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
  
  <div id="dialog-confirm" title="Delete?"></div>
  
  <!-- dialog for spinner -->
 <div id="complete-dialog-spinner" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h4 class="modal-title">Saving Data Page</h4></div>
      <div class="modal-body">
        <div id="containerl"> </div>
     </div>
    </div>
  </div>
</div>
	</jsp:body>
</template:genericpage>