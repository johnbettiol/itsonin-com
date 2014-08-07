var EventListModule = {

	filteredEvents: [],
	filter: {
			types: [],
			startTime: moment().set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0)
	},
	map: {},
	markers: [],
	showMap: false,
	showMapOnTop: false,

	init: function() {
		var self = this;

		//preload sprite
		$('<img/>').attr('src', '/static/img/sprites.png').appendTo('body').css('display','none');
		//init map
		$('#map-canvas').width($('.list-container').width() + 30);
		self.initMap();

		$('#map-btn').on('click', function() {
			self.filteredEvents = events;
			$(".list-filters").hide();

			if(self.showMap == true) {
				$('.list-map').css('visibility', 'hidden');
				$('.list-map').css('position', 'absolute');
				$('.list-container').css('margin-top', '');
			} else {
				$('.list-map').css('visibility', 'inherit');
				$('.list-map').css('position', 'fixed');
				$('.list-container').css('margin-top', '250px');
			}
			//self.initMap();
			//google.maps.event.trigger(self.map, 'resize');
			//self.map.setCenter(new google.maps.LatLng(51.227741, 6.773456));
			self.showMap = !self.showMap;
		});

		$('#filter0-btn').on('click', function() {
			self.showMap = false;
			$('.list-map').css("visibility", "hidden");
			$('.list-map').css("position", "absolute");
			$(".list-filters").toggle();

			if($('.list-filters').is(':hidden') == true) {
				$('.list-container').css('margin-top', '');
			} else {
				$('.list-container').css('margin-top', '250px');
			}
			self.renderEvents(events);
		});

		$('#my-events-btn').on('click', function() {
			$('.list-search-bar').addClass('hidden');
			$('.list-filters').addClass('hidden');
		});

		$('#search-btn').on('click', function() {
			$('.list-search-bar').removeClass('hidden');
			$('.list-filters').addClass('hidden');

			$('#search-input').val('');
			self.renderEvents(events);
		});

		$('#filter-btn').on('click', function() {
			$('.list-filters').removeClass('hidden');
			$('.list-search-bar').addClass('hidden');
		});

		$('#search-events-btn').on('click', function() {
			self.filterEventsByText($('#search-input').val());
		});

		$('#happening-now-btn').on('click', function() {
			self.filterNowEvents();
		});

		$('.list-categories li').on('click', function() {
			$('.list-categories li').each(function (i) {
				$(this).removeClass('active');
			});
			$(this).addClass('active');
			self.filterEvents('subCategory', $(this).attr('id'));
		});

		$("#date-field").datetimepicker({format: 'yyyy-mm-dd hh:ii', autoclose: true});
		//TODO: use this one https://github.com/xdan/datetimepicker or this https://github.com/dbushell/Pikaday

		$(document).on('touchmove', self.handleScroll);

		$(document).on('scroll', self.handleScroll);

		$.views.helpers({
			formatDate: function (val) {
				return moment(val).format('h:mm')
			}
		});
	},

	filterEvents: function(field, value) {
		filteredEvents = $.grep(events, function(e, i) {
			return value === e[field];
		});
		this.renderEvents(filteredEvents);
	},

	filterEventsByText: function(text) {
		filteredEvents = $.grep(events, function(e, i) {
   			return  e.title.toLowerCase().indexOf(text.toLowerCase()) > -1
	   	});
	   	this.renderEvents(filteredEvents);
	},

	filterNowEvents: function() {
		var now = new Date();
		filteredEvents = $.grep(events, function(e, i) {
   			return e.startTime < now && e.endTime > now;
	   	});
	   	this.renderEvents(filteredEvents);
	},

	loadEvents: function(params, callback) {
		$.get('/api/event/list', params, callback);
	},

	renderEvents: function(events) {
		$('#list-container .col-sm-12').html($('#eventTemplate').render(events));
	},

	initMap: function(){
		var self = this;
		var mapOptions = {
				zoom: 10,
				center: new google.maps.LatLng(51.227741, 6.773456),
				disableDefaultUI: true,
				panControl: false,
				zoomControl: true,
				zoomControlOptions: {
					position: google.maps.ControlPosition.LEFT_CENTER
				},
				scaleControl: false,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			  }
		self.map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

		for(var i=0; i<events.length; i++) {
			var marker = self.addMarker(events[i], i);
			self.markers.push(marker);
			events[i]['__gm_id'] = marker['__gm_id']
		}

		zoomChangeListener = google.maps.event.addListener(self.map, 'zoom_changed', function (event) {

		});

		zoomChangeListener = google.maps.event.addListener(self.map, 'dragend', function (event) {
			//console.log('dragend')
		});

		var timeout;
		zoomChangeBoundsListener = google.maps.event.addListener(self.map,'bounds_changed',function (event) {  
			//google.maps.event.removeListener(zoomChangeBoundsListener);
			/*window.clearTimeout(timeout);
			timeout = window.setTimeout(function () {
				...
			}, 500);*/
		});

		google.maps.event.addListener(self.map, 'idle', function() {
			//http://stackoverflow.com/questions/4338490/google-map-event-bounds-changed-triggered-multiple-times-when-dragging

			if(EventListModule.showMap == true) {
				var bounds = self.map.getBounds();

				self.filteredEvents = [];
				for(var i = 0; i < self.markers.length; i++){
					var marker = self.markers[i];
					if(bounds.contains(marker.position)) {
						for(var j = 0; j < events.length; j++) {
							if(marker['__gm_id'] == events[j]['__gm_id']) {
								self.filteredEvents.push(events[j]);
							}
						}
					}
				}
			   	self.renderEvents(self.filteredEvents);
			}
		});

		var markerCluster = new MarkerClusterer(self.map, self.markers);
	},

	addMarker: function (event, index) {
		var self = this;
		var icon = new google.maps.MarkerImage('/static/img/marker/icon_' + index + '.png', new google.maps.Size(32,32))
		var marker= new google.maps.Marker({
			position:  new google.maps.LatLng(event.gpsLat, event.gpsLong),
			icon: icon,
			map: self.map,
			title: event.title});

		google.maps.event.addListener(marker, "click", function() {
			//self.map.panTo(marker.getPosition());
			self.scroll(event.eventId);
		});

		return marker;
	},

	handleScroll: function() {return;
		if($(window).width() <= 640){
			if($(window).scrollTop() > 0) {
				if(EventListModule.showMap == true && EventListModule.showMapOnTop == false) {
					$('.list-map').css({
						'width': '100%',
						'position': 'fixed',
						'top': '50px',
						'z-index': 10
					});
					/*$('.list-container').css({
						'margin-top': '200px'//180
					});*/
				}
				EventListModule.showMapOnTop = true;
			} else {
				if(EventListModule.showMap == true) {
					/*$('.list-container').css({
						'margin-top': ''
					});*/
					$('.list-map').css({
						'width': '',
						'position': '',
						'top': ''
					});

				}
				EventListModule.showMapOnTop = false;
			}
		}
	},

	scroll: function (id) {
		$('body,html').stop().animate({
			scrollTop: $("#event-"+id).position().top
		});
	}
};