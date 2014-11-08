<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" ng-app="itsonin">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="itsonin ${ioiContext.city} - Admin city">

    <title>itsonin ${ioiContext.city} - Admin city</title>

    <!-- BOOTSTRAP -->
    <link rel="stylesheet" type="text/css" href="/static/lib/bootstrap/css/bootstrap.css">
    
    <!-- FONT AWESOME -->
    <link rel="stylesheet" href="/static/lib/font-awesome/css/font-awesome.min.css" />

    <%-- FONT - OPEN-SANS --%>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600' rel='stylesheet' type='text/css'>

	<script type="text/javascript">
		var Config = {
			city: '${ioiContext.city}'
		};
	</script>
</head>

<body>
    <div class="navbar navbar-inverse navbar-static-top" role="navigation" navbar>
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <span class="navbar-brand"><i class="fa fa-users"></i> Itsonin</span>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li data-match-route="/events"><a href="#/events">Events</a></li>
                    <li data-match-route="/seeding"><a href="#/seeding">Seeding</a></li>
                    <li data-match-route="/users"><a href="#/users">Users</a></li>
                </ul>
            </div>
        </div>
    </div>
	<div ng-view class="container" style="font-family: Open Sans; font-size: 13px"></div>

    <!-- JQUERY -->
    <script src="/static/lib/jquery/jquery.min.js"></script>

    <!-- BOOTSTRAP -->
    <script src="/static/lib/bootstrap/js/bootstrap.js"></script>

    <!-- ANGULAR -->
    <script src="/static/lib/angular/angular.min.js"></script>
    <script src="/static/lib/angular/angular-route.min.js"></script>
    <script src="/static/lib/angular/angular-sanitize.min.js"></script>

	<!-- ANGULAR-BOOTSTRAP -->
	<script src="/static/lib/angular-bootstrap/ui-bootstrap-tpls-0.11.2.min.js"></script>

    <!-- APPLICATION SCRIPTS -->
    <script src="/static/js/admin/app.js" type="text/javascript"></script>
	<script src="/static/js/admin/views.js" type="text/javascript"></script>
	<script src="/static/js/admin/service/interceptor.js" type="text/javascript"></script>
	<script src="/static/js/admin/service/loading.js" type="text/javascript"></script>
	<script src="/static/js/admin/service/eventService.js" type="text/javascript"></script>
	<script src="/static/js/admin/service/seedingService.js" type="text/javascript"></script>
	<script src="/static/js/admin/controller/EventsController.js" type="text/javascript"></script>
	<script src="/static/js/admin/controller/SeedingController.js" type="text/javascript"></script>
	<script src="/static/js/admin/controller/UsersController.js" type="text/javascript"></script>
	<script src="/static/js/admin/controller/modals/FilterModalController.js" type="text/javascript"></script>
	<script src="/static/js/admin/directive/navbar.js" type="text/javascript"></script>

    <!--[if lt IE 9]>
    <script src="/static/lib/html5shiv/html5shiv.js"></script>
    <script src="/static/lib/respond/respond.src.js"></script>
    <![endif]-->

</body>

</html>
