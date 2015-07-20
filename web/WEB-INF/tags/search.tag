<%@ tag description="Dataset template" pageEncoding="UTF-8"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form class="navbar-form navbar-right" id="formsearch" method="post" action="${contextPath }/pages/catalogs/index.jsp">
   <div class="form-group">
   <div class="input-group">
        <input type="text" id="searchtext" name="searchvalue" class="form-control col-lg-8" placeholder="Search">
        
    </div> 
    </div>
</form>