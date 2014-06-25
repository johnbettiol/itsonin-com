<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="../../head.jsp" %>
	<script type="text/javascript" src="/static/js/modules/list.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
        	EventListModule.init();
        });
    </script>
</head>
<body>
  <div id="wrap">
    <div class="container">
      <div class="row row-main">
        <div class="col-sm-offset-3 col-sm-6 col-md-offset-3 col-md-6 col-lg-offset-3 col-lg-6">
        	<div class="row text-right">
        		<a href="/${ioiContext.locale}/${ioiContext.city}/Events" class="btn btn-default btn-xs">Events</a>
        		<a href="/${ioiContext.locale}/${ioiContext.city}/MyEvents" class="btn btn-default btn-xs">My events</a>
        		<a href="/about" class="btn btn-default btn-xs">About</a>
        	</div>
	        <div class="row text-center">
				<h2>
					<a href="/about">It's On In</a>
				</h2>
			</div>
			<div class="row">
				<div class="col-md-6 col-lg-6 search-panel">
					<div>
						<div class="input-group">
							<input type="text" class="form-control" ng-model="location" /> 
								<span class="input-group-btn">
								<button class="btn btn-default" type="button">
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</span>
						</div>
						<div style="display: table-cell;padding-left:5px">
							<a href="/${ioiContext.locale}/${ioiContext.city}/e/add" class="btn btn-default pull-right">
								<span class="glyphicon glyphicon-plus"></span>
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<label>Happening:</label>
				<button type="button" id="filter-now-button" class="btn btn-default btn-sm">Now</button>
				<button type="button" id="filter-tomorrow-button" class="btn btn-default btn-sm">Tomorrow</button>
				<input size="16" type="text" class="form_datetime">
			</div>
			<div class="row">
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="toolbar toolbar-event">
							<ul class="event-categories">
								<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
									<li>
										<a href="javascript:void(0)" class="event-category" id="${eventCategory}">
											<span class="glyphicon glyphicon-picture"></span>
											<span><c:out value="${eventCategory}"/></span>
										</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
				<div class="row">
					<div class="panel panel-default" style="display:none" id="event-subcategories-${eventCategory}">
						<div class="panel-body">
							<div class="toolbar toolbar-event">
								<ul class="event-categories">
									<c:forEach var="eventSubCategory" items="${eventSubCategories}">
										<c:if test="${eventSubCategory.parent == eventCategory}">
											<li <c:if test="${event.subCategory == eventSubCategory}">class="active"</c:if>>
												<a href="javascript:void(0)" class="event-subcategory" id="${eventSubCategory}">
													<span class="glyphicon glyphicon-picture"></span>
													<span><c:out value="${eventSubCategory}"/></span>
												</a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
	        <div class="row">
	    		<c:forEach var="event" items="${events}">
		    		<div class="panel panel-default">
		    			<div class="panel-body">
		    				<span class="glyphicon glyphicon-picture pull-left"></span>
		    				<div class="fields pull-left">
		    					<span class="field bold"><c:out value="${event.title}"/></span>
		    					<span class="field italic"><c:out value="${event.locationTitle}"/></span>
		    					<span class="field"><c:out value="${event.locationAddress}"/></span>
		    				</div>
		    				<a href="/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}" class="pull-right">View</a>
		    			</div>
		    		</div>
	    		</c:forEach>
	        </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>