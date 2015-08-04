<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }
<c:set var="user" value="${sessionScope.dapaas_user }" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:choose >
	<c:when test="${param['a'] == 'e' }">
	${userbean.modifiedAPIKey(pageContext.request, pageContext.response,pageContext.session, param['key'], true) }
	</c:when>
	<c:when test="${param['a'] == 'd' }">
	${userbean.modifiedAPIKey(pageContext.request, pageContext.response,pageContext.session, param['key'], false) }
	</c:when>
	<c:when test="${param['a'] == 'r' }">
	${userbean.deleteAPIKey(pageContext.request, pageContext.response,pageContext.session, param['key']) }
	</c:when>
	<c:when test="${param['a'] == 'c' }">
		<c:set var="newkey" value="${userbean.cretaeAPIKey(pageContext.request, pageContext.response,pageContext.session) }" /> 
	</c:when>
</c:choose>


<c:set var="keys" value="${userbean.getAPIKeys(pageContext.request, pageContext.response,pageContext.session) }"/>
<template:validation/>
<template:genericpage title="DataGraft portal">
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
	<div class="dataset-wrapper">
	<c:set var="emptylabel1" value="No user API Keys found. <a class='theme-text'  href='${contextPath }/pages/transformations'>Create your first API Key?</a> " />
	<h2>API Keys manager</h2>		
	<table  class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">API Key</th>			
			<th class="theme-bg">Enable/Disable</th>
			<th class="theme-bg">Actions</th>
		</tr>
	</thead>
	<tbody>
		
		<c:forEach items="${keys}" var="data">
			
			<tr>
  				<td class="first-row">${data.apiKey}</td>
          		
          		<td> 
          			<c:if test="${data.enable}">
          				Enable
          			</c:if>
          			<c:if test="${not data.enable}">
          				Disable
          			</c:if>
          		</td>
					<td class="tableAction">
					<div class="right">
					<c:if test="${data.enable}">
						<c:url value="/pages/keymanager/index.jsp" var="dwnUrl" scope="request">
							  <c:param name="key" value="${data.apiKey}"/>
							  <c:param name="a" value="e"/>
						 </c:url>
					</c:if>
					 <c:if test="${not data.enable}">
						<c:url value="/pages/keymanager/index.jsp" var="dwnUrl" scope="request">
							  <c:param name="key" value="${data.apiKey}"/>
							  <c:param name="a" value="d"/>
						 </c:url>
					</c:if>
					 
					 <a href="${dwnUrl }" class="aimg" title="Eble/Disable">
					 <i class="mdi-action-autorenew theme-text"></i>
					 </a> 
					 
					 
					 
					 <c:url value="/pages/keymanager/index.jsp" var="delUrl" scope="request">
						  <c:param name="key" value="${data.apiKey}"/>
						  <c:param name="a" value="r"/>
				     </c:url>
           			 <a id="deleteda" class="confirmation aimg" href="${delUrl }" title="Delete">
           			 	<i class="mdi-content-remove-circle-outline theme-text"></i>
           			 </a>
           			</div>
					</td>
				
			</tr>
		</c:forEach>
	</tbody>
</table>
 <c:url value="/pages/keymanager/index.jsp" var="crUrl" scope="request">
	<c:param name="a" value="c"/>
</c:url>
<div>
	<input type="button" value="Create New API Key" class="btn btn-primary btn-raised theme-bg" onclick="location.href='${crUrl}'">
</div>
<c:if test="${not empty newkey}">
<h2>New API Key</h2>
<table  class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">API Key</th>			
			<th class="theme-bg">API Secret</th>
			<th class="theme-bg">Enable/Disable</th>
		</tr>
	</thead>
	<tr>
	<td class="first-row">${newkey.apiKey}</td>
    <td>${newkey.apiSecret }</td>
    <td> 
          			<c:if test="${newkey.enable}">
          				Enable
          			</c:if>
          			<c:if test="${not newkey.enable}">
          				Disable
          			</c:if>
    </td>
    </tr>
	</table>
</c:if>
<c:if test="${empty keys}" >
	<p>${emptylabel}</p>
</c:if>
			
		</div>
<div id="dialog-confirm" title="Delete?"></div>
	</jsp:body>
</template:genericpage>