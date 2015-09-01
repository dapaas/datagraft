<%@page import="eu.dapaas.utils.Utils"%>
<%@page import="eu.dapaas.dao.User"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page import="eu.dapaas.utils.Config"%>
<%@page import="eu.dapaas.constants.AuthenticationProvider"%>
<%@page import="com.neurologic.oauth.util.Globals"%>
<%@page import="eu.dapaas.constants.AuthenticationProvider"%>
<%@page import="com.neurologic.oauth.config.ModuleConfig"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="userBean" class="eu.dapaas.bean.UserBean"/>
<template:validation />

<%
User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);
if (user == null){
  response.sendRedirect("");
}
if (AuthenticationProvider.facebook.equals(user.getProvider())){
  response.sendRedirect("http://www.facebook.com/"+user.getProviderId());
  return;
}

if (AuthenticationProvider.google.equals(user.getProvider())){
  response.sendRedirect("https://plus.google.com/"+user.getProviderId()+"/posts");
  return;
}

if (AuthenticationProvider.twitter.equals(user.getProvider())){
  response.sendRedirect("https://twitter.com/@"+user.getName());
  return;
}

String action = request.getParameter("action");
if (!Utils.isEmpty(action) && action.equals("update")){
  userBean.updateDetail(request, response, session);
  response.sendRedirect("myassets");
  return;
}
%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:remove var="wizard"/>
<c:set var="userdetail" value="${userBean.getDetail(pageContext.request, pageContext.response, pageContext.session)}" />
<template:genericpage title="DataGraft User Management">
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
		
		<div class="myaccount-form">
		<h2 class="dataset-label">User details</h2>
	    	<form  method="POST" id="createoauth" class="sign-popup-form" >
		
				<input type="hidden" name="signup">
				<input type="hidden" name="action" id="action" />
				
				
				<div class="form-group">
			 			<label for="username" class="control-label">Username</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username" value="${ userdetail.username}" required="required">
			 			<span class="material-input"></span>
                        </div>
                        
			 		</div>
					<div class="form-group">
						<label for="r-name" class="control-label">Full names</label>
						
                        <div class="form-control-wrapper">
			 			<input type="text" class="form-control col-lg-8" placeholder="Full names" name="name" id="r-name" value="${ userdetail.name}">
			 			<span class="material-input"></span>
                        </div>
                        
			 		</div>
			 		
			 		<div class="form-group">
			 			<label for="email" class="control-label">E-mail</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="email" class="form-control col-lg-8" placeholder="e-mail" name="email" id="email" value="${ userdetail.email}" required="required">
			 			<span class="material-input"></span>
                        </div>
                       
			 		</div>
	    	</form>
	    	<div class="publish-div">
	  			<input type="button" id="updateaccount" name="updateaccount" value="Save" class="btn btn-primary btn-raised publish-button theme-bg">
	  		</div>
	    </div>	
	    
	    <c:if test="${user.confirm }">
		    <div class="myaccount-form">	
		    <h2 class="dataset-label">Email confirmation</h2>
		    <div class="form-horizontal">
		    	<p class="sign-popup-form emailmessage">Your email is not confirmed! Please click button bellow and we send new confirmation email to you!</p>
		    	<div class="publish-div">
		  			<input type="button" id="emailconfirm" name="emailconfirm" value="Send email" class="btn btn-primary btn-raised publish-button theme-bg">
		  		</div>
		    </div>
		    </div>
	    </c:if>
	    
	   <div class="myaccount-form">
	   <h2 class="dataset-label">Change password</h2>
	   <p class="errormessage"> </p>
	   <form  method="POST" id="changepasswordform" class="sign-popup-form">
	   				<div class="form-group">
			 			<label for="username" class="control-label">Old password</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="password" class="form-control col-lg-8" placeholder="Old password" name="oldpassword" id="oldpassword" >
			 			<span class="material-input"></span>
                        </div>
                        
			 		</div>
			 		<div class="form-group">
			 			<label for="username" class="control-label">New password</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="password" class="form-control col-lg-8" placeholder="New password" name="newpassword" id="newpassword" >
			 			<span class="material-input"></span>
                        </div>
                        
			 		</div>
			 		<div class="form-group">
			 			<label for="username" class="control-label">Confirm new password</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="password" class="form-control col-lg-8" placeholder="New password" name="confirmpassword" id="confirmpassword" >
			 			<span class="material-input"></span>
                        </div>
                        
			 		</div>
	   </form>
	   <div class="publish-div">
	  		<input type="button" id="changepassword" name="changepassword" value="Change Password" class="btn btn-primary btn-raised publish-button theme-bg">
	   </div>
	   </div>
	    </div>
	</jsp:body>
</template:genericpage>