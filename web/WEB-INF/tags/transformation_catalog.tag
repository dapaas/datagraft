<%@tag import="eu.dapaas.utils.Config"%>
<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="templateData" type="java.util.List"%>
<%@attribute description="table empty label" name="emptylabel"%>
<%@attribute description="table id" name="id"%>
<%@attribute description="all footer" name="footer"%>
<%@attribute description="all sizepage" name="sizepage"%>
<%@attribute description="table page" name="page"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form id="selecttransformation" action="${contextPath }/pages/transformations/details.jsp" method="get">
	<input type="hidden" id="id" name="id"/>
</form>

<table id="${id}" data-page="${page}"class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">Transformation</th>
			<th class="theme-bg">Date</th>
			<th class="theme-bg">Description</th>
			<th class="theme-bg">Actions</th>
		</tr>
	</thead>
	<tbody>
		
		<c:forEach items="${templateData}" var="data">
		
			<fmt:parseDate value="${data.issued}" pattern="yyyy-MM-dd" var="issuedDate"/>
			<fmt:formatDate var="todayString" value="${today}" pattern="yyyy-MM-dd" />
			<fmt:formatDate var="issuedString" value="${issuedDate}" pattern="yyyy-MM-dd" />
			<fmt:formatDate var="issuedDateString" value="${issuedDate}" pattern="d MMM"/>
			<fmt:formatDate var="yesterdayString" value="${yesterday}" pattern="yyyy-MM-dd" />
			
			<tr id="${data.id}">
  				<td class="first-row">${data.title}</td>
  				<td class="tableDate" data-toggle="tooltip" data-container="body" title="<fmt:formatDate value="${issuedDate}" pattern="d MMM yyyy"/>" data-sort="${issuedString}">
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
  				
  				<td>
  				 <c:if test="${fn:length(data.description)<=30}" > ${data.description}
  				 </c:if>
  				 <c:if test="${fn:length(data.description)>30}" > 
  				 ${fn:substring(data.description, 0, 30)} ...
  				 </c:if>
  				</td>
  				
					<td class="tableAction" id="${data.id}">
					<div class="right">
						
						<c:url value="/pages/transformations" var="dwnUrl" scope="request">
							<c:param name="id" value="${data.id}"></c:param>
						 </c:url>
						 <a href="${dwnUrl}" class="aimg" title="Edit">
						 <i class="mdi-content-create theme-text"></i>
						 </a> 
						 <c:url value="/pages/myassets" var="delUrl" scope="request">
							  <c:param name="id" value="${data.id}"/>
							  <c:param name="delete" value="transformation"/>
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
<c:if test="${not empty templateData and sizepage>1}" >
<ul class="pager">
    <li class="previous ${(page == 1)? 'disabled' : ''}" ><a class="theme-text ${(page == 1)? 'disabled' : ''}" id="pageTransformationLess">← Previous</a></li>
    <li class="next ${(page == sizepage)? 'disabled' : ''}" ><a class="theme-text ${(page == sizepage)? 'disabled' : ''}" id="pageTransformationAdd" >Next →</a></li>
</ul>
</c:if>
<c:if test="${not empty footer &&  not empty templateData}">
<p>${footer} </p>
</c:if>
<c:if test="${empty templateData}" >
	<p>${emptylabel}</p>
</c:if>