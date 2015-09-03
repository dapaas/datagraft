<%@page import="eu.dapaas.constants.SessionConstants"%>
<%@page import="eu.dapaas.utils.Utils"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="transformationId" value='<%= Utils.escapeJS(request.getParameter("id")) %>' />
<c:set var="distributionId" value='<%= Utils.escapeJS(request.getParameter("distribution")) %>' />
<jsp:useBean id="userbean" class="eu.dapaas.bean.UserBean"/>
<jsp:useBean id="wizard" class="eu.dapaas.bean.Wizard" scope="session"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
${userbean.putInCookie(pageContext.request, pageContext.response, pageContext.session) }

<template:validation/>
<template:genericpage title="DataGraft Public Portal">


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
        <c:set var="basicAuth" value="${userbean.getTemporaryBasicAuth(pageContext.request, pageContext.response,pageContext.session, 'grafterizer') }"/>
        <c:set var="filename" value="${wizard.uploadesFile.file.getAbsolutePath()}" />
        <c:set var="displayFilename" value="${fn:substring(wizard.uploadesFile.file.name, 0, fn:indexOf(wizard.uploadesFile.file.name, '.'))}" />
        <c:if test="${wizard.uploadesFile.file.exists()}">
        <c:import url="file://${filename}" var="fileContent" />

        <pre id="fileContent" style="display:none"><c:out value="${fileContent}" /></pre>
        <pre id="displayFilename" style="display:none"><c:out value="${displayFilename}" /></pre>
        </c:if>
        <script async>
        $(window).load(function() {
                var graftInstance = new Grafterizer("https://grafterizer.datagraft.net", document.body)
                        .setAuthorization("${basicAuth}")
                        .go('transformations.new');

                var fileContent = document.getElementById('fileContent');
                var displayFilename = document.getElementById('displayFilename');
                if (fileContent && displayFilename) {
                        graftInstance.sendMessage({
                          message: 'upload-and-new',
                          type: 'text/csv',
                          name: displayFilename.firstChild.data,
                          distribution: fileContent.firstChild.data
                        });
                }
        });
        </script>
        <link rel='stylesheet' type='text/css' href="${contextPath}/css/grafterizer.css"/>
        <script type="text/javascript" src="${contextPath}/scripts/grafterizerPostMessage.js" async></script>
        </jsp:body>
</template:genericpage>