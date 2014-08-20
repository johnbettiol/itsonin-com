<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="/dynamic/en/head.jsp" %>
	<script type="text/javascript" src="/static/js/modules/list.js"></script>
	<script type="text/javascript">
		var events = ${eventsJson};
		
		$(document).ready(function() {
			EventListModule.init();
		});
	</script>
	<script id="eventTemplate" type="text/x-jsrender">
	<div class="list-group-item event-item" id="{{:eventId}}">
		<div class="media">
			<div class="pull-left">
				<img src="/static/img/party.png" class="pull-left" height="40" width="40">
			</div>
			<div class="media-body clearfix">
				<div class="media-heading event-title"><c:out value="{{:title}}"/></div>
				<div class="event-arrow pull-right" onclick="location.pathname='/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}'">
					<i class="fa fa-angle-right"></i>
				</div>
				<p class="event-offer">
					FREE VODKA FOR EVERY GOAL
				</p>
				<i class="fa fa-clock-o"></i>
				<small class="text-muted">Today 5:00 pm - 10:00pm</small>
			</div>
		</div>
	</div>
	</script>
</head>
<body>
	<div style="position:fixed;top:0;left:0;right:0;z-index:1031;">
		<div class="container" id="events_header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
				  <div class="row">
					<div class="col-sm-12 header">
						<img src="/static/img/itsonin-white.png" height="20" width="20">
						<span class="header-title">itsonin ${ioiContext.city}</span>
						<i class="fa fa-2x fa-map-marker pull-right pointer" style="margin-left: 20px" id="map-btn"></i>
						<i class="fa fa-2x fa-filter pull-right pointer" id="filter0-btn"></i>
					</div>
				  </div>
				  <div class="row map">
					  <div id="map-canvas" style="height: 100%"></div>
				  </div>
				  <div class="row filters">
						<div class="col-sm-12">
							<div class="filter-bar-section" style="margin-top: 4px;">
								<ul class="categories" id="goto-categories">
									<li id="PARTY">
										<span class="icon icon-bordered icon-party"></span>
										<span class="category-title">Party</span>
									</li>
									<li id="SOCIAL">
										<span class="icon icon-bordered icon-social"></span>
										<span class="category-title">Social</span>
									</li>
									<li id="ACADEMY">
										<span class="icon icon-bordered icon-academy"></span>
										<span class="category-title">Academy</span>
									</li>
									<li id="HOBBIES">
										<span class="icon icon-bordered icon-hobbies"></span>
										<span class="category-title">Hobbies</span>
									</li>
									<li id="SINGLES">
										<span class="icon icon-bordered icon-singles"></span>
										<span class="category-title">Singles</span>
									</li>
								</ul>
							</div>
							<div class="filter-bar-section">
								<ul class="categories" id="meet-categories">
									<li id="CONCERT">
										<span class="icon icon-bordered icon-concert"></span>
										<span class="category-title">Concert</span>
									</li>
									<li id="SPORT">
										<span class="icon icon-bordered icon-sport"></span>
										<span class="category-title">Sport</span>
									</li>
									<li id="FESTIVAL">
										<span class="icon icon-bordered icon-festival"></span>
										<span class="category-title">Festival</span>
									</li>
									<li id="CONVENTION">
										<span class="icon icon-bordered icon-convention"></span>
										<span class="category-title">Convention</span>
									</li>
								</ul>
							</div>
							<div class="filter-bar-section">
								<div class="row">
									<div class="col-sm-12">
										<div class="btn-group btn-group-justified">
											<div class="btn-group">
											  <button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-star"></span>
											  </button>
										  	</div>
										  	<div class="btn-group">
										  		<button class="btn btn-default" type="button" id="happening-now-btn">Now</button>
										  	</div>
										  	<div class="btn-group">
												<button class="btn btn-default" type="button">
													<span class="glyphicon glyphicon-calendar"></span>
												</button>
										  	</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container" id="events">
	  <div class="row">
		<div class="col-sm-offset-3 col-sm-6 events-container">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title">
						<span>TOMORROW AUGUST 25</span>
						<a href="/${ioiContext.locale}/${ioiContext.city}/e/add" class="btn btn-default btn-xs pull-right">
							<span class="glyphicon glyphicon-plus"></span>
						</a>
					</div>
				</div>
				<div class="list-group" id="list-container">
					<c:forEach var="event" items="${events}" varStatus="status">
						<div class="list-group-item event-item" id="${event.eventId}">
							<div class="media">
								<div class="pull-left">
									<span style="display: inline-block;height: 100%;vertical-align: middle;"></span>
									<img src="/static/img/party.png" height="40" width="40" style="vertical-align: middle;display:inline;">
								</div>
							<!-- 								<div style="clear:both"></div>-->
								<div class="media-body clearfix">
									<div class="media-heading event-title"><c:out value="${event.title}"/></div>
									<div class="event-arrow pull-right" onclick="location.pathname='/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}'">
										<i class="fa fa-angle-right"></i>
									</div>
									<p class="event-offer">
										FREE VODKA FOR EVERY GOAL
									</p>
									<i class="fa fa-clock-o"></i>
									<small class="text-muted">Today 5:00 pm - 10:00pm</small>
								</div>
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