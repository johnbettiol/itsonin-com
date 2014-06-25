var EventNewModule = {
		
	dateTypeState: 0,
	dateTypes: ["date-type-sometime", "date-type-now", "date-type-custom"],
	locationTypeState: 0,
	locationTypes: ["location-type-sometime", "date-type-now", "date-type-custom"],

    init: function() {
    	var self = this;
    	
        $('#date-type-button').on('click', function() {
        	self.toggleDateType();
        });
        
        $('#show-map-button').on('click', function() {
        	self.showMapModal();
        });
        
        $('#event-form').submit(self.saveEvent);
        
        $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii', autoclose: true});
        
        $('.event-category').on('click', function() {
        	$('.event-category').each(function (i) {
        		$(this).parent().removeClass('active');
        		$('#event-subcategories-' + $(this).attr('id')).hide();
        	});
        	$(this).parent().addClass('active');
        	$('#event-subcategories-' + $(this).attr('id')).show();
        });
        
        $('.event-subcategory').on('click', function() {
        	$('.event-subcategory').each(function (i) {
        		$(this).parent().removeClass('active');
        	});
        	$(this).parent().addClass('active');
        });
        
        self.initLocationAutocomplete();
        
        self.initMap();
    },
    
    toggleDateType: function(){    	
    	$('#' + this.dateTypes[this.dateTypeState]).hide();
    	this.dateTypeState = (this.dateTypeState + 1) % this.dateTypes.length;
		$('#' + this.dateTypes[this.dateTypeState]).show();

    	switch(self.dateTypeState) {
	    	case 0: {//sometime
	
	    		break;
	    	} case 1: {//now
	
	    		break; 
	    	} case 2: {//custom
	    		break;
	    	}
    	}
    },
    
    saveEvent: function(event) {
    	if ($('#name').val().length == 0) {
    		$('#error-message').html('Guest name is required');
    		$('#error-message').show();
    		event.preventDefault();
    	}
    },
    
    showMapModal: function() {
    	$('#map-modal').modal('show');
    },

	createEvent: function(event, guest, callback) {
		$.ajax({
		    type: 'POST',
		    url: '/api/event/create',
		    data: JSON.stringify({event:event, guest:guest}),
		    success: callback,
		    contentType: "application/json",
		    dataType: 'json'
		});
	},
	
	updateEvent: function(event, guest, callback) {
		$.ajax({
		    type: 'POST',
		    url: '/api/event/' + event.eventId + '/update',
		    data: JSON.stringify({event:event, guest:guest}),
		    success: callback,
		    contentType: "application/json",
		    dataType: 'json'
		});
	},
	
	initLocationAutocomplete: function() {
        var autocomplete = new google.maps.places.Autocomplete($("#locationAddress")[0], {});
        google.maps.event.addListener(autocomplete, 'place_changed', function() {
			var place = autocomplete.getPlace();
			//TODO
			/*$scope.event['locationAddress'] = place.name;
			$scope.event['gpsLat'] = place.geometry.location.lat();
			$scope.event['gpsLong'] = place.geometry.location.lng();*/
		});
	},
	
    initMap: function(){
    	var markers = [];
		var center = new google.maps.LatLng(50.1, 14.4);
		var map = new google.maps.Map(document.getElementById('map-canvas'), 
				{mapTypeId: google.maps.MapTypeId.ROADMAP, zoom: 8});
		var geocoder = new google.maps.Geocoder();

		var defaultBounds = new google.maps.LatLngBounds(
			      new google.maps.LatLng(51.230501, 6.762852),
			      new google.maps.LatLng(51.840701, 6.762852));
			  map.fitBounds(defaultBounds);
			  
		var input = document.getElementById('pac-input');
		//map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

		var searchBox = new google.maps.places.SearchBox(input);
		
		// Listen for the event fired when the user selects an item from the
		// pick list. Retrieve the matching places for that item.
		google.maps.event.addListener(searchBox, 'places_changed', function() {
			var places = searchBox.getPlaces();

			for (var i = 0, marker; marker = markers[i]; i++) {
				marker.setMap(null);
			}

			// For each place, get the icon, place name, and location.
			markers = [];
			var bounds = new google.maps.LatLngBounds();
			//for (var i = 0, place; place = places[i]; i++) { //using only 1st place
			
				var place = places[0];

				// Create a marker for each place.
				var marker = new google.maps.Marker({
					map: map,
					title: place.name,
					position: place.geometry.location,
					draggable: true
				});
				
				google.maps.event.addListener(marker, 'click', function (mouseEvent) {
					//console.log(marker.getPosition().lat())
					//latitude: gMapMarker.getPosition().lat(),
					//	longitude: gMapMarker.getPosition().lng(),
					var pos = mouseEvent.latLng;

				});
				
				/*google.maps.event.addListener(marker, 'dragend', function() {
					geocoder.geocode({
					    latLng: marker.getPosition()
					  }, function(responses) {
					    if (responses && responses.length > 0) {
					      console.log(responses[0].formatted_address);
					    } else {
					      console.log('Cannot determine address at this location.');
					    }
					  });
				  });*/

				markers.push(marker);

				bounds.extend(place.geometry.location);
			//}

			map.fitBounds(bounds);
		});
    }
};