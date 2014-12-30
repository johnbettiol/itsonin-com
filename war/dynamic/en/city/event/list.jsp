<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page import="org.joda.time.format.DateTimeFormat" %>
<%@ page import="com.itsonin.web.IoiRouterContext" %>
<%@ page session="false" %>
<% pageContext.setAttribute("now", new org.joda.time.DateTime()); %>

<!DOCTYPE html>
<html>
<head>
	<title>itsonin - Events in ${ioiContext.city}</title>
	<%@ include file="/dynamic/en/head.jsp" %>
</head>
<body>
	<div id="view">
		<script type="text/javascript">
			$(document).ready(function() {
				var events = ${eventsJson};
				EventListModule.init(events);
			});
		</script>
		<script id="eventTemplate" type="text/x-jsrender">
		<div class="list-group-item event-item">
			<a href="/${ioiContext.locale}/${ioiContext.city}/e/{{:eventId}}" class="event-link"  id="event-{{:eventId}}">
			<div class="media">
				<div class="event-icon">
					<span class="fa fa-university fa-2x"></span>
				</div>
				<div class="media-body clearfix event-body">
					<div class="media-heading event-title">{{:title}}</div>
					{{if offerRef}}
						<p class="event-offer">
							{{:offer}}
						</p>
					{{/if}}
					<div class="event-time text-muted">
						<i class="fa fa-clock-o"></i>
						{{if ~showDate()==false}}{{:~formatTime(startTime)}}{{if endTime}} - {{/if}}{{:~formatTime(endTime)}}{{/if}}
						{{if ~showDate()==true}}{{:~formatDate(startTime)}}{{if endTime}} - {{/if}}{{:~formatDate(endTime)}}{{/if}}
					</div>
				</div>
			</div>
			</a>
		</div>
		</script>
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
								<button id="filter-btn" <c:if test="${(fn:length(ioiContext.categoryFilter) > 0) || (ioiContext.dateFilter=='Favourites') ||(ioiContext.dateFilter=='Offers') || (ioiContext.dateFilter=='Hot')}">class="active"</c:if>>
									<i class="fa fa-2x fa-filter"></i>
								</button>
							</div>
						</div>
						<div class="row map">
							<div id="map-canvas"></div>
						</div>
						<div class="row filters" <c:if test="${(fn:length(ioiContext.categoryFilter) > 0) || (ioiContext.dateFilter=='Favourites') ||(ioiContext.dateFilter=='Offers') || (ioiContext.dateFilter=='Hot')}">style="display:block"</c:if>>
							<div class="btn-group btn-group-justified" id="filter-categories">
								<div class="btn-group">
									<a href="${categoryUrls.NIGHTLIFE}"
									class="btn mob-btn <c:if test="${fn:contains(ioiContext.categoryFilter, 'NIGHTLIFE')}">selected</c:if>" id="Nightlife">
										<span class="fa fa-glass"></span>
										<span class="block">NIGHTLIFE</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="${categoryUrls.SOCIAL}"
									class="btn mob-btn <c:if test="${fn:contains(ioiContext.categoryFilter, 'SOCIAL')}">selected</c:if>" id="Social" >
										<span class="fa fa-users"></span>
										<span class="block">SOCIAL</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="${categoryUrls.CULTURAL}"
									class="btn mob-btn <c:if test="${fn:contains(ioiContext.categoryFilter, 'CULTURAL')}">selected</c:if>" id="Cultural">
										<span class="fa fa-university"></span>
										<span class="block">CULTURAL</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="${categoryUrls.FESTIVAL}"
									class="btn mob-btn <c:if test="${fn:contains(ioiContext.categoryFilter, 'FESTIVAL')}">selected</c:if>" id="Festival">
										<span class="fa fa-ticket"></span>
										<span class="block">FESTIVAL</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="${categoryUrls.SPORT}"
									class="btn mob-btn <c:if test="${fn:contains(ioiContext.categoryFilter, 'SPORT')}">selected</c:if>" id="Sport">
										<span class="fa fa-futbol-o"></span>
										<span class="block">SPORT</span>
									</a>
								</div>
							</div>

							<div class="btn-group btn-group-justified" id="filter-buttons">
								<div class="btn-group">
									<a href="/${ioiContext.locale}/${ioiContext.city}/Events/Favourites" 
										class="btn mob-btn <c:if test="${ioiContext.dateFilter == 'Favourites'}">selected</c:if>" id="Favourites-button">
										<span class="fa fa-star"></span>
										<span class="block">FAVOURITES</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="/${ioiContext.locale}/${ioiContext.city}/Events/Offers" 
										class="btn mob-btn <c:if test="${ioiContext.dateFilter == 'Offers'}">selected</c:if>" id="Offers-button">
										<span class="fa fa-dollar"></span>
										<span class="block">PROMO</span>
									</a>
								</div>
								<div class="btn-group">
									<a href="/${ioiContext.locale}/${ioiContext.city}/Events/Hot" 
										class="btn mob-btn <c:if test="${ioiContext.dateFilter == 'Hot'}">selected</c:if>" id="Hot-button">
										<span class="fa fa-fire"></span>
										<span class="block">HOT!</span>
									</a>
								</div>
							</div>
						</div>
						<div class="row date">
							<div class="col-xs-12">
								<span class="pointer" id="filter-date"><c:out value="${prettyDate}"/></span>
								<a href="/${ioiContext.locale}/${ioiContext.city}/Events/Yesterday" id="prev-day-button" class="pull-left" 
									<c:if test="${(ioiContext.dateFilter=='Favourites') ||(ioiContext.dateFilter=='Offers') || (ioiContext.dateFilter=='Hot')}">style="display:none"</c:if>>
									<i class="fa fa-angle-left"></i>
								</a>
								<a href="/${ioiContext.locale}/${ioiContext.city}/Events/Tomorrow" id="next-day-button" class="pull-right"
									<c:if test="${(ioiContext.dateFilter=='Favourites') ||(ioiContext.dateFilter=='Offers') || (ioiContext.dateFilter=='Hot')}">style="display:none"</c:if>>
									<i class="fa fa-angle-right"></i>
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="container" id="events">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6 events-container hidden" <c:if test="${(fn:length(ioiContext.categoryFilter) > 0) || (ioiContext.dateFilter=='Favourites') ||(ioiContext.dateFilter=='Offers') || (ioiContext.dateFilter=='Hot')}">style="margin-top: 200px;"</c:if>>
					<div class="panel panel-default">
						<div class="list-group" id="list-container">
							<c:forEach var="event" items="${events}" varStatus="status">
								<div class="list-group-item event-item">
									<a href="/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}" class="event-link"  id="event-${event.eventId}">
										<div class="media">
											<div class="event-icon">
												<span class="fa fa-university fa-2x"></span>
											</div>
											<div class="media-body clearfix event-body">
												<div class="media-heading event-title"><c:out value="${event.title}"/></div>
												<c:if test="${not empty event.offerRef}">
													<p class="event-offer">
														<c:out value="${event.offer}"/>
													</p>
												</c:if>
												<div class="event-time text-muted">
													<i class="fa fa-clock-o fs-11"></i>
													<fmt:formatDate type="time" pattern="HH:mm" value="${event.startTime}"/>
													<c:if test="${not empty event.endTime}"> - </c:if>
													<fmt:formatDate type="time" pattern="HH:mm" value="${event.endTime}"/>
												</div>
											</div>
										</div>
									</a>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="free-space"></div>
	</div>
</body>
</html>