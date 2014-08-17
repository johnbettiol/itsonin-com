<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html>
<head>
<title>itsonin - Events in ${ioiContext.city}</title>
<%@ include file="/dynamic/en/head.jsp"%>
<script type="text/javascript" src="/static/js/modules/new.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		EventNewModule.init();
	});
</script>
</head>
<body>
	<div style="position:fixed;top:0;left:0;right:0;z-index:1031;">
		<div class="container" id="events_header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
				  <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 header">
								<a href="/${ioiContext.locale}/${ioiContext.city}/Events">
									<img src="/static/img/itsonin-white.png" height="20" width="20">
									<span class="header-title">itsonin ${ioiContext.city}</span>
								</a>
							</div>
						</div>
					</div>
				  </div>
			   </div>
			</div>
		</div>
	</div>
	<div class="container" id="event">
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 event-container">
				<div class="row">
					<div class="col-xs-12">
						<h4>ADD AN EVENT</h4>
						<label>Your name</label>
						<input type="text" class="form-control" id="guest-name">
						<label>Event title</label>
						<input type="text" class="form-control" id="title" name="title">
						<label>Event description</label>
						<textarea class="form-control" id="description" name="description"></textarea>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event category</label>
						<div style="margin-top: 5px; margin-bottom: 5px">
							<label class="radio-inline"> <input type="radio" id="category" name="category" value="MEET"> meet someone</label>
							<label class="radio-inline"> <input type="radio" id="category" name="category" value="GOTO"> goto something</label>
						</div>
						<div class="filter-bar-section" id="meet-categories" style="display: none;">
							<div style="margin-bottom: 5px; font-size:12px;">
								<i>Please select one of the following</i>
							</div>
							<ul class="categories">
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
						<div class="filter-bar-section" id="goto-categories" style="display: none;">
							<div style="margin-bottom: 5px; font-size:12px;">
								<i>Please select one of the following</i>
							</div>
							<ul class="categories">
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
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event location</label>
						<input type="text" class="form-control" id="event-location" placeholder="Enter a location">
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12" style="height: 200px; width: 100%; padding-top: 10px;padding-bottom: 10px">
						<div id="map-canvas" style="height: 100%; width: 100%"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<label>Location description</label>
						<textarea class="form-control" id="location-description" name="loction-description"></textarea>
						<div class="btn-container">
							<button class="btn btn-default" id="upload-photos-btn">Upload photos</button>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event time and date</label>
						<div>
							<label>Start date & time</label>
							<div class="row">
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="dateFrom"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="timeFrom"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-time"></span>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div>
							<label>End date & time</label>
							<div class="row">
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="dateTo"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="timeTo"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-time"></span>
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event details</label>
						<div class="row">
							<div class="col-xs-6">
								<i style="font-size: 12px">Visibility</i>
								<div class="radio">
									<label> <input type="radio" name="visibility" value="PUBLIC"> Public </label>
								</div>
								<div class="radio">
									<label> <input type="radio" name="visibility" value="PRIVATE" checked> Private </label>
								</div>
								<div class="radio">
									<label> <input type="radio" name="visibility" value="FRIENDS" disabled> <strike>Friends only</strike> </label>
								</div>
							</div>
							<div class="col-xs-6">
								<i style="font-size: 12px">Sharing</i>
								<div class="radio">
									<label> <input type="radio" name="sharability" value="NOSHARE"> No share </label>
								</div>
								<div class="radio">
									<label> <input type="radio" name="sharability" value="NORMAL" checked> Normal </label>
								</div>
								<div class="radio">
									<label> <input type="radio" name="sharability" value="PYRAMID"> Pyramid </label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="btn-container pull-right">
							<button class="btn btn-default" id="cancel-btn">Cancel</button>
							<button class="btn btn-default" type="submit">Save</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>