angular.module('itsonin').directive('googlePlaces', function(){
	return {
		restrict:'E',
		replace:true,
		scope: {event:'='},
		template: '<input id="google_places_ac" name="google_places_ac" type="text" ' +
			'class="input-block-level form-control" ng-model="event.locationAddress"/>',
		link: function($scope, elm, attrs){
			var autocomplete = new google.maps.places.Autocomplete($("#google_places_ac")[0], {});
			google.maps.event.addListener(autocomplete, 'place_changed', function() {
				var place = autocomplete.getPlace();
				$scope.event['locationAddress'] = place.name;
				$scope.event['gpsLat'] = place.geometry.location.lat();
				$scope.event['gpsLong'] = place.geometry.location.lng();
				$scope.$apply();
			});
		}
	}
});