<%@page import="eu.dapaas.utils.Config"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<jsp:useBean id="transformationbean" class="eu.dapaas.bean.TransformationBean"/>

<jsp:setProperty name="transformationbean" property="response" value="${pageContext.response}"/>
<jsp:setProperty name="transformationbean" property="session" value="${pageContext.session}"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }

<c:set var="transformation" value="${transformationbean.getDetail(param['id']) }" />


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
			<!--ul class="breadcrumb">
    			<li><a class="theme-text" href="${contextPath }/pages/catalogs">Explore</a></li>

    			<li class="active">Transformation</li>
			</ul-->


				<h1 class="add-label">Preview ${transformation.title}  </h1>



			<div id="content"  class="well">
<h2 class="dataset-label">Transformation properties</h2>

  		<div class="form-horizontal">
								<div class="form-group">
                                        <label for="datasetname" class="col-lg-2 control-label">Name:</label>
                                        <div class="col-lg-10">
                                            <div class="form-control-wrapper">
                                            <label id="datasetname" class="form-control empty" >${transformation.title} </label>

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
				<label  class="form-control empty">
					${transformation.publisher }
				</label>
			</div>
			</div>
		</div>

		<div class="form-group">
			<label class="col-lg-2 control-label" for="description">Creation Date:</label>
			 <div class="col-lg-10">
             <div class="form-control-wrapper">
				<label  class="form-control empty">

					<fmt:parseDate value="${transformation.issued}" pattern="yyyy-MM-dd" var="issuedDate"/>
					<fmt:formatDate value="${issuedDate }" pattern="d MMM yyyy"/>



				</label>
			</div>
			</div>
		</div>

		<c:set var="kw" value="" />
		<c:forEach items="${transformation.keyword}" var="k" >
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



</div>

</div>







	</jsp:body>
</template:genericpage>