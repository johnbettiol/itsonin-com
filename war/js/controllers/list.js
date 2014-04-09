angular.module('itsonin').controller('ListController',
  ['$scope', '$rootScope', '$routeParams', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, eventService, constants) {
	  
	  $scope.allEvents = true;
	  $scope.allDates = true;
	  $scope.allPlaces = true;
	  $scope.allCategories = true;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  $scope.locationExist = ($routeParams.location == $rootScope.location);
	  $scope.filter = {
		types: []
	  };
	  
	  $scope.isKnownLocation = function () {
	      var knownLocations = ['Düsseldorf', 'Duesseldorf', 'Dusseldorf', 'Düsseldorf'];
	      for(var i=0; i<knownLocations.length; i++){
	          if($routeParams.location == knownLocations[i]){
	              return true;
	          }
	      }
	      return false;
	  }

	  $scope.$watch('filter.startTime', function(newValue, oldValue) {
		  if(newValue && (newValue+'').length == 16 ){//TODO: fix
			  $scope.loadEvents();
		  }
	  });
	  
	  $scope.loadEvents = function () {
	    if($scope.allEvents == true) {
	        angular.extend($scope.filter, {allEvents: true});
	    } else {
	        angular.extend($scope.filter, {allEvents: false});
	    }
		eventService.list($scope.filter, function(response) {
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
		  $scope.resetFilter();
          $scope.allEvents = !$scope.allEvents;
		  $scope.loadEvents();
		  $scope.allDates = true;
		  $scope.allPlaces = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.toggleDatesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allDates = !$scope.allDates;
		  $scope.allEvents = true;
		  $scope.allPlaces = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.togglePlacesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allPlaces = !$scope.allPlaces;
		  $scope.allDates = true;
		  $scope.allEvents = true;
		  $scope.allCategories = true;
	  }
	  
	  $scope.toggleCategoriesFilter = function() {
		  $scope.resetFilter();
		  $scope.loadEvents();
		  $scope.allCategories = !$scope.allCategories;
		  $scope.allDates = true;
		  $scope.allPlaces = true;
		  $scope.allEvents = true;
	  }
	  
	  $scope.toggleEventType = function(type) {
		  var exist = -1;
		  angular.forEach($scope.filter.types, function(item, index){
			  if(item == type) {
				  exist = index;
			  }
		  });
		  
		  if(exist == -1){
			  $scope.filter.types.push(type);
		  } else {
			  $scope.filter.types.splice(exist, 1);
		  }
		  
		  $scope.loadEvents();
	  }
	  
	  $scope.filterContainsType = function(type) {
		  var exist = -1;
		  angular.forEach($scope.filter.types, function(item, index){
			  if(item == type) {
				  exist = index;
			  }
		  });
		  return exist >= 0;
	  }
	  
	  $scope.resetFilter = function() {
		  $scope.filter = {
			types: []
		  };
	  }
	  
}]);
