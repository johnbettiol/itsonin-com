<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
	<div class="header-container">
		<div class="container" id="events_header">
			<div class="row">
				<div class="col-sm-offset-3 col-sm-6">
				  <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 header">
								<img src="/static/img/itsonin-white.png" height="20" width="20">
								<i class="fa fa-angle-right v-a-m fs-18"></i>
								<a href="/${ioiContext.locale}/${ioiContext.city}/Events">
									<span class="header-title v-a-m">${ioiContext.city}</span>
								</a>
								<i class="fa fa-angle-right v-a-m fs-18"></i>
								<span class="header-title v-a-m">Add Event</span>
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
						<input type="text" class="form-control" id="guest-name-field">
						<label>Event title</label>
						<input type="text" class="form-control" id="title-field" name="title-field">
						<label>Event description</label>
						<textarea class="form-control" id="description-field" name="description-field"></textarea>
						<label>Offer</label>
						<input type="text" class="form-control" id="offer-field" name="offer-field">
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event category</label>
						<div id="categories" class="hbox text-center b-b b-light text-sm">
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
						<c:forEach var="eventCategory" items="${eventCategories}" varStatus="loop">
							<div class="subcategories" id="category-${eventCategory}" style="display: none;">
								<div style="margin-bottom: 5px; font-size:12px;">
									<i>Please select one of the following</i>
								</div>
								<div class="hbox text-center b-b b-light text-sm">
									<c:forEach var="eventSubCategory" items="${eventSubCategories}" varStatus="loop">
										<c:if test="${eventSubCategory.parent == eventCategory}">
											<a href="javascript:void(0)" class="col padder-v text-muted" id="${eventSubCategory}">
												<span class="fa fa-glass fa-2x m-b-xs"></span>
												<span class="block">${eventSubCategory}</span>
											</a>
										</c:if>
									</c:forEach>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-xs-12">
						<label>Event location</label>
						<input type="text" class="form-control" id="location-title-field" placeholder="Enter a location">
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12" style="height: 200px; width: 100%; padding-top: 10px;padding-bottom: 10px">
						<div id="map-canvas" style="height: 100%; width: 100%"></div>
					</div>
				</div>
				<div class="row" style="display:none">
					<div class="col-xs-12">
						<label>Location description</label>
						<textarea class="form-control" id="location-description-field" name="loction-description"></textarea>
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
										<input type="text" class="form-control" id="dateFrom-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="timeFrom-field"/> 
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
										<input type="text" class="form-control" id="dateTo-field"/> 
										<span class="input-group-btn">
											<button class="btn btn-default" type="button">
												<span class="glyphicon glyphicon-calendar"></span>
											</button>
										</span>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="input-group">
										<input type="text" class="form-control" id="timeTo-field"/> 
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
									<label> <input type="radio" id="visibility-field" name="visibility-field" value="PUBLIC"> Public </label>
								</div>
								<div class="radio">
									<label> <input type="radio" id="visibility-field" name="visibility-field" value="PRIVATE" checked> Private </label>
								</div>
								<div class="radio">
									<label> <input type="radio" id="visibility-field" name="visibility-field" value="FRIENDS" disabled> <strike>Friends only</strike> </label>
								</div>
							</div>
							<div class="col-xs-6">
								<i style="font-size: 12px">Sharing</i>
								<div class="radio">
									<label> <input type="radio" id="sharability-field" name="sharability-field" value="NOSHARE"> No share </label>
								</div>
								<div class="radio">
									<label> <input type="radio" id="sharability-field" name="sharability-field" value="NORMAL" checked> Normal </label>
								</div>
								<div class="radio">
									<label> <input type="radio" id="sharability-field" name="sharability-field" value="PYRAMID"> Pyramid </label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="success-alert" style="display:none">
					<div class="col-xs-12">
				        <div class="alert alert-success alert-dismissable">
			            	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							<span id="success-text"></span>
			            	<a href="/${ioiContext.locale}/${ioiContext.city}/Events" class="btn btn-default btn-sm">go back</a>
	            		</div>
	            	</div>
	            </div>
	            <div class="row" id="error-alert" style="display:none">
					<div class="col-xs-12">
			            <div class="alert alert-danger alert-dismissable">
			              <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			              <span id="error-text"></span>
			            </div>
	            	</div>
	            </div>
				<div class="row">
					<div class="col-xs-12">
						<div class="btn-container pull-right">
							<a href="/${ioiContext.locale}/${ioiContext.city}/Events" class="btn btn-default">Cancel</a>
							<button class="btn btn-default" id="save-btn">Save</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>