<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html ng-app="hotel.guest">
<head>
    <c:set var="req" value="${pageContext.request}"/>
    <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}"/>
    <base href="${baseURL}/">
    <meta charset="UTF-8">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,700&subset=latin,latin-ext' rel='stylesheet'
          type='text/css'>
    <link rel="stylesheet" href="public/css/bootstrap.min.css">
    <link rel="stylesheet" href="public/guest/main.css">
    <title>jHotel</title>
</head>
<body>
<header>
    <div class="wrapper">
        <h1>jHotel</h1>

        <h2>Ahol az álmok valóra válnak</h2>
    </div>
</header>
<div ui-view class="wrapper"></div>

<footer>
    <div class="wrapper">
        &copy;jHotel, Szoftverfejlesztés J2EE alapon házi feladat
    </div>
</footer>

<script src="public/script/bower_components/jquery/dist/jquery.min.js"></script>
<script src="public/script/bower_components/lodash/dist/lodash.min.js"></script>
<script src="public/script/bower_components/angular/angular.min.js"></script>
<script src="public/script/bower_components/restangular/src/restangular.js"></script>
<script src="public/script/bower_components/angular-animate/angular-animate.min.js"></script>
<script src="public/script/bower_components/angular-bootstrap/ui-bootstrap.js"></script>
<script src="public/script/bower_components/angular-bootstrap/locale_hu.js"></script>
<script src="public/script/bower_components/angular-local-storage/angular-local-storage.min.js"></script>
<script src="public/script/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
<script src="public/script/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
<script src="public/common/common.js"></script>
<script src="public/guest/script.js"></script>
</body>
</html>