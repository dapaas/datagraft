<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="catalogdetails" class="eu.dapaas.bean.DatasetBean"/>
<jsp:useBean id="poligonbean" class="eu.dapaas.bean.PoligonBean"/>

<jsp:setProperty name="catalogdetails" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="catalogdetails" property="session" value="${pageContext.session}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />



<c:if test="${not empty param['id'] && param['a'] == 'e'}" >
<!-- edit -->
<c:set var="poligon" value="${poligonbean.getPoligon(param['id']) }" />
</c:if>
<c:if test="${not empty param['id'] && param['a'] == 'd'}" >
<!-- delete -->
${poligonbean.deletePoligon(param['id']) }
</c:if>

<c:set var="poligons" value="${catalogdetails.getPoligons() }" />


<template:validation/>
<template:genericpage title="DataGraft Admin">

<jsp:attribute name="navbar">
	
</jsp:attribute>
	<jsp:attribute name="footer">
		<template:footer />
	</jsp:attribute>
	<jsp:attribute name="search">
		<template:search />
	</jsp:attribute>
	<jsp:body>
	
	<div class="container">
	<h1 class="add-label">Upload your poligon data</h1>
	
	<form  action="${contextPath }/admin" class="form-horizontal" method="post" enctype="multipart/form-data">
	<input type="hidden" id="id" name="id" value="${param['id'] }" />
	<div class="form-group">
        <label for="title" class="col-lg-2 control-label">Title</label>
        	<div class="col-lg-10">
        		<div class="form-control-wrapper">
        			<input id="title" name="title" type="text" class="form-control" data-ripple-color="#F0F0F0" value="${poligon.title }"/>
        			<span class="material-input"></span>
        		</div>
        	</div>
        </div>
        
        
   
         <div class="form-group">
            <label for="inputFile" class="col-lg-2 control-label">File</label>
            <div class="col-lg-10">
                <input type="text" readonly="" class="form-control floating-label" placeholder="Browse..." value="${poligon.filename }">
                <input type="file" id="inputFile" name="inputFile" multiple="" >
            </div>
        </div>
        
        
        <div class="form-group">
	        <div class="col-lg-10">
	        	<div class="form-control-wrapper">
	        	
	        		<button type="submit" class="btn btn-primary btn-raised theme-bg" id="adminpoligonaction">${(empty param['id'] )? 'Create': 'Update' }</button>
	        	</div>
	        </div>
        </div>
	</form>
	<!-- list with poligons -->
	
	
	<table id="${id}" class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">ID</th>
			<th class="theme-bg">Title</th>
			<th class="theme-bg">Filename</th>
			<th class="theme-bg">Actions</th>
		</tr>
	</thead>
	<tbody>
		
		<c:forEach items="${poligons}" var="data">
			
			<tr id="${data.id}">
  				<td class="first-row">${data.id}</td>
  				<td> ${data.title}</td>
  				<td> ${data.filename}</td>
  				
					<td class="tableAction" id="${data.id}">
					<div class="right">
						
						 <c:url value="/pages/admin/upload.jsp" var="editUrl" scope="request">
							<c:param name="id" value="${data.id}"></c:param>
							<c:param name="a" value="e"></c:param>
						 </c:url>
						 <a href="${editUrl}" class="aimg" title="Edit">
						 <i class="mdi-content-create theme-text"></i>
						 </a> 
						 <c:url value="/pages/admin/upload.jsp" var="delUrl" scope="request">
							<c:param name="id" value="${data.id}"></c:param>
							<c:param name="a" value="d"></c:param>
						 </c:url>
	           			 <a class="confirmation aimg" href="${delUrl }" title="Delete">
	           			 	<i class="mdi-content-remove-circle-outline theme-text"></i>
	           			 </a>
           			</div>
					</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

	</div>
	<div id="dialog-confirm" title="Delete?"></div>
	</jsp:body>
	
</template:genericpage>