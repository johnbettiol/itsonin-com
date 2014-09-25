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
	<script type="text/javascript">
		var events = ${eventsJson};
    </script>
	<script type="text/javascript" src="/static/js/modules/list.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			EventListModule.init();
		});
	</script>
	<script id="eventTemplate" type="text/x-jsrender">
	<div class="list-group-item event-item" id="{{:eventId}}">
		<div class="media">
			<div class="event-icon">
				<span class="fa fa-university fa-2x"></span>
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
			</div>
			<div class="event-arrow">
				<a href="/${ioiContext.locale}/${ioiContext.city}/e/{{:eventId}}">
					<i class="fa fa-2x fa-angle-right"></i>
				</a>
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
					<div class="row header">
						<div class="header-logo pull-left">
							<a href="#">
								<img src="/static/img/itsonin-white.png" height="20" width="20">
							</a>
						</div>
						<div class="header-title pull-left">
							<i class="fa fa-angle-right"></i>
							<span>${ioiContext.city}</span>
						</div>
						<div class="header-actions pull-right">
							<a href="/${ioiContext.locale}/${ioiContext.city}/e/add" id="plus-link">
								<i class="fa fa-2x fa-plus"></i>
							</a>
							<button id="map-btn">
								<i class="fa fa-2x fa-map-marker"></i>
							</button>
							<button id="filter-btn">
								<i class="fa fa-2x fa-filter"></i>
							</button>
						</div>
					</div>
					<div class="row map">
						<div id="map-canvas"></div>
					</div>
					<div class="row filters">
						<div class="btn-group btn-group-justified" id="filter-categories">
							<div class="btn-group">
								<button class="btn mob-btn" id="NIGHTLIFE">
									<span class="fa fa-glass"></span>
									<span class="block">NIGHTLIFE</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="SOCIAL">
									<span class="fa fa-users"></span>
									<span class="block">SOCIAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="CULTURAL">
									<span class="fa fa-university"></span>
									<span class="block">CULTURAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="FESTIVAL">
									<span class="fa fa-ticket"></span>
									<span class="block">FESTIVAL</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="SPORT">
									<span class="fa fa-futbol-o"></span>
									<span class="block">SPORT</span>
								</button>
							</div>
						</div>
						<div class="btn-group btn-group-justified" id="filter-buttons">
							<div class="btn-group">
								<button class="btn mob-btn" id="favourites-button">
									<span class="fa fa-star"></span>
									<span class="block">FAVOURITES</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="promo-button">
									<span class="fa fa-dollar"></span>
									<span class="block">PROMO</span>
								</button>
							</div>
							<div class="btn-group">
								<button class="btn mob-btn" id="calendar-button">
									<span class="fa fa-fire"></span>
									<span class="block">HOT!</span>
								</button>
							</div>
						</div>
					</div>
					<div class="row date">
						<div class="col-xs-12">
							<span class="pointer" id="filter-date">TODAY <joda:format value="${now}" pattern="EEEE MMMM d" locale="en"/></span>
							<button id="prev-day-button" class="pull-left">
								<i class="fa fa-angle-left"></i>
							</button>
							<button id="next-day-button" class="pull-right">
								<i class="fa fa-angle-right"></i>
							</button>
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
										<span class="fa fa-university fa-2x"></span>
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
									</div>
									<div class="event-arrow">
										<a href="/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}" class="arrow-link">
											<i class="fa fa-2x fa-angle-right"></i>
										</a>
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