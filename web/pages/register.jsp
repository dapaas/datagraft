<%@page import="eu.dapaas.utils.Config"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String action = request.getParameter("action");
if (action!=null && action.equals("submitFB")) {
   
    return;
  }
  
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:remove var="wizard"/>

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

<script type="text/javascript">

Application.state="${state}";

</script>
		<div class="container well">
		<h2 >Sign up</h2>
		
	    	
	    	
	    	<form action="${contextPath}/signup" method="POST" id="createoauth" class="sign-popup-form">
			<div class="form-horizontal">
				<input type="hidden" name="signup">
				<input type="hidden" name="action" id="caction" />
				
				
				<div class="form-group">
			 			<label for="username" class="col-lg-2 control-label">Username</label>
			 			<div class="col-lg-10">
                        <div class="form-control-wrapper">
			 			<input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username" required="required">
			 			<span class="material-input"></span>
                        </div>
                        </div>
                        
			 		</div>
					<div class="form-group">
						<label for="r-name" class="col-lg-2 control-label">Name</label>
						<div class="col-lg-10">
                        <div class="form-control-wrapper">
			 			<input type="text" class="form-control col-lg-8" placeholder="Name" name="name" id="r-name" >
			 			<span class="material-input"></span>
                        </div>
                        </div>
			 		</div>
			 		
			 		<div class="form-group">
			 			<label for="email" class="col-lg-2 control-label">E-mail</label>
			 			<div class="col-lg-10">
                        <div class="form-control-wrapper">
			 			<input type="email" class="form-control col-lg-8" placeholder="e-mail" name="email" id="email" required="required">
			 			<span class="material-input"></span>
                        </div>
                       </div>
			 		</div>
			 		
			 		
			 		
			 		<div class="form-group">
			 			<label class="col-lg-2 control-label" for="pass">Password</label>
			 			<div class="col-lg-10">
                        <div class="form-control-wrapper">
			 			<input type="password" class="form-control col-lg-8" placeholder="Password" name="password" id="pass" required="required">
			 			<span class="material-input"></span>
                        </div>
                        </div>
			 		</div>
			 		<div class="col-lg-2"></div>
					<div class="col-lg-10"><p style="margin-top:2em;margin-bottom:2em">By signing up, I agree with DataGraft.net <a href="https://datagraft.net/terms-of-use/" target="_blank">Terms of Use</a> and <a href="https://datagraft.net/privacy-policy/" target="_blank">Privacy Policy</a>.</p></div>
					<div class="col-lg-4"></div>
	  				<div class="col-lg-4"><input type="submit" value="Sign up with username" class="btn btn-material-pink btn-raised input-signup"></div>
	  				<div class="col-lg-4"></div>
	  				
	  				<div class="col-lg-12"><div class="login-text-social marg-top">Or sign up with:</div></div>
  					
						
				 		<div class="col-lg-4"><input type="button" value="Sign up with Facebook" class="btn btn-raised input-signup fb" id="fbcreate"></div>
						<div class="col-lg-4"><input type="button" value="Sign up with Google+" class="btn btn-raised input-signup gplus" id="gcreate"></div>
						<div class="col-lg-4"><input type="button" value="Sign up with Twitter" class="btn btn-raised input-signup tw" id="tcreate"></div>
					
	    		
	    	</div>	
	    	</form>
	    
	    </div>
	</jsp:body>
</template:genericpage>
