<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html ng-app="hotel.staff">
<head>
    <c:set var="req" value="${pageContext.request}"/>
    <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}"/>
    <base href="${baseURL}/">
    <meta charset="UTF-8">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,700&subset=latin,latin-ext' rel='stylesheet'
          type='text/css'>
    <link rel="stylesheet" href="public/css/bootstrap.min.css">
    <link rel="stylesheet" href="public/common/common.css">
    <link rel="stylesheet" href="public/staff/main.css">
    <title>jHotel Staff</title>
</head>
<body>
<div class="wrapper">

    <nav class="navbar navbar-inverse" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="#">jHotel staff</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li ui-sref-active="active"><a ui-sref="reservations">Foglalások</a></li>
                    <li ui-sref-active="active"><a ui-sref="purchase">Terhelés szobára</a></li>
                    <li ui-sref-active="active"><a ui-sref="rooms">Szobák</a></li>
                </ul>


            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
    <div class="panel" ui-view></div>
</div>

<script src="public/script/bower_components/jquery/dist/jquery.min.js"></script>
<script src="public/script/bower_components/lodash/dist/lodash.min.js"></script>
<script src="public/script/bower_components/angular/angular.min.js"></script>
<script src="public/script/bower_components/restangular/dist/restangular.min.js"></script>
<script src="public/script/bower_components/angular-bootstrap/locale_hu.js"></script>
<script src="public/script/bower_components/angular-bootstrap/ui-bootstrap.js"></script>
<script src="public/script/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
<script src="public/script/bower_components/angular-local-storage/angular-local-storage.min.js"></script>
<script src="public/script/bower_components/angular-ui-router/release/angular-ui-router.min.js"></script>
<script src="public/common/common.js"></script>
<script src="public/staff/script/app.js"></script>
<script src="public/staff/script/controllers.js"></script>
</body>
</html>