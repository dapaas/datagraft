<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page import="eu.dapaas.utils.Utils"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean" />
<jsp:useBean id="transformationbean" class="eu.dapaas.bean.TransformationBean" />

<jsp:setProperty name="transformationbean" property="response" value="${pageContext.response}" />
<jsp:setProperty name="transformationbean" property="session" value="${pageContext.session}" />

<c:set var="transformationId" value='<%= Utils.escapeJS(request.getParameter("id")) %>' />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session" />
<c:set var="user" value="${sessionScope.dapaas_user}" />

<c:set var="transformation" value="${transformationbean.getDetail(user, param['id']) }" />
<c:if test="${not empty user }">
	<jsp:setProperty name="wizard" property="transformation" value="${transformation}" />
</c:if>

<c:if test="${param['action'] == 'export' }">
	${transformationbean.executeAndDownload()}
</c:if>
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
	<c:set var="basicAuth" value="${userbean.getTemporaryBasicAuth(pageContext.request, pageContext.response,pageContext.session, 'grafterizer') }"/>
	<form id="formexport" method="post">
		<input type="hidden" name="action" id="action" />
	</form>
	<form id="deletedataset" method="post" action="${contextPath }/pages/transformations/details.jsp">
		<input type="hidden" id="delete" name="delete" />
		<input type="hidden" id="id" name="id"/>
	</form>
	<div class="container">
	<!--ul class="breadcrumb">
    	<li><a class="theme-text" href="${contextPath }/pages/catalogs">Explore</a></li>
    	<c:if test="${not empty user }">
    		<li><a class="theme-text" href="${contextPath }/pages/myassets">Dashboard</a></li>
    	</c:if>
    	<li>Transformation page</li>
	</ul-->
	<c:if test="${not empty user }">
		<h1 class="add-label"> Execute transformation '${transformation.title}'</h1>
	</c:if>
	<c:if test="${empty user }">
		<h1 class="add-label">Preview ${transformation.title}  </h1>
	</c:if>
<div class="well">
		<h2 class="dataset-label">Transformation properties</h2>
  		
  		<div class="form-horizontal">
			<div class="form-group">
	           <label for="datasetname" class="col-lg-2 control-label">Name:</label>
	           <div class="col-lg-10">
	             <div class="form-control-wrapper">
	             <label id="datasetname" class="form-control empty">${transformation.title} </label>
	           </div>
	          </div>
	         </div>
			<div class="form-group">
				<label class="col-lg-2 control-label" for="description">Description:</label>
				 <div class="col-lg-10">
		             <div class="form-control-wrapper">
						<div id="description" class="form-control empty" style="height: auto; min-height: 26px">${ transformation.description }</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-lg-2 control-label" for="description">Owner:</label>
				 <div class="col-lg-10">
	             <div class="form-control-wrapper">
					<label class="form-control empty">
						${transformation.publisher }
					</label>
				</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-lg-2 control-label" for="description">Creation Date:</label>
				 <div class="col-lg-10">
	             <div class="form-control-wrapper">
					<label class="form-control empty">
						<fmt:parseDate value="${transformation.issued}" pattern="yyyy-MM-dd" var="issuedDate" />
						<fmt:formatDate value="${issuedDate }" pattern="d MMM yyyy" />
					</label>
				</div>
				</div>
			</div>
			<c:set var="kw" value="" />
			<c:forEach items="${transformation.keyword}" var="k">
				<c:set var="kw" value="${kw}${k}," />
			</c:forEach>
			<div class="form-group">
				<label class="col-lg-2 control-label" for="keyword">Keyword:</label>
				<div class="col-lg-10">
	            <div class="form-control-wrapper">
				<label class="form-control empty">${kw } </label>
				</div>
				</div>
			</div>
		
		</div>
		<c:if test="${not empty user }">
			<div class="publish-div">
			<!-- is user owner -->
			<c:if test="${not empty user && user.username == transformation.publisher}">
			<c:url value="/pages/myassets" var="delUrl" scope="request">
			  <c:param name="id" value="${transformation.id}"/>
			  <c:param name="delete" value="transformation"/>
			</c:url>
						 
			 <a type="button" id="" class="btn btn-primary btn-raised theme-bg confirmation" href="${delUrl }">Delete</a>
			 <c:url value="/pages/transformations" var="dwnUrl" scope="request">
				<c:param name="id" value="${transformation.id}"></c:param>
			</c:url>
			 <a type="button" class="btn btn-primary btn-raised theme-bg"  href="${dwnUrl}">Edit</a>  
			</c:if>
			 <a type="button" id="forktransformation" class="btn btn-primary btn-raised theme-bg publish-button"  href="javascript:forktransformation('${transformation.id }')"> Fork </a> 
			</div>
		</c:if>		
</div>



<c:if test="${not empty user }">
<!-- title before dropzone -->
	<div class="well">
		<c:if test="${wizard.action =='new' }">
			<h2 class="dataset-label">Create data page</h2>
		</c:if>
		<c:if test="${empty wizard.uploadesFile.file}">
					${ wizard.uploadesFile.setFiletype("GRF")}
					${ wizard.uploadesFile.setContentType(param['id'])}
			<div id="tr_dropzone_dataset" class="dropzone-dataset up">
			</div>
		</c:if>
		<div id="content" class="publish-div ${not empty wizard.uploadesFile.file? 'up': 'down'}">
			 <a type="button" class="btn btn-primary btn-raised theme-bg  ${ (transformation.transformationType == 'pipe') ? 'disabled' : '' }" id="execsrepository" href="#">Publish data page</a> 
			 <a type="button" class="btn btn-primary btn-raised theme-bg publish-button" id="execdlresult" href="#"> Download results </a> 
		</div>
	</div>
</c:if>


<c:if test="${not empty param['id'] }">
<div class="well" id="transformationIframeDetails">
<script async>
$(window).load(function() {
	var graftInstance = new Grafterizer("https://grafterizer.datagraft.net", document.getElementById("transformationIframeDetails"))
		.setAuthorization("${basicAuth}")
		.go('readonly', {
		        id: encodeURI('${transformationId}')
		});
});
</script>
</div>
</c:if>

</div>
<div id="dialog-confirm" title="Delete?"></div>
	<script type="text/javascript" src="${contextPath}/scripts/grafterizerPostMessage.js" async></script>
	</jsp:body>
</template:genericpage>
