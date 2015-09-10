<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}"></c:set>
<c:if test="${empty user }">
          <li><a href="${contextPath }/pages/catalogs">Explore </a></li>
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Sign In</a>
          <ul class="dropdown-menu"><li>
            <form action="${contextPath}/login" method="POST" id="loginform" class="sign-popup-form">
              <input type="hidden" name="action" id="action">
              <input type="text" class="form-control col-lg-8" placeholder="Username" name="username" id="username" required="required">
              <input type="password" class="form-control col-lg-8" placeholder="Password" name="password" id="password" required="required">
              <input type="submit" class="btn btn-material-pink btn-raised input-signup" value="Sign In with username">
              <div class="forgotten-wrapper"><a class="theme-text" href="${contextPath}/pages/forgotpassword">Forgotten password</a></div>
              <div class="login-text-social">Or Sign in with:</div>
              <input type="button" value="Sign in with Facebook" class="btn btn-raised input-signup fb" id="fblogin">
              <input type="button" value="Sign in with Google+" class="btn btn-raised input-signup gplus" id="glogin">
              <input type="button" value="Sign in with Twitter" class="btn btn-raised input-signup tw" id="tlogin">
              <div class="login-text">If you do not have DataGraft account, please <a class="theme-text" href="${contextPath}/pages/register">sign up</a> now</div>
            </form>
          </li></ul>
</c:if><c:if test="${not empty user }">
          <li><a href="${contextPath}/pages/catalogs">Explore</a></li>
          <li><a href="${contextPath}/pages/myassets">Dashboard</a></li>
          <li><a href="${contextPath}/pages/publish">Publish</a></li>
          <li><a href="${contextPath}/pages/transformations">Transform</a></li>
          
          <li class="dropdown">
                <a href="#" data-target="#" class="dropdown-toggle" data-toggle="dropdown">${user.name } <b class="caret"></b></a>
                <ul class="dropdown-menu menu-right theme-bg">
                	 <li><a class="theme-bg" href="${contextPath}/pages/keymanager">API Key manager</a></li>
                	 
                	 <li><a class="theme-bg" href="${contextPath}/pages/myaccount.jsp">My Account</a></li>
                    <li><a class="theme-bg" href="${contextPath}/pages/logout">Logout</a></li>
                </ul>
            </li>
          
</c:if>
