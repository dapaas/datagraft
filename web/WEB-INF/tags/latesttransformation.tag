<%@tag import="eu.dapaas.utils.Config"%>
<%@tag description="Table template" pageEncoding="UTF-8"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute description="tHead col 1 name" name="col1"%>
<%@attribute description="tHead col 2 name" name="col2"%>
<%@attribute description="tHead col 3 name" name="col3"%>
<%@attribute description="tHead col 4 name" name="col4"%>

<%@attribute description="table label" name="label"%>
<%@attribute description="table empty label" name="emptylabel"%>
<%@attribute description="table body data" name="values"%>

<%@attribute description="table id" name="id"%>
<%@attribute name="templateData" type="java.util.List"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


 <h2>${label}</h2>
<table  id="${id}" class="table table-latest table-striped table-hover  table-responsive">
	
	<thead>
		<tr>
			<th class="header theme-bg">${col1}</th>
			<th class="hidden-xs theme-bg">${col2}</th>
			<th class="header theme-bg"><span class="hidden-xs">${col3}</span><span class="visible-xs">Date</span></th>
			
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
			<tr id="${data.id}" >
  				<td class="first-row">${data.title}</td>
  				<td class="hidden-xs">${data.publisher}</td>
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
  				
			</tr>	
		</c:forEach>
	</tbody>
</table>
<c:if test="${empty templateData}" >
	<p>${emptylabel}</p>
</c:if>
