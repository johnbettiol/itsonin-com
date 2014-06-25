<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin</title>
	<script type="text/javascript" src="/static/js/modules/about.js"></script>
	<%@ include file="../head.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            AboutModule.init();
        });
    </script>
</head>
<body>
  <div id="wrap">
    <div class="container">
      <div class="row row-main">
        <div class="col-sm-offset-3 col-sm-6 col-md-offset-3 col-md-6 col-lg-offset-3 col-lg-6">
          	<div class="row text-center">
				<h2>
					<a ng-href="/${ioiDestination}">It's On In</a>
				</h2>
			</div>
			
			<div class="video">
				<iframe src="//www.youtube.com/embed/d-diB65scQU?rel=0" frameborder="0" allowfullscreen></iframe>
			</div>
			<div class="text-center">
				<a ng-href="/${ioiDestination}" class="btn btn-default">Return</a>
			</div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>