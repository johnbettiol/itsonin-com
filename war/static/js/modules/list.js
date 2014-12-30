var EventListModule = (function() {

	var RED_MARKER = 'http://labs.google.com/ridefinder/images/mm_20_red.png';

	var events = [];
	var filteredEvents = [];
	var filteredByMapEvents = [];
	var filter = {
		type: undefined,
		categories: [],
		date: moment()
	};
	var map = {};
	var markers = [];
	var isMapShown = false;
	var highlightMarkerOnScroll = true;
	var viewsCache = {};
	var eventsCache = {};

	return {
		init: function(initialEvents) {
			var self = this;
			events = initialEvents;
			filteredEvents = initialEvents;

			self.initFilterFromPath();

			$('.mob-btn, .header-actions button, .header-actions a, header-logo a, .event-arrow a, #prev-day-button, #next-day-button').on('touchstart', function(e) {
				$(this).addClass('hover');
			}).on('touchmove', function(e) {
				$(this).removeClass('hover');
			}).on('mouseenter', function(e) {
				$(this).addClass('hover');
			}).on('mouseleave', function(e) {
				$(this).removeClass('hover');
			}).on('click', function(e) {
				$(this).removeClass('hover');
			});
//TODO: mouseup mousedown focus blur
			/*$('button').on('click', function() {
				document.activeElement.blur();
			});*/

			//init caches
			if(!viewsCache['/en/Düsseldorf/Events']) {
				viewsCache['/en/Düsseldorf/Events'] =
					$("<div>").append($.parseHTML($('#view')[0].outerHTML, document, true)).find('#view');
			}

			$('#map-btn').on('click', function() {
				filteredByMapEvents = filteredEvents;
				$(".filters").hide();
				$(this).toggleClass('active');
				if($('#filter-btn').hasClass('active')){
					$('#filter-btn').toggleClass('active');
				}

				if(isMapShown == true) {
					$('.map').css({'visibility': 'hidden', 'position': 'absolute', 'left': '-10000'});
					$('.events-container').css('margin-top', '');
				} else {
					$('.map').css({'visibility': 'inherit', 'position': 'relative', 'left': ''});
					$('.events-container').css('margin-top', '200px');
					map.panTo(new google.maps.LatLng(51.227741, 6.773456));
				}
				$(window).trigger('resize');
				
				isMapShown = !isMapShown;
			});

			$('#filter-btn').on('click', function() {
				isMapShown = false;
				$('.map').css({'visibility': 'hidden', 'position': 'absolute'});
				$('.filters').toggle();
				$(this).toggleClass('active');
				if($('#map-btn').hasClass('active')){
					$('#map-btn').toggleClass('active');
				}

				if($('.filters').css('display') == 'none') {
					$('.events-container').css('margin-top', '');
				} else {
					$('.events-container').css('margin-top', '200px');
				}
				$(window).trigger('resize');
			});

			$('#prev-day-button').on('click', function() {
				event.preventDefault();
				self.updateHash(moment(filter.date).subtract(1, 'days'), filter.categories);
			});

			$('#next-day-button').on('click', function() {
				event.preventDefault();
				self.updateHash(moment(filter.date).add('days', 1), filter.categories);
			});

			$('#filter-buttons a').on('click', function() {
				event.preventDefault();
				var type = $(this).attr('id').split('-')[0]
				if(filter.type != type) {
					self.updateHash(filter.date, filter.categories, type);
				} else {
					self.updateHash(moment(), filter.categories);
				}
			});

			$('#filter-categories a').on('click', function() {
				event.preventDefault();
				var category = $(this).attr('id');

				var categories = filter.categories.slice();
				var i = $.inArray(category, categories);
				if(i >= 0){
					categories.splice(i,1);
				} else {
					categories.push(category);
				}

				self.updateHash(filter.date, categories, filter.type);
			});

			$('#filter-date').pickadate({
				onSet: function(context) {
					self.updateHash(moment(context.select), filter.categories);
				}
			});

			$('#plus-link').on('click', function(event) {
				event.preventDefault();
				location.hash = '#!/en/Düsseldorf/e/add';
			});

			$('.event-link').on('click', function(event) {
				event.preventDefault();
				var id = $(this).attr('id').split('-')[1];
				location.hash = '#!/en/Düsseldorf/e/' + id;
			});

			$.views.helpers({
				formatTime: function (val) {
					if(val) {
						return moment(val).format('HH:mm')
					} else {
						return '';
					}
				},
				formatDate: function (val) {
					if(val) {
						return moment(val).format('MMM D, HH:mm')
					} else {
						return '';
					}
				},
				showDate: function() {
					return filter.favourites | filter.promo; //TODO
				}
			});

			moment.locale('en', {
				calendar : {
					lastDay : '[Yesterday ] MMMM D',
					sameDay : '[Today ] MMMM D',
					nextDay : '[Tomorrow ] MMMM D',
					lastWeek : 'dddd MMMM D',
					nextWeek : 'dddd MMMM D',
					sameElse : 'dddd MMMM D'
				}
			});

			self.loadGoogleScript();

			Path.map('#!/en/Düsseldorf/e/add').to(function(){
				self.loadPage('/en/Düsseldorf/e/add', '#view');
			});

			Path.map('#!/en/Düsseldorf/e/:eventId').to(function(){
				self.loadPage('/en/Düsseldorf/e/' + this.params['eventId'], '#view');
			});

			Path.map('#!/en/Düsseldorf/Events(/:date)(/:categories)(/:location)').to(function(){
				var date = this.params['date'];
				var categories = (this.params['categories'] && this.params['categories'] != 'All') ? this.params['categories'].split(',') : [];
				var location = this.params['location'];

				self.loadPage('/en/Düsseldorf/Events', '#view', function() {
					self.renderFilters(date, categories, location);
				});
			});

			Path.map('#!/').to(function(){
				if(Path.routes.previous) {
					self.initFilterFromPath();
					self.loadPage('/en/Düsseldorf/Events', '#view', function() {
						self.renderFilters(filter.date, filter.categories, filter.location);
					});
				}
			});

			if(!Path.routes.previous) {
				Path.root('#!/');
				$('.date').removeClass('hidden');
				$('.events-container').removeClass('hidden');
				eventsCache['date_' + filter.date.format('YYYY-MM-DD')] = initialEvents;
			}

			Path.listen();

			if(!Path.routes.current) {
				$('.date').removeClass('hidden');
				$('.events-container').removeClass('hidden');
				eventsCache['date_' + filter.date.format('YYYY-MM-DD')] = initialEvents;
			}

			/*$(window).on('resize', function(){
				var $lastEvent = $('#list-container').children().last();
				if($lastEvent) {
					var size = 206;
					if($('.map').css('visibility') == 'hidden' && $('.filters').css('display') == 'none') {
						size -= 120;
					}
					$('#free-space').height($(this).height() - ($lastEvent.height() + size));
				}
				$('#map-canvas').width($('#list-container').width());
			}).trigger('resize');*/

			$(window).unbind('scroll');
			$(window).scroll(function(e) {
				if(highlightMarkerOnScroll == true) {
					$('.event-item').each(function (i) {
						var isTop = self.isTopVisibleEvent($(this))
						if(isTop == true){
							self.highlightMarker($(this).attr('id'));
						}
					});
				}
			});
		},

		initFilterFromPath: function() {
			var requestChunks = location.pathname.split("/");
			if (requestChunks.length >= 5) {
				var date = requestChunks[4];
				if(date == 'Favourites' || date == 'Offers' || date == 'Hot') {
					filter.type = date;
				} else if(date == 'Yesterday') {
					filter.date = moment().subtract(1, 'days');
				} else if(date == 'Today') {
					filter.date = moment();
				} else if(date == 'Tomorrow') {
					filter.date = moment().add('days', 1);
				} else if(date.length > 0){
					filter.date = moment(date, 'YYYY-MM-DD');
				}
			} else {
				filter.date = moment(moment(), 'YYYY-MM-DD');
			}
			if (requestChunks.length >= 6) {
				var categories = requestChunks[5];
				if(categories != 'All') {
					filter.categories = categories.split(',');
					for(var i = 0; i < filter.categories.length; i++) {
						var str = filter.categories[i];
						filter.categories[i] = str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
					}
				}
			} else{
				filter.categories = [];
			}
		},

		loadPage: function(url, selector, callback) {
			var self = this;
			self.showSpinner();

			if(!viewsCache[url]) {
				$.get(url, 'html').done(function(responseText) {
					self.hideSpinner();
					viewsCache[url] = $("<div>").append($.parseHTML(responseText, document, true)).find(selector);
					$('body').html(viewsCache[url]);
					if(callback && typeof callback == 'function') {
						callback();
					}
				}).fail(function(jqXHR, textStatus) {
					self.hideSpinner();
					alert( "Request failed: " + textStatus );
				});
			} else {
				$('body').html(viewsCache[url]);
				self.hideSpinner();
				if(callback && typeof callback == 'function') {
					callback();
				}
			}
		},

		getFilter: function() {
			return filter;
		},

		filterEvents: function() {
			filteredEvents = $.grep(events, function(event, i) {
				var fits = true;
				if(filter.categories.length != 0) {//TODO: temporary, change!!!
					var i = $.inArray(event.category.charAt(0).toUpperCase() + event.category.slice(1).toLowerCase(), filter.categories);
					if(i < 0){
						fits = false;
					}
				}
				return fits;
			});
			this.renderEvents(filteredEvents);
			//this.initMarkers(filteredEvents);
		},

		loadEvents: function(params) {
			var self = this;
			self.showSpinner();

			EventService.list(params, function(data){
				events = data;
				self.filterEvents();
				$('.date').removeClass('hidden');
				$('.events-container').removeClass('hidden');
				self.hideSpinner();
			}, function(){
				self.hideSpinner();
			});
		},

		renderEvents: function(events) {
			var self = this;
			$('#list-container').html($('#eventTemplate').render(events));

			$('.event-item').on('click', function() {
				var eventId = $(this).attr('id');
				self.highlightMarker(eventId);
			});

			$('.event-link').on('click', function(event) {
				event.preventDefault();
				var id = $(this).attr('id').split('-')[1];
				location.hash = '#!/en/Düsseldorf/e/' + id;
			});
		},

		loadGoogleScript: function() {
		/*	if (typeof google === 'object' && typeof google.maps === 'object') {
				this.initMap();
				return;
			}

			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?sensor=true' +
				'&libraries=places,visualization&language=en-US&v=3.2&callback=EventListModule.initMap';
			document.body.appendChild(script);*/
		},

		initMap: function(){
			var self = this;
			$('#map-canvas').width($('#list-container').width());

			var mapOptions = {
				zoom: 10,
				center: new google.maps.LatLng(51.227741, 6.773456),
				disableDefaultUI: true,
				disableDoubleClickZoom: true,
				panControl: false,
				zoomControl: true,
				zoomControlOptions: {
					position: google.maps.ControlPosition.LEFT_CENTER
				},
				scaleControl: false,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

			google.maps.event.addListener(map, 'idle', function() {
				//http://stackoverflow.com/questions/4338490/google-map-event-bounds-changed-triggered-multiple-times-when-dragging
				if(isMapShown == true) {
					var bounds = map.getBounds();

					filteredByMapEvents = [];
					for(var i = 0; i < markers.length; i++){
						var marker = markers[i];
						if(bounds.contains(marker.position)) {	
							for(var j = 0; j < events.length; j++) {
								if(marker['__gm_id'] == events[j]['__gm_id']) {
									filteredByMapEvents.push(events[j]);
								}
							}
						}
					}
				   	self.renderEvents(filteredByMapEvents);
				}
			});

			google.maps.event.addDomListener(window, "resize", function() {
				 google.maps.event.trigger(map, "resize");
			});

			self.initMarkers(events);
		},

		initMarkers: function (events) {
			//remove old markers from map
			for(var i=0; i < markers.length; i++){
				markers[i].setMap(null);
			}

			for(var i=0; i < events.length; i++) {
				var marker = this.addMarker(events[i], i);
				markers.push(marker);
				events[i]['__gm_id'] = marker['__gm_id'];
			}
		},

		addMarker: function (event, index) {
			var self = this;
			var marker= new google.maps.Marker({
				position:  new google.maps.LatLng(event.gpsLat, event.gpsLong),
				icon: self.getBlueIcon(),
				map: map,
				title: event.title});

			google.maps.event.addListener(marker, "click", function() {
				$.each(markers, function(index, marker) {
					marker.setIcon(self.getBlueIcon());
				});
				marker.setIcon(RED_MARKER);
				marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
				self.scrollToEvent(event.eventId);
			});
			
			google.maps.event.addListener(marker, "mouseover", function() {
				this.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
			});

			return marker;
		},

		highlightMarker: function(eventId) {
			var self = this;

			$.each(markers, function(index, marker) {
				marker.setIcon(self.getBlueIcon());
			});

			if(isMapShown == true) {
				$.each(events, function(index, event) {
					if(eventId == event.eventId){	
						$.each(markers, function(index, marker) {
							if(marker['__gm_id'] == event['__gm_id']) {
								marker.setIcon(RED_MARKER);
								marker.setZIndex(google.maps.Marker.MAX_ZINDEX + 1);
							}
						});
					}
				});
			}
		},

		scrollToEvent: function(id) {
			highlightMarkerOnScroll = false;
			$('body,html').stop().animate({
					scrollTop: $("#"+id).position().top
				}, 400, 'swing',
				function(){
					highlightMarkerOnScroll = true;
			});
		},

		isTopVisibleEvent: function(elem) {
			var docViewTop = $(window).scrollTop();
			var elemTop = $(elem).offset().top;
			var elemBottom = elemTop + $(elem).height();
			return (elemTop+5 >= docViewTop+200 && elemTop <= docViewTop + 200 + $(elem).height());
		},

		getBlueIcon: function() {
			return {
				path: google.maps.SymbolPath.CIRCLE,
				fillOpacity: 0.5,
				fillColor: '#0066FF',
				strokeOpacity: 1.0,
				strokeColor: 'black',
				strokeWeight: 1.0, 
				scale: 4 //pixels
			};
		},

		showSpinner: function() {
			$('body').append('<div class="overlay"><div class="spinner"><i class="fa fa-spinner fa-spin"></i></div></div>');
		},

		hideSpinner: function() {
			$('.overlay').remove();
		},

		updateHash: function(date, categories, type) {
			var hash = '!/en/Düsseldorf/Events/';
			if(type == 'Favourites' || type == 'Offers' || type == 'Hot') {
				hash += type;
			} else {
				var now = moment();
                var sod = now.startOf('day');
                var diff = date.startOf('day').diff(sod, 'days', true);

				hash +=
					diff < -1 ? date.format('YYYY-MM-DD') :
					diff < 0 ? 'Yesterday' :
                    diff < 1 ? 'Today' :
                    diff < 2 ? 'Tomorrow' : date.format('YYYY-MM-DD');
			}

			hash = hash + '/' + (categories.length == 0 ? 'All' : categories.join(','));

			location.hash = hash;
		},

		renderFilters: function (date, categories, loc){
			var self = this;
			var showFilters = false;

			//if(!Path.routes.previous || self.compareArrays(categories, filter.categories)==false/*($(categories).not(filter.categories).length != 0 || $(filter.categories).not(categories).length != 0)*/) {
				filter.categories = categories;
				$('#filter-categories a').each(function (i) {
					$(this).removeClass('selected');
				});
				if(filter.categories.length > 0) {
					showFilters = true;
					$.each(filter.categories, function(index, value) {
						$('#' + value).addClass('selected');
					});
					self.filterEvents();
				}
			//}

			if(categories.length == 0) {//temp hack
				$('#filter-categories a').each(function (i) {
					$(this).removeClass('selected');
				});
			}

			if(date == 'Favourites' || date == 'Offers' || date == 'Hot') {
				if(filter.type != date) {
					filter.type = date;
					filter.date = moment();
					$('#filter-date').text(date);
					$('#prev-day-button').hide();
					$('#next-day-button').hide();
					$('#filter-buttons a').each(function (i) {
						$(this).removeClass('selected');
					});
					$('#' + date + '-button').addClass('selected');

					showFilters = true;

					if(date == 'Favourites') {
						self.loadEvents({favourites: true});
					} else if(date == 'Offers') {
						self.loadEvents({promo: true});
					} else if(date == 'Hot') {
						self.loadEvents({hot: true});
					}
				}
			} else {
				delete filter.type;

				if(date == 'Yesterday') {
					filter.date = moment().subtract(1, 'days');
				} else if(date == 'Today') {
					filter.date = moment();
				} else if(date == 'Tomorrow') {
					filter.date = moment().add('days', 1);
				} else if(date && date.length > 0){
					filter.date = moment(date, 'YYYY-MM-DD');
				} else {
					filter.date = moment();
				}

				$('#filter-buttons a').each(function (i) {
					$(this).removeClass('selected');
				});
				$('#filter-date').text(filter.date.calendar());
				$('#prev-day-button').show();
				$('#next-day-button').show();
				self.loadEvents({date: filter.date.format('YYYY-MM-DD')});
			}

			if(showFilters == true) {
				$('#filter-btn').addClass('active');
				$('.filters').css('display', 'block');
				$('.events-container').css('margin-top', '200px');
			}

			if(loc) {
				$('#map-btn').toggleClass('active');
				$('.map').css({'visibility': 'inherit', 'position': 'relative', 'left': ''});
				$('.events-container').css('margin-top', '200px');
			}
		},

		compareArrays: function(arr1, arr2){
		    if (arr1.length !== arr2.length) return false;
		    for (var i = 0, len = arr1.length; i < len; i++){
		        if (arr1[i] !== arr2[i]){
		            return false;
		        }
		    }
		    return true;
		}
	}
}());