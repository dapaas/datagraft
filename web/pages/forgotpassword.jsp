<%@page import="eu.dapaas.utils.Utils"%>
<%@page import="eu.dapaas.utils.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<%
String error = "";
String action = request.getParameter("action");
if (!Utils.isEmpty(action) && action.equals("forgotpass")){
	try{
	  userbean.requestPasswordReset(request.getParameter("email"));
	  error = "A link to reset password has been send to your email.";
	}catch(Exception e){
	  error = e.getMessage();
	}
}
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:remove var="wizard"/>
<c:set var="error" value="<%=error %>"/>

<template:genericpage title="DataGraft Signup">
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
		<h2 >Forgot password</h2>
		
	    	<p class="errormessage"> ${error} </p>
	    	
	    	<form method="POST" id="createoauth" class="sign-popup-form">
		
				<input type="hidden" name="action" id="action" value="forgotpass"/>
				
				
				
			 		
			 		<div class="form-group">
			 			<label for="email" class="control-label">E-mail</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="email" class="form-control col-lg-8" placeholder="e-mail" name="email" id="email" required="required">
			 			<span class="material-input"></span>
                        </div>
                       
			 		</div>
			 		
			 		
			 		
	  				<input type="submit" value="Request for password reset" class="btn btn-material-pink btn-raised input-signup">
	    	</form>
	    
	    </div>
	</jsp:body>
</template:genericpage>