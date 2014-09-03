<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page session="false" %>
<% pageContext.setAttribute("now", new org.joda.time.DateTime()); %>

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
			<div class="event-icon">
				<span class="fa fa-university fa-2x m-b-xs"></span>
			</div>
			<div class="media-body clearfix event-body">
				<div class="media-heading event-title">{{:title}}</div>
				<p class="event-offer">
					{{:offer}}
				</p>
				<div class="event-time text-muted">
					<i class="fa fa-clock-o"></i>
					{{if !favourite}}{{:~formatTime(startTime)}}{{if endTime}} - {{/if}}{{:~formatTime(endTime)}}{{/if}}
					{{if favourite}}{{:~formatDate(startTime)}}{{if endTime}} - {{/if}}{{:~formatDate(endTime)}}{{/if}}
				</div>
				<i class="fa fa-angle-right event-arrow pointer"
					onclick="location.pathname='/${ioiContext.locale}/${ioiContext.city}/e/{{:eventId}}'"></i>
			</div>
		</div>
	</div>
	</script>
</head>
<body>
	<div class="header-container">
		<div class="container" id="events-header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
				  <div class="row">
					<div class="col-sm-12 header">
						<img src="/static/img/itsonin-white.png" height="20" width="20">
						<i class="fa fa-angle-right v-a-m fs-18"></i>
						<span class="header-title v-a-m">${ioiContext.city}</span>
						<i class="fa fa-2x fa-map-marker pull-right pointer" id="map-btn"></i>
						<i class="fa fa-2x fa-filter pull-right pointer" id="filter-btn"></i>
						<a href="/${ioiContext.locale}/${ioiContext.city}/e/add" class="pull-right" id="plus-link">
							<i class="fa fa-2x fa-plus"></i>
						</a>
					</div>
				  </div>
				  <div class="row map">
					  <div id="map-canvas"></div>
				  </div>
				  <div class="row filters">
						<div class="col-sm-12">
							<div id="filter-categories" class="hbox text-center b-b b-light text-sm">
								<a href="javascript:void(0)" class="col padder-v text-muted" id="NIGHTLIFE">
									<span class="fa fa-glass fa-2x m-b-xs"></span>
									<span class="block">NIGHTLIFE</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="SOCIAL">
									<span class="fa fa-users fa-2x m-b-xs"></span>
									<span class="block">SOCIAL</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="CULTURAL">
									<span class="fa fa-university fa-2x m-b-xs"></span>
									<span class="block">CULTURAL</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="FESTIVAL">
									<span class="fa fa-ticket fa-2x m-b-xs"></span>
									<span class="block">FESTIVAL</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="SPORT">
									<span class="fa fa-futbol-o fa-2x m-b-xs"></span>
									<span class="block">SPORT</span>
								</a>
							</div>
							<div id="filter-buttons" class="hbox text-center text-sm mt-8">
								<a href="javascript:void(0)" class="col padder-v text-muted" id="favourites-button"> 
									<i class="fa fa-star fa-2x m-b-xs"></i>
									<span class="block">FAVOURITES</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="promo-button"> 
									<i class="fa fa-dollar fa-2x m-b-xs"></i>
									<span class="block">PROMO</span>
								</a>
								<a href="javascript:void(0)" class="col padder-v text-muted" id="calendar-button"> 
									<i class="fa fa-fire fa-2x m-b-xs"></i>
									<span class="block">HOT!</span>
								</a>
							</div>
						</div>
					</div>
					<div class="row date">
						<div class="col-sm-12">
							<i class="fa fa-angle-left pointer pull-left" id="prev-day-button"></i>
							<span class="pointer" id="filter-date">TODAY <joda:format value="${now}" pattern="EEEE MMMM d" locale="en"/></span>
							<i class="fa fa-angle-right pointer pull-right" id="next-day-button"></i>
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
				<div class="list-group" id="list-container">
					<c:forEach var="event" items="${events}" varStatus="status">
						<div class="list-group-item event-item" id="${event.eventId}">
							<div class="media">
								<div class="event-icon">
									<span class="fa fa-university fa-2x m-b-xs"></span>
								</div>
								<div class="media-body clearfix event-body">
									<div class="media-heading event-title"><c:out value="${event.title}"/></div>
									<p class="event-offer">
										<c:out value="${event.offer}"/>
									</p>
									<div class="event-time text-muted">
										<i class="fa fa-clock-o fs-11"></i>
										<fmt:formatDate type="time" pattern="hh:mm a" value="${event.startTime}"/>
										<c:if test="${not empty event.endTime}"> - </c:if>
										<fmt:formatDate type="time" pattern="hh:mm a" value="${event.endTime}"/>
									</div>
									<i class="fa fa-angle-right event-arrow pointer" 
										onclick="location.pathname='/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}'"></i>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	  </div>
	</div>
	<div id="free-space"></div>
</body>
</html>