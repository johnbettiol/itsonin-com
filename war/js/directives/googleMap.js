angular.module('itsonin').directive('googleMap', function(){
	return {
		restrict: 'E',
		scope: {place:'='},
		template: '<input id="pac-input" class="form-control" style="margin-bottom:5px" type="text">'+
		'<div id="map-canvas" style="height: 400px;"></div>',
		link: function($scope, element, attrs){
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
				for (var i = 0, place; place = places[i]; i++) {
					
					$scope.$apply(function (){
						$scope.place = place;
					});

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
					
					google.maps.event.addListener(marker, 'dragend', function() {
						geocoder.geocode({
						    latLng: marker.getPosition()
						  }, function(responses) {
						    if (responses && responses.length > 0) {
						      console.log(responses[0].formatted_address);
						    } else {
						      console.log('Cannot determine address at this location.');
						    }
						  });
					  });

					markers.push(marker);

					bounds.extend(place.geometry.location);
				}

				map.fitBounds(bounds);
			});
			
			google.maps.event.addListener(map, 'bounds_changed', function() {
				var bounds = map.getBounds();
				searchBox.setBounds(bounds);
			});
		}
	}
});