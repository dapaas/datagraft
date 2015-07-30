<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}"></c:set>
<c:if test="${empty user }">
          <li><a href="${contextPath }/pages/catalogs/index.jsp">Explore </a></li>
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Sign In</a>
          <ul class="dropdown-menu"><li>
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
              <div class="login-text">If you do not have DataGraft account, please <a class="theme-text" href="${contextPath}/pages/register.jsp">sign up</a> now</div>
            </form>
          </li></ul>
</c:if><c:if test="${not empty user }">
          <li><a href="${contextPath}/pages/catalogs/index.jsp">Explore</a></li>
          <li><a href="${contextPath}/pages/myassets/index.jsp">Dashboard</a></li>
          <li><a href="${contextPath}/pages/publish/index.jsp">Publish</a></li>
          <li><a href="${contextPath}/pages/transformations/index.jsp">Transform</a></li>
          
          <li class="dropdown">
                <a href="#" data-target="#" class="dropdown-toggle" data-toggle="dropdown">${user.name } <b class="caret"></b></a>
                <ul class="dropdown-menu menu-right theme-bg">
                	 <li><a class="theme-bg" href="${contextPath}/pages/keymanager/index.jsp">API Key manager</a></li>
                	 <li><a class="theme-bg" href="http://dapaas.github.io/documentation">Help</a></li>
                    <li><a class="theme-bg" href="${contextPath}/pages/logout.jsp">Logout</a></li>
                </ul>
            </li>
          
</c:if>
