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
	<span>{{:~formatDate(startTime)}} - {{:~formatDate(endTime)}}</span>
	<div class="panel panel-default list-event-item" id="event-{{:eventId}}">
		<div class="panel-body">
			<img src="/static/img/party.png" class="pull-left" height="60" width="60">
			<div class="list-event-body">
				<span class="list-event-title">{{:title}}</span>
				<span class="list-event-offer">FREE BEER FOR EVERY GOAL</span>
			</div>
		</div>
	</div>
	</script>
</head>
<body>
    <div class="container">
      <div class="row">
        <div class="col-sm-offset-3 col-sm-6 list-container">
			<div class="row">
                <div class="col-sm-12 list-header">
                	<img src="/static/img/itsonin-white.png" height="20" width="20">
                    <span class="list-header-title">itsonin ${ioiContext.city}</span>
                    <i class="fa fa-2x fa-map-marker pull-right pointer" id="map-btn"></i>
                    <i class="fa fa-2x fa-filter pull-right pointer" id="filter0-btn"></i>
                </div>
            </div>
	        <div class="row list-map">
	            <div id="map-canvas" style="height: 100%"></div>
	        </div>
            <div class="row list-filter-bar" style="display:none"><!--  style="margin-top: 200px" -->
                <div class="col-sm-12">
	                <div class="btn-group btn-group-justified">
						<div class="btn-group">
							<button type="button" class="btn btn-default" id="my-events-btn">
								<span class="glyphicon glyphicon-star"></span>
							</button>
						</div>
						<div class="btn-group">
						    <button type="button" class="btn btn-default" id="search-btn">
						    	<span class="glyphicon glyphicon-search"></span>
							</button>
						</div>
						<div class="btn-group">
						    <button type="button" class="btn btn-default" id="filter-btn">
						    	<span class="glyphicon glyphicon-filter"></span>
							</button>
						</div>
					</div>
                </div>
            </div>
            <div class="row list-filters" style="display:none">
	            <div class="col-sm-12">
	            	<div class="list-filter-bar-section">
	                    <ul class="list-categories" id="list-goto-categories">
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
	            	<div class="list-filter-bar-section">
	                    <ul class="list-categories" id="list-meet-categories">
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
	            	<div class="list-filter-bar-section">
	                    <button class="btn btn-default pull-left" type="button" id="happening-now-btn">
							<span class="glyphicon glyphicon-star"></span>
						</button>
	                    <button class="btn btn-default pull-left" type="button" id="happening-now-btn">Now</button>
	                    <div class="input-group inline">
		                    <input type="text" class="form-control" id="date-field"/> 
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">
									<span class="glyphicon glyphicon-calendar"></span>
								</button>
							</span>
						</div>
					</div>
	            </div>
            </div>
			<div class="row list-search-bar hidden">
				<div class="col-xs-12 col-sm-12 col-md-12">
					<div class="input-group">
						<input type="text" class="form-control" placeholder="Search Events" id="search-input"/> 
						<span class="input-group-btn">
							<button class="btn btn-default" type="button" id="search-events-btn">
								<span class="glyphicon glyphicon-search"></span>
							</button>
						</span>
					</div>
				</div>
			</div>
			<div class="row list-date-bar">
                <div class="col-sm-12">
                    <span>TODAY MAY 20</span>
                    <a href="/${ioiContext.locale}/${ioiContext.city}/e/add" class="btn btn-default pull-right">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
                </div>
            </div>
	        <div class="row" id="list-container">
	        	<div class="col-sm-12">
	    			<c:forEach var="event" items="${events}">
		    			<span><fmt:formatDate type="time" timeStyle="short" value="${event.startTime}"/> - 
		    			<fmt:formatDate type="time" timeStyle="short" value="${event.endTime}"/></span>
			    		<div class="panel panel-default list-event-item" id="event-${event.eventId}" onclick="location.pathname='/${ioiContext.locale}/${ioiContext.city}/e/${event.eventId}'">
			    			<div class="panel-body">
			    				<img src="/static/img/party.png" class="pull-left" height="60" width="60">
			    				<div class="list-event-body">
			    					<span class="list-event-title"><c:out value="${event.title}"/></span>
			    					<!-- <span class="list-event-description"><c:out value="${event.description}"/></span> -->
	                                <span class="list-event-offer">FREE BEER FOR EVERY GOAL</span>
	                                <!-- 
	                                <div class="list-event-props-line1">
		                                <i class="fa fa-lg fa-map-marker"></i> 
		                                <c:out value="${event.locationTitle}"/>
		                                <span class="badge badge-border-blue margin-left-8">1 km</span>
		                                <b class="margin-left-8"></b>
	                                </div>
	                                <div class="hidden-sm hidden-md hidden-lg pull-left">
		                            	<i class="fa fa-lg fa-user pull-left"></i> 
		                            	<i class="fa fa-lg fa-star pull-left margin-left-8"></i> 
		                                <span class="icon icon-pyramid pull-left margin-left-8"></span>
		                                <span class="badge badge-blue margin-left-8">2</span>
		                            </div>
			                        <div class="list-event-icons hidden-xs text-center">
			                            <span class="badge badge-blue">22</span>
			                            <i class="fa fa-lg fa-star margin-top-14 block"></i> 
			                            <i class="fa fa-lg fa-user margin-top-14 block"></i> 
			                            <span class="icon icon-pyramid margin-top-14"></span>
			                        </div> -->
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