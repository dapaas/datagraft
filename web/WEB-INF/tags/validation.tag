<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.dapaas_user}"></c:set>
<c:set var="islogin" value="${userbean.loginStatus(pageContext.request, pageContext.response, pageContext.session) }" />
<c:if test="${empty user and not islogin}">
	<c:set var="error" value="User is not Sign In. Please Sign In!" scope="session" />
	<jsp:forward page="/pages/error.jsp"></jsp:forward>
</c:if>