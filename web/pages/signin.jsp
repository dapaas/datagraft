<%@page import="eu.dapaas.utils.Config"%>
<%@page import="com.neurologic.oauth.util.Globals"%>
<%@page import="com.neurologic.oauth.config.ModuleConfig"%>
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
		<li><a href="${contextPath }/pages/catalogs">Explore </a></li>
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
		<h2 >Sign in</h2>
		
	    	
	    	<form action="${contextPath}/login" method="POST" id="loginform" class="sign-popup-form">
              <input type="hidden" name="action" id="action">
              <input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username">
              <input type="password" class="form-control col-lg-8" placeholder="Password" name="password" id="password">
              <input type="submit" class="btn btn-material-pink btn-raised input-signup" value="Sign in">
              <div class="forgotten-wrapper"><a class="theme-text" href="#">Forgotten password</a></div>
              <div class="login-text-social">Or Sign in with:</div>
              <input type="button" value="Sign in with Facebook" class="btn btn-raised input-signup fb" id="fblogin">
              <input type="button" value="Sign in with Google+" class="btn btn-raised input-signup gplus" id="glogin">
              <input type="button" value="Sign in with Twitter" class="btn btn-raised input-signup tw" id="tlogin">
              <div class="login-text">If you do not have DataGraft account, please <a class="theme-text" href="${contextPath}/pages/register">sign up</a> now</div>
            </form>
	    	
	    
	    </div>
	</jsp:body>
</template:genericpage>