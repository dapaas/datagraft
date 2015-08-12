<%@tag import="eu.dapaas.utils.Config"%>
<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="templateData" type="java.util.List"%>
<%@attribute description="table empty label" name="emptylabel"%>
<%@attribute description="table id" name="id"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form id="selectdataset" action="${contextPath }/pages/transformation/details.jsp" method="get">
	<input type="hidden" id="id" name="id"/>
</form>

<table id="${id}" class="table table-striped table-hover  table-responsive">
	<thead class="table-head">
		<tr>
			<th class="theme-bg">Transformation</th>
			<th class="theme-bg">Description</th>
			<th class="theme-bg">Actions</th>
		</tr>
	</thead>
	<tbody>
		
		<c:forEach items="${templateData}" var="data">
			
			<tr id="${data.id}">
  				<td class="first-row">${data.title}</td>
  				<td>
  				 <c:if test="${fn:length(data.description)<=150}" > ${data.description}
  				 </c:if>
  				 <c:if test="${fn:length(data.description)>150}" > 
  				 ${fn:substring(data.description, 0, 150)} ...
  				 </c:if>
  				</td>
  				
					<td class="tableAction" id="${data.id}">
					<div class="right">
						<a id="previewdetail" href="" class="aimg" title="Apply transformation">
						 	<i class="mdi-hardware-keyboard-arrow-right theme-text"></i>
						 </a>
						<c:url value="/pages/transformations" var="dwnUrl" scope="request">
							<c:param name="id" value="${data.id}"></c:param>
						 </c:url>
						 <a href="${dwnUrl}" class="aimg" title="Edit">
						 <i class="mdi-content-create theme-text"></i>
						 </a> 
	           			 <a id="deleteda" class="confirmation aimg" href="javascript:deleteTransformation('${data.id }')" title="Delete">
	           			 	<i class="mdi-content-remove-circle-outline theme-text"></i>
	           			 </a>
           			</div>
					</td>
				
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${empty templateData}" >
	<p>${emptylabel}</p>
</c:if>