<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<template:genericpage title="DataGraft | Contact us">
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
	<div class="container well">
		<h2>Contact us</h2>
		<p>Thank you for your interest in DataGraft. We’d love to hear from you.</p>
		<p>Please send us an email and let us know what we can do for you, and we will contact you promptly.</p>
		<form method="post" class="form-horizontal">
			<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
				<div class="form-group">
					<label for="r-name" class="col-lg-2 control-label">Name</label>
					<div class="col-lg-10">
	                    <div class="form-control-wrapper">
				 			<input type="text" class="form-control empty" placeholder="Name" name="name" id="r-name" required="required">
				 			<span class="material-input"></span>
	                    </div>
	                </div>
				</div>
				<div class="form-group">
					<label for="r-email" class="col-lg-2 control-label">E-mail</label>
					<div class="col-lg-10">
	                   <div class="form-control-wrapper">
				 			<input type="email" class="form-control empty" placeholder="E-mail" name="email" id="r-email" required="required">
				 			<span class="material-input"></span>
	                    </div>
	                 </div>
				</div>
				
				<div class="form-group">
					<label for="r-mess" class="col-lg-2 control-label">Message</label>
					<div class="col-lg-10">
	                  <div class="form-control-wrapper">
				 			<textarea class="form-control empty" name="message" id="r-mess" required="required"></textarea>
				 			<span class="material-input"></span>
	                   </div>
	                </div>
				</div>
				<input type="submit" value="Send" class="btn btn-material-pink btn-raised input-signup">
			</div>
		</form>
		</div>
	</jsp:body>
</template:genericpage>