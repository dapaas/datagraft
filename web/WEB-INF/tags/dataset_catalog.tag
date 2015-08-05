<%@tag import="eu.dapaas.utils.Config"%>
<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="templateData" type="java.util.List"%>
<%@ attribute name="action" type="java.lang.Boolean"%>
<%@attribute description="table empty label" name="emptylabel"%>
<%@attribute description="table id" name="id"%>
<jsp:useBean id="utils" class="eu.dapaas.utils.Utils"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form id="selectdataset" action="${contextPath }/pages/publish/details.jsp" method="get">
	<input type="hidden" id="id" name="id"/>
</form>

<table id="${id}" class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">Data page</th>
			<c:if test="${not action}">
				<th class="theme-bg">Publisher</th>
			</c:if>
			<th class="theme-bg">Date</th>
			<th class="theme-bg">Portal</th>
			<c:if test="${action}">
				<th class="theme-bg">Actions</th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:set var="today" value="<%=new java.util.Date()%>" />
		<c:set var="yesterday" value="<%=new java.util.Date(new java.util.Date().getTime() - 86400000)%>" />
		<c:forEach items="${templateData}" var="data">
			<fmt:parseDate value="${data.issued}" pattern="yyyy-MM-dd" var="issuedDate"/>
			<fmt:formatDate var="todayString" value="${today}" pattern="yyyy-MM-dd" />
			<fmt:formatDate var="issuedString" value="${issuedDate}" pattern="yyyy-MM-dd" />
			<fmt:formatDate var="issuedDateString" value="${issuedDate}" pattern="d MMM"/>
			<fmt:formatDate var="yesterdayString" value="${yesterday}" pattern="yyyy-MM-dd" />
			<tr id="${data.id}">
  				<td class="first-row">${data.title}</td>
  				<c:if test="${not action}">
          <td>${data.publisher }</td>
          </c:if>
  				<td data-toggle="tooltip" data-container="body" title="<fmt:formatDate value="${issuedDate}" pattern="d MMM yyyy"/>" data-sort="${issuedString}">
  				<c:choose>
				  <c:when test="${todayString == issuedString}">
				  Today
				  </c:when>
				  <c:when test="${yesterdayString == issuedString}">
				  Y-day
				  </c:when>
				  <c:otherwise>
				  ${issuedDateString}
				  </c:otherwise>
				</c:choose>
  				</td>
  				<td><a class='theme-text' href="${contextPath}/<%=Config.getInstance().getPortalURL()%>${data.portalParameter}">
  				  <c:if test="${not empty data.portalParameter}" >
  				  #${data.portalParameter}
  				  </c:if>
  				  </a></td>
  				<c:if test="${action}">
					<td class="tableAction">
					  <div class="right">
						 <c:url value="/pages/publish/details.jsp" var="dwnUrl" scope="request">
							  <c:param name="id" value="${data.id}"/>
							  </c:url>
						 <a href="${dwnUrl }" class="aimg" title="Edit">
						 <i class="mdi-content-create theme-text"></i>
						 </a> 
						 
						 <c:url value="/pages/myassets" var="delUrl" scope="request">
							  <c:param name="id" value="${data.id}"/>
							  <c:param name="delete" value="dataset"/>
							  </c:url>
	           			 <a id="deleteda" class="confirmation aimg" href="${delUrl }" title="Delete">
	           			 	<i class="mdi-content-remove-circle-outline theme-text"></i>
	           			 </a>
           			</div>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${empty templateData}" >
	<p>${emptylabel}</p>
</c:if>