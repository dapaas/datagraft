<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="datasetbean" class="eu.dapaas.bean.DatasetBean"/>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:setProperty name="datasetbean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="datasetbean" property="session" value="${pageContext.session}"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }

<c:if test="${empty param['id'] }" >
${wizard.emptyNew()}
</c:if>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}" />

<template:validation/>
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
    <li class="active">Create Data Page</li>
</ul>


<h1 class="add-label">Upload your data</h1>

<div id="dropzone_dataset" class="dropzone-dataset well up">
</div>

<c:if test="${wizard.action =='new'}">
	<div id="content"  class="well down ">
	<h2 class="dataset-label">Create data page</h2>
	
	<p> Create your own data page by selecting one of the three options below</p>
	<div class="row">
	<div class="col-xs-6 col-md-4"> <h4>Use your uploaded data to create a data page </h4><a type="button" class="btn btn-primary btn-raised theme-bg input-full-button"  id="createrawbutton" href="#"> Create raw </a> </div>
	<div class="col-xs-6 col-md-4"> <h4>Use the Grafterizer tool to transform your data</h4> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="createnewtransformation" href="#" data-toggle="modal" data-target="#dialog-refresh"> Create using a new transformation </a> </div>
	<div class="col-xs-6 col-md-4"> <h4>Choose an existing transformation from the library</h4> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="usetransformation" href="#"> Create using existing transformation </a> </div>
	</div>
	
			<div id="transformationpage">
					<h2 class="dataset-label">Search transformation</h2>
					
					<form class="" id="transfsearchform" method="post">
					   <div class="form-group">
					   <div class="input-group">
					        <input type="text" id="transfsearchtext" name="transfsearchtext" class="form-control col-lg-8" placeholder="Search transformation">
					        <span class="input-group-btn">
					            <a id="transfsearchsubmitbutton" class="btn btn-default theme-bg">Search</a>
					        </span>
					    </div> 
					    </div>
					</form>
					<div id="select-group">
									<table class="table table-striped table-hover table-responsive" id="transform-table">
									<thead>
										<tr>
										  	<th></th>
								        	<th></th>
								        	<th></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									</table>
					</div>
			</div>
	</div>
</c:if>
<c:if test="${wizard.action =='edit'}">
	<div id="content"  class="well down ">
	<h2 class="dataset-label">Upload in data page</h2>
	
	<p> Upload your new data in data page by selecting one of the options below</p>
	<div class="row">
	<div class="col-xs-6 col-md-4"> <h4>Use your uploaded data to edit a data page </h4><a type="button" class="btn btn-primary btn-raised theme-bg input-full-button"  id="uploadrawbutton" href="#"> Upload raw </a> </div>
	<div class="col-xs-6 col-md-4"> <h4>Use the Grafterizer tool to transform your data</h4> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="createnewtransformation" href="#" data-toggle="modal" data-target="#dialog-refresh"> Upload using a new transformation </a> </div>
	<div class="col-xs-6 col-md-4"> <h4>Choose an existing transformation from the library</h4> <a type="button" class="btn btn-primary btn-raised theme-bg input-full-button" id="usetransformation" href="#"> Upload using existing transformation </a> </div>
	</div>
	
			<div id="transformationpage">
					<h2 class="dataset-label">Search transformation</h2>
					
					<form class="" id="transfsearchform" method="post">
					   <div class="form-group">
					   <div class="input-group">
					        <input type="text" id="transfsearchtext" name="transfsearchtext" class="form-control col-lg-8" placeholder="Search transformation">
					        <span class="input-group-btn">
					            <a id="transfsearchsubmitbutton" class="btn btn-default theme-bg">Search</a>
					        </span>
					    </div> 
					    </div>
					</form>
					<div id="select-group">
									<table class="table table-striped table-hover table-responsive" id="transform-table">
									<thead>
										<tr>
										  	<th></th>
								        	<th></th>
								        	<th></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									</table>
					</div>
			</div>
	</div>
</c:if>
</div>



<div id="dialog-refresh" title="" class="modal fade" tabindex="-1">
<div class="modal-dialog">
    <div class="modal-content">
    <div class="modal-header">
        <h4 class="modal-title">Refresh Transformation</h4>
      </div>
    <div class="modal-body">
		After create new transformation please refresh list.
	</div>
	
	<div class="modal-footer">
	<input type="button" value="Refresh" class="btn btn-primary btn-raised theme-bg" id="refreshbutton">
	</div>
	</div>
	</div>
</div>

	</jsp:body>
</template:genericpage>