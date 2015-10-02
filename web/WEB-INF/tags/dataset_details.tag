<%@tag import="eu.dapaas.utils.Config"%>
<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="templateData" type="eu.dapaas.dao.DatasetCatalogDetails"%>
<jsp:useBean id="querydetails" class="eu.dapaas.bean.QyeryDatasetBean" />
<%@ attribute name="editable" type="java.lang.String"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:if test="${param['action'] == 'export' }">
${querydetails.exportRDF(pageContext.request, pageContext.response, pageContext.session)}
</c:if>
<c:set var="propertyArray" value="${querydetails.getDatasetProperties(pageContext.request, pageContext.response, pageContext.session)}"></c:set>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<table id="catalogdetails" class="table table-latest table-app table-striped table-hover  table-responsive details">
	<!-- class="mobile-details dataset-details details" -->
	<!-- <thead class="table-head">
		<tr>
			<th></th>
			<th></th>

		</tr>
	</thead> -->
	<tbody>
		<fmt:parseDate value="${templateData.issued}" pattern="yyyy-MM-dd"
			var="issuedDate" />
		<fmt:parseDate value="${templateData.modified}" pattern="yyyy-MM-dd"
			var="modifiedDate" />

		<tr>
			<td class="first-row">Title</td>
			<td>${ templateData.title}</td>
		</tr>
		<tr>
			<td class="first-row">Issued Date</td>
			<td><fmt:formatDate value="${issuedDate}" pattern="dd-MM-yyyy" /></td>
		</tr>

		<tr>
			<td class="first-row">Portal</td>
			<td><a class="theme-text" href="${contextPath}/<%=Config.getInstance().getPortalURL()%>${templateData.portalParameter}"><%=Config.getInstance().getPortalURL()%>${templateData.portalParameter}</a></td>
		</tr>

		<tr>
			<td class="first-row">Modified Date</td>
			<td><fmt:formatDate value="${modifiedDate}" pattern="dd-MM-yyyy" /></td>
		</tr>
		<tr>
			<td class="first-row">Description</td>
			<td>${templateData.description}</td>
		</tr>
		<tr>
			<td class="first-row">Publisher</td>
			<td>${templateData.publisher}</td>
		</tr>
		<tr>
			<td class="first-row">Keywords</td>
			<td><c:forEach items="${templateData.keyword}" var="k">${k }, </c:forEach>
			</td>
		</tr>

	</tbody>
</table>
<c:if test="${editable == 'editable'}">
	<div class="linkbar">
		<a type="button" class="btn btn-primary btn-raised theme-bg" data-toggle="modal" data-target="#dialog-export" > 	Export RDF </a> 
		<a type="button" class="btn btn-primary btn-raised theme-bg" href="${contextPath }/pages/publish/${templateData.id}"> 	Edit </a> 
		
	</div>
</c:if>
<ul id="query-tabs" class="nav nav-tabs margtop theme-bg">
	<li><a href="#query-results">Query</a></li>
	<li ${fn:length(propertyArray)==0 ? 'style="display: none;"' : ''}><a href="#query-builder-results">Query Builder</a></li>
</ul>
<div id="query-results" class="tab-section">
<form id="queryform" action="${contextPath }/pages/dataset_queryresult.jsp" method="post">
	<input type="hidden" name="id" value="${templateData.id}"> <input
	type="hidden" name="publisher" value="${templateData.publisher}">
	<div id="textarea">
		<textarea id="line_numbers" name="query"></textarea>
	</div>
	</form>
</div>
<div id="query-builder-results" class="tab-section">
	<form id="query-buider-form" action="${contextPath }/pages/dataset_queryresult.jsp" method="post">
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

<div id="dialog-export" title="" class="modal fade" tabindex="-1">
<div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">Export RDF</h4>
      </div>
    <div class="modal-body">
	<form id="dialogexport" method="POST">
		<input type="hidden" id="action" name="action" /> <input
			type="hidden" id="id" name="id" value="${templateData.id}" />
		<fieldset>
			<label>Export File Type</label> <select id="contenttype"
				name="contenttype">
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



<!-- 
<div id="dialog-lodlive-preview" title="" class="modal fade" tabindex="-1">
<div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">Lodlive preview</h4>
      </div>
    <div class="modal-body">
	<form id="querylodliveform" action="lodlive_preview.jsp">
		<input type="hidden" name="id" value="${templateData.id}">
		<input type="hidden" name="publisher" value="${templateData.publisher}">
		<p>Query:</p>
		<div id="textarea">
			<textarea id="lodliveQuery" name="lodliveQuery"></textarea>
		</div>
	</form>
	</div>
	<div class="modal-footer">
		<a type="button" class="btn btn-primary btn-raised theme-bg" id="querylodlivebutton" href="#">
			Execute </a>
	</div>
	</div>
	</div>
</div> -->