
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<jsp:useBean id="userBean" class="eu.dapaas.bean.UserBean"/>
${userBean.logout(pageContext.request, pageContext.response, pageContext.session)}
