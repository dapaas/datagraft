<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<jsp:useBean id="applicationbean" class="eu.dapaas.bean.ApplicationBean"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:setProperty name="wizard" property="type" value="application"/>
<jsp:setProperty name="wizard" property="action" value="new"/>

${wizard.emptyNew()}
${applicationbean.getApplicationForEdit(pageContext.request, pageContext.response, pageContext.session)}

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<template:validation/>
<template:genericpage title="DaPaaS Developer portal">
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
<c:if test="${wizard.action == 'new' }">
<h1 class="add-label">Upload my application</h1>
</c:if>
<c:if test="${wizard.action == 'edit' }">

<h1 class="add-label">Edit application '${wizard.details.title}'</h1>
<a class="theme-text" id="updateapplication">Update your application</a>
</c:if>

	<div id="dropzone_application" class="dropzone-dataset ${(wizard.action == 'edit') ? 'down': 'up' }"></div>

	<div id="content"  class="${(wizard.action == 'edit') ? 'up': 'down' }">
		<h2 class="dataset-label">Application properties</h2>
		
		<div class="form-horizontal">
		
		<div class="form-group">
			<label class="col-lg-2 control-label" for="applicationname">Application name:</label>
			<div class="col-lg-10">
            <div class="form-control-wrapper">
			<input type="text" id="applicationname" name="applicationname" class="form-control empty" value="${wizard.details.title}" placeholder="Name...">
			<span class="material-input"></span>
            </div>
                                           
            </div>
		</div>
		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Description:</label>
			<div class="col-lg-10">
            <div class="form-control-wrapper">
			<textarea id="description" name="description" class="form-control empty" rows="3" placeholder="Description...">${wizard.details.description }</textarea>
			<span class="material-input"></span>
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
			<input type="text" id="keyword" name="keyword" class="form-control empty" value="${kw }" placeholder="Keywords..."/>
			<span class="material-input"></span>
            </div>
                                           
            </div>
		</div>
		<div class="form-group">
			<label for="deploymentname" class="col-lg-2 control-label">Deployment Name</label>
			<div class="col-lg-10">
            <div class="form-control-wrapper">
			<input type="text" id="deploymentname" name="deploymentname" class="form-control empty" value="${wizard.details.deploymentName}" placeholder="Deployment..." />
			<span class="material-input"></span>
            </div>
                                           
            </div>
		</div>
</div>


		<div class="publish-div"><input type="button" id="saveapplication" class="btn publish-button btn-primary btn-raised" value="Save" /></div>
	</div>
</div>

</jsp:body>
</template:genericpage>