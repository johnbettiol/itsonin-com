angular.module('itsonin').controller('MeController',
  ['$scope', 'eventService', 'constants', function ($scope, eventService, constants) {
	  
	  $scope.loadEvents = function () {
		  eventService.list(function(response) {
			  $scope.events = response;
		  },
		  function(error) {

		  });
	  }

	  $scope.loadEvents();

	  $scope.getEventImage = function(eventType) {
		  return $scope.eventImages[eventType];
	  }

	  $scope.getEventImages = function() {
		  var result = {};
		  angular.forEach(constants.EVENT_TYPES, function(eventType){
			  result[eventType.id] = eventType.img;
		  });
		  return result;
	  }
	  $scope.eventImages = $scope.getEventImages();
	  
}]);
