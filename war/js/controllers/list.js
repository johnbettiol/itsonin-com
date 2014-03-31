angular.module('itsonin').controller('ListController',
  ['$scope', '$rootScope', '$routeParams', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, eventService, constants) {
	  
	  $scope.allEvents = true;
	  $scope.allDates = true;
	  $scope.allPlaces = true;
	  $scope.allCategories = true;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  $scope.locationExist = ($routeParams.location == $rootScope.location)
	  
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
	  
	  $scope.toggleEventsFilter = function() {
		  $scope.allEvents = !$scope.allEvents;

	  }
	  
	  $scope.toggleDatesFilter = function() {
		  $scope.allDates = !$scope.allDates;

	  }
	  
	  $scope.togglePlacesFilter = function() {
		  $scope.allPlaces = !$scope.allPlaces;

	  }
	  
	  $scope.toggleCategoriesFilter = function() {
		  $scope.allCategories = !$scope.allCategories;
	  }
	  
}]);
