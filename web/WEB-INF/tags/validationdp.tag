<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:set var="wizard" value="${sessionScope.wizard}"></c:set>
<c:if test="${not empty wizard and not empty wizard.details.id and not empty param['id'] and wizard.details.id != param['id']}">
  <c:set var="error"  value="Cannot edit multiple data pages in the same session." scope="session" ></c:set>
  <jsp:forward page="/pages/error"></jsp:forward>
</c:if>
<c:if test="${not empty wizard and wizard.action=='new' and not empty param['id']}">
<c:set var="error"  value="Cannot edit multiple data pages in the same session." scope="session" ></c:set>
  <jsp:forward page="/pages/error"></jsp:forward>
</c:if>