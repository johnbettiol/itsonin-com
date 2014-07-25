var EventListModule = {

	filteredEvents: [],
	filter: {
			types: [],
			startTime: moment().set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0)
	},
	
    init: function() {
    	var self = this;
        
        $('#settings-btn').on('click', function() {
        	alert('settings');
        });

        $('#add-event-btn').on('click', function() {
        	alert('settings');
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

        $('#happening-tomorrow-btn').on('click', function() {
        	alert('tomorrow');
        });

        $('.list-categories li').on('click', function() {
        	$('.list-categories li').each(function (i) {
        		$(this).removeClass('active');
        	});
        	$(this).addClass('active');
        	self.filterEvents('subCategory', $(this).attr('id'));
        });

        $("#date-field").datetimepicker({format: 'yyyy-mm-dd hh:ii', autoclose: true});

        self.initMap();

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
    	$('#list-container').empty();
    	this.renderEvents(filteredEvents);
    },

    filterEventsByText: function(text) {
    	filteredEvents = $.grep(events, function(e, i) {
   			return  e.title.toLowerCase().indexOf(text.toLowerCase()) > -1
	   	});
	   	$('#list-container').empty();
	   	this.renderEvents(filteredEvents);
    },
    
    filterNowEvents: function() {
		var now = new Date();
    	filteredEvents = $.grep(events, function(e, i) {
   			return e.startTime < now && e.endTime > now;
	   	});
	   	$('#list-container').empty();
	   	this.renderEvents(filteredEvents);
    },
    
    loadEvents: function(params, callback) {
	    $.get('/api/event/list', params, callback);
    },
    
    renderEvents: function(events) {
    	$('#list-container').html($('#eventTemplate').render(events));
    },
    
    initMap: function(){
    	var markers = [];
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
		var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

		var marker = new google.maps.Marker({
			position: map.getCenter(),
			map: map
		});

		//TODO: create cluster
		//var markerCluster = new MarkerClusterer(map, markers);
    }
};