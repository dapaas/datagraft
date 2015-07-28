<%@page import="eu.dapaas.dao.User"%>
<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page import="eu.dapaas.utils.Utils"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%!String getCookie(Cookie[] cookies, String cookieName) {
	if (cookies != null)
		for (int i = 0; i < cookies.length; i++)
			if (cookies[i].getName().equals(cookieName))
				return cookies[i].getValue();
	return null;
}
%>
<%
String isLoginEver = getCookie(request.getCookies(), "dapaas_user_login_ever");
User user = (User) session.getAttribute(SessionConstants.DAPAAS_USER);
boolean hasDataset = false;
if (user!=null){
	hasDataset = Utils.checkCatalog(user.getApiKey(), user.getApiSecret());
}
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}"></c:set>
<c:set var="isloginever" value="<%= isLoginEver%>"></c:set>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <title>DataGraft - Data and Platform as a Service brought to you by DaPaaS</title>

  <meta name="description" content="Data-and-Platform-as-a-Service">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link type="text/css" rel="stylesheet" href="${contextPath}/css/jquery.rating.css">
  <link type="text/css" rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <link type="text/css" rel="stylesheet" href="${contextPath}/css/dropzone.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-design/0.2.1/css/material-wfont.css" />
  <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Roboto:100" />
  <link rel="stylesheet" type="text/css" href="${contextPath}/css/bootstrap-custom.css" />
  <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.min.css">
  <link rel="stylesheet" type="text/css" href="css/stylish-index.css" />

  <script type="text/javascript" src="//code.jquery.com/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-design/0.2.1/js/material.min.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/script_index.js"></script>
  <script type="text/javascript" src="https://cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery.rating.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/main-dp.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery.tablesorter.min.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery-linenumbers.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/bootstrap-tokenfield.min.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/ddpservices.js"></script>
  <script type="text/javascript" src="http://malsup.github.com/jquery.form.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/dropzone.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/upload.js"></script>
  
  <link rel="stylesheet" href="${contextPath}/css/base.css">
  <link rel="stylesheet" href="${contextPath}/css/main.css">
  
  <script type="text/javascript">
    Application.contextPath = "${contextPath}";
  </script>
</head>
<body>

<div class="container">
  <div class="container-inner">
    <div class="background"></div>
    
    <div id="intro-bg-video-container">
    	
        <!-- <video id="intro-bg-video" class="fluid-width-video-wrapper" autoplay loop muted>
          <source src="images/video.mp4"  type="video/mp4">
          <source src="images/video.webm" type="video/webm">
        </video>-->
      </div>
      
      
    
	     <div class="mainsearch">
			<form class="navbar-form" id="formsearch" method="post" action="${contextPath }/pages/catalogs/index.jsp">
			   <input type="text" id="searchtext" name="searchvalue" class="form-control col-lg-8 white" placeholder="Search">
			   
			</form>
			<a href="${contextPath}/pages/catalogs/index.jsp" id="searchsubmitbutton">Explore</a>
			<a  id="singupdata" data-login="${user.username}" data-isloginever="${isloginever }" data-hasdataset="<%= hasDataset%>">Sign In</a>
	      </div>
	    
  
    
    <section id="intro" class="wrapper">   
      <h1>DataGraft</h1>
      <h2>Data-and-Platform-as-a-Service</h2>
      <h2>Brought to you by DaPaaS</h2>
      <h2><img src="${contextPath}/images/dapaas.png" alt=""></h2>
      <a id="indexscrollf" class="scrfirst"><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="36px" height="18px" viewBox="0 0 36 18" zoomAndPan="disable" preserveAspectRatio="none">
    <style type="text/css"><![CDATA[ line { stroke: #fff; stroke-width: 1; } ]]></style>
	<line x1="0" y1="0" x2="18" y2="18" />
	<line x1="36" y1="0" x2="18" y2="18" />
</svg></a>
    </section>
    
    
    <section id="images" class="wrapper" style="display: none;">
    
      <div class="images-byline">
        <h1>DataGraft</h1>
        <ul>
          <li>Manage your data in a simple, effective, and efficient way.</li>
          <li>Powerful data transformation and scalable data access capabilities for your data.</li>
        </ul>
      </div>
      
      
      
      <div class="images-byline-2">
      	<div class="row show-grid">
			  <div class="col-md-6">
					<h3>One stop shop for hosted data management:</h3>
					<ul>
			          <li>Interactively build, modify and share data transformations.</li>
			          <li>Reuse transformations to repeatably clean and transform spreadsheet data.</li>
			          <li>Host and share datasets and transformations in our cloud based catalog.</li>
			        </ul>
			  </div>
			  <div class="col-md-6">
			  	<h3>Flexible management and sharing of data and transformations:</h3>
			  	<ul>
			          <li>Choose to share transformations or datasets privately or publicly.</li>
			          <li>Fork, reuse and extend transformations built by other professionals from our public catalog.</li>
			          <li>API access to public or private datasets and transformations.</li>
			        </ul>
			  </div>
		</div>
        <div class="row show-grid">
              <div class="col-md-3">
					&nbsp;
			  </div>
			  <div class="col-md-6">
					<h3>Reliable data hosting and querying services:</h3>
					<ul>
			          <li>Query Join and visualise datasets through your own SPARQL endpoint.</li>
			          <li>We scale so you don't have to.</li>
			          <li>Visualise your data with our SPARQL based chart builder.</li>
			        </ul>
			  </div>
			  <div class="col-md-3">
			  	&nbsp;
			  </div>
		</div>
      
      
      


	  </div>
      <img id="homepage" class="raw-page" src="images/home.jpg" alt="">
      <div class="iphone">
        <img class="iphone-frame" src="images/iphoneframe.png" alt="">
        <div class="iphone-viewport">
          <img id="screenshot1" class="iphone-content" src="images/screenshot1.jpg" alt="">
         
        </div>
      </div>
    </section>
    
    <section id="links" style="display: none;">
      <p class="links-byline">Sign up today and unleash DataGraft for your data!</p>
      <a class="btn-ind" id="publishmydata" data-login="${user.username}" data-isloginever="${isloginever }" data-hasdataset="<%= hasDataset%>">Start using DataGraft now</a>
    </section>
  </div>
  <div class="twitter">
    <a href="https://twitter.com/dapaasproject" class="twitter-follow-button" data-show-count="false">Follow @dapaasproject</a>
    <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
  </div>
  <div class="photoby">
  Photo by <a href="https://www.flickr.com/photos/jorneriksson/" target="_blank">Jørn Eriksson</a> (<a href="https://creativecommons.org/licenses/by/2.0/" target="_blank">CC BY 2.0</a>)
  </div>
  <div class="menulink">
  	<ul class="nav nav-tabs">
	  	<li><a href="http://dapaas.github.io/api" target="_blank">API</a></li>
	  	<li><a href="http://dapaas.github.io/faq" target="_blank">FAQ</a></li>
	</ul>
  </div>
</div>




  

  
<div id="complete-dialog-signup" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h4 class="modal-title">Sign up</h4>
      </div>
      <div class="modal-body">
        <form action="${contextPath}/signup" method="POST" id="createoauth" class="sign-popup-form">
          <input type="hidden" name="signup">
          <input type="hidden" name="action" id="caction" />
          <div class="form-group">
            <label for="username" class="control-label">Username</label>
            <div class="form-control-wrapper">
              <input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username">
              <span class="material-input"></span>
            </div>
          </div>
          
          <div class="form-group">
            <label for="r-name" class="control-label">Full names</label>
            <div class="form-control-wrapper">
              <input type="text" class="form-control col-lg-8" placeholder="Full names" name="name" id="r-name" >
              <span class="material-input"></span>
            </div>
          </div>
          <div class="form-group">
            <label for="email" class="control-label">E-mail</label>
            <div class="form-control-wrapper">
              <input type="text" class="form-control col-lg-8" placeholder="e-mail" name="email" id="email">
              <span class="material-input"></span>
            </div>
          </div>
          
          <div class="form-group">
            <label class="control-label" for="pass">Password</label>
            <div class="form-control-wrapper">
              <input type="password" class="form-control col-lg-8" placeholder="Password" name="password" id="pass">
              <span class="material-input"></span>
            </div>
          </div>
          <input type="submit" value="Sign up with username" class="btn btn-material-pink btn-raised input-signup">
          <div class="login-text-social marg-top">Or sign up with:</div>
          <input type="button" value="Sign up with Facebook" class="btn btn-raised input-signup fb" id="fbcreate">
          <input type="button" value="Sign up with Google+" class="btn btn-raised input-signup gplus" id="gcreate">
          <input type="button" value="Sign up with Twitter" class="btn btn-raised input-signup tw" id="tcreate">
          <div class="login-text"> If you do have DataGraft account, please <a id="signindialog">sign in now.</a></div>
        </form>
     </div>
    </div>
  </div>
</div>

<div id="complete-dialog-signin" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h4 class="modal-title">Sign in</h4></div>
      <div class="modal-body">
        <form action="${contextPath}/login" method="POST" id="loginform" class="sign-popup-form">
          <input type="hidden" name="action" id="action">
          <div class="form-group">
          	<label for="username" class="control-label">Username</label>
          	<div class="form-control-wrapper">
          		<input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="usern">
          		<span class="material-input"></span>
            </div>
          </div>
          <div class="form-group">
	          <label for="password" class="control-label">Password</label>
	          <div class="form-control-wrapper">
	          	<input type="password" class="form-control col-lg-8" placeholder="Password" name="password" id="password">
	          	<span class="material-input"></span>
              </div>
          </div>
          <input type="submit" class="btn btn-material-pink btn-raised input-signup" value="Sign in">
          
          <div class="forgotten-wrapper"><a href="#">Forgotten password</a></div>
          <div class="login-text-social">Or Sign in with:</div>
          <input type="button" value="Sign in with Facebook" class="btn btn-raised input-signup fb" id="fblogin">
          <input type="button" value="Sign in with Google+" class="btn btn-raised input-signup gplus" id="glogin">
          <input type="button" value="Sign in with Twitter" class="btn btn-raised input-signup tw" id="tlogin">
          <div class="login-text">If you do not have DataGraft account, please <a id="signupdialog">sign up</a> now</div>
        </form>
     </div>
    </div>
  </div>
</div>

</body>
</html>