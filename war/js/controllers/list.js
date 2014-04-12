angular.module('itsonin').controller('ListController',
  ['$scope', '$rootScope', '$routeParams', 'eventService', 'constants', 
   function ($scope, $rootScope, $routeParams, eventService, constants) {
	  
	  $scope.dateFilterState = 0;
	  $scope.dateFilterText = ["All dates", "Custom", "Now", "Tomorrow"];
	  $scope.allEvents = true;
	  $scope.allPlaces = true;
	  $scope.allCategories = true;
	  $scope.eventTypes = constants.EVENT_TYPES;
	  $scope.locationExist = ($routeParams.location == $rootScope.location);
	  $scope.filter = {
		types: []
	  };
	  
	  $scope.isKnownLocation = function () {
	      var knownLocations = ['Düsseldorf', 'Duesseldorf', 'Dusseldorf'];
	      for(var i=0; i<knownLocations.length; i++){
	          if($routeParams.location == knownLocations[i]){
	              return true;
	          }
	      }
	      return false;
	  }

	  $scope.$watch('filter.startTime', function(newValue, oldValue) {
		  if(newValue && newValue instanceof Date && $scope.dateFilterState == 1){
			  $scope.loadEvents();
		  }
	  });
	  
	  $scope.loadEvents = function () {
		$scope.events = [];
	    var params = {
			allEvents: ($scope.allEvents == true)?true:false,
			types: $scope.filter.types
	    }
	    
	    console.log($scope.filter.startTime);
	    
	    if($scope.filter.startTime){
	    	params.startTime = moment($scope.filter.startTime).format('YYYY-MM-DD HH:mm');
	    }
	    
	    if($scope.filter.endTime){
	    	params.endTime = moment($scope.filter.endTime).format('YYYY-MM-DD HH:mm');
	    }
	    
		eventService.list(params, function(response) {
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
       	  $scope.loadEvents();
	  }
	  
	  $scope.toggleDateFilter = function(){
		  $scope.dateFilterState = ($scope.dateFilterState + 1) % $scope.dateFilterText.length;
		  
		  switch($scope.dateFilterState) {
			  case 0: {//all
				  delete $scope.filter.startTime;
				  delete $scope.filter.endTime;
				  $scope.loadEvents();
				  break;
			  } case 2: {//now
				  $scope.filter.startTime = new Date();
				  $scope.filter.endTime = new Date();
				  $scope.loadEvents();
				  break; 
			  } case 3: {//tomorrow
				  $scope.filter.startTime = moment().add('days', 1)
				  	.set('hour', 0).set('minute', 0).set('second', 0);
				  $scope.filter.endTime = moment().add('days', 1)
				  	.set('hour', 23).set('minute', 59).set('second', 59);
				  $scope.loadEvents();
			  }
		  }
	  }
	  
	  $scope.togglePlacesFilter = function() {
		  $scope.allPlaces = !$scope.allPlaces;
		  if($scope.allPlaces == true){
			  $scope.loadEvents();
		  }
	  }
	  
	  $scope.toggleCategoriesFilter = function() {
		  $scope.allCategories = !$scope.allCategories;
		  if($scope.allCategories == true){
			  $scope.filter.types = [];
			  $scope.loadEvents();
		  }
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
	  
}]);
