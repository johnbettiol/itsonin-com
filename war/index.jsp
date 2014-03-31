<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<html ng-app="itsonin">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>itsonin</title>

    <link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="/css/style.css?v=20140401011746">
	
	<!--[if lt IE 9]>
	    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular.js" type="text/javascript"></script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular-cookies.js" type="text/javascript"></script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular-resource.js" type="text/javascript"></script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular-route.js" type="text/javascript"></script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.15/angular-sanitize.js" type="text/javascript"></script>
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.js" type="text/javascript"></script>
	<script src="/lib/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="/js/all.js?v=20140401011746" type="text/javascript"></script>
  </head>

  <body>
	  <div id="wrap">
	    <div class="container">
	      <div class="row">
	        <div class="col-sm-offset-3 col-sm-6 col-md-offset-3 col-md-6 col-lg-offset-3 col-lg-6">
	          <div ng-view></div>
	        </div>
	      </div>
	    </div>
	  </div>
	  <script type="text/javascript" 
	  src="http://maps.google.com/maps/api/js?sensor=true&libraries=places&language=en-US"></script>
  </body>
</html>
