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

String updateaccount = request.getParameter("updateaccount");
if (!Utils.isEmpty(updateaccount)){
  userBean.updateDetail(request, response, session);
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
		<h2 >User management</h2>
		
	    	
	    	
	    	<form  method="POST" id="createoauth" class="sign-popup-form">
		
				<input type="hidden" name="signup">
				<input type="hidden" name="action" id="action" />
				
				
				<div class="form-group">
			 			<label for="username" class="control-label">Username</label>
			 			
                        <div class="form-control-wrapper">
			 			<input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username" value="${ userdetail.username}">
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
			 			<input type="text" class="form-control col-lg-8" placeholder="e-mail" name="email" id="email" value="${ userdetail.email}" required="required">
			 			<span class="material-input"></span>
                        </div>
                       
			 		</div>
	  				<input type="submit" id="updateaccount" name="updateaccount" value="Save" class="btn btn-material-pink btn-raised input-signup">
	  				<!-- <input type="button" value="Change Password" class="btn btn-primary btn-raised theme-bg input-signup"> -->
	    	</form>
	    
	    </div>
	</jsp:body>
</template:genericpage>