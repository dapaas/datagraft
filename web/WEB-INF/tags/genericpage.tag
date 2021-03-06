<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true"%>
<%@attribute name="footer" fragment="true"%>
<%@attribute name="search" fragment="true"%>
<%@attribute name="navbar" fragment="true"%>
<%@attribute name="table" fragment="true"%>
<%@attribute name="title"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="icon" type="image/png" href="https://datagraft.net/static/images/favicon.png" >
  <title>${title}</title>
  <link type="text/css" rel="stylesheet" href="${contextPath}/css/jquery.rating.css">
  <link type="text/css" rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <link type="text/css" rel="stylesheet" href="${contextPath}/css/dropzone.css">
  <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" />
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-material-design/0.2.1/css/material-wfont.css" />
  <link rel='stylesheet' type='text/css' href='//fonts.googleapis.com/css?family=Roboto:100|Montserrat:400'/>
  <link rel='stylesheet' type='text/css' href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.min.css">
  <link rel='stylesheet' type='text/css' href="${contextPath}/css/bootstrap-tokenfield.min.css"/>
  <link rel='stylesheet' type='text/css' href="${contextPath}/css/tokenfield-typeahead.min.css"/>
  <link rel='stylesheet' type='text/css' href="${contextPath}/css/bootstrap-custom.css"/>
  <link rel='stylesheet' type='text/css' href="${contextPath}/css/sinchanges.css"/>
  
  
  
  <script type="text/javascript" src="//code.jquery.com/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <script type="text/javascript" src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-material-design/0.2.1/js/material.min.js"></script>
  <script type="text/javascript" src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
  <script type="text/javascript" src="//malsup.github.io/jquery.form.js"></script>
  
  <script type="text/javascript" src="${contextPath}/scripts/jquery.rating.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery.tablesorter.min.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery-linenumbers.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/bootstrap-tokenfield.min.js"></script>
  
  <script type="text/javascript" src="${contextPath}/scripts/spin.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/jquery.spin.js"></script>

  
  <script type="text/javascript" src="${contextPath}/scripts/ddpservices.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/dropzone.js"></script>
  
  <script type="text/javascript" src="${contextPath}/scripts/main-dp.js"></script>
  <script type="text/javascript" src="${contextPath}/scripts/upload.js"></script>
  
  
  <jsp:invoke fragment="header" />
  <script type="text/javascript">
    Application.contextPath = "${contextPath}";
  </script>
</head>
<body>
  <header class="navbar navbar-inverse theme-bg">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${contextPath}/" title="Data and Platform as a Service">DataGraft</a>
      </div>
      <div class="navbar-collapse collapse navbar-responsive-collapse">
        <ul class="nav navbar-nav navbar-right">
          <jsp:invoke fragment="navbar" />
        </ul>
        <jsp:invoke fragment="search" />
      </div>
      <div></div>
    </div>
  </header>
  <div id="body" class="container">
    <jsp:doBody />
  </div>
  <footer class="navbar navbar-inverse theme-bg" role="navigation">
    <nav class="container">
      <div class="navbar-header">
        <button class="navbar-toggle" data-toggle="collapse" data-target=".navFooterCollapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
      </div>
      <div class="navbar-collapse collapse navbar-responsive-collapse">
        <ul class="nav navbar-nav navbar-left">
          <li><a href="/documentation/">Documentation</a></li>
          <li><a href="/api/">API</a></li>
          <li><a href="/faq/">FAQ</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li><a href="/terms-of-use/">Terms of use</a></li>
          <li><a href="/privacy-policy/">Privacy policy</a></li>
          <li><a href="/cookie-policy/">Cookie policy</a></li>
          <li><a href="/contact/">Contact</a></li>
          <li><a href="/feedback/"><i class="mdi-communication-messenger"></i> 	Feedback</a></li>
        </ul>
      </div>
      <p class="navbar-link">DataGraft is a service operated by the <a href="//dapaas.eu">DaPaaS</a> project, co-funded by the EC under 7th Framework Programme (FP7 2007-2013)</p>
    </nav>
  </footer>
</body>
</html>
