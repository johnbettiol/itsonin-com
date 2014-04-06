angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', 'eventService', 'guestService', 
   function ($scope, $routeParams, eventService, guestService) {

	$scope.readyToShow = false;

	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response;
			$scope.readyToShow = true;
		},
		function(error) {

		});
	}

	$scope.loadEvent();
	
	$scope.createGuest = function () {
		guestService.create($routeParams.eventId, 'GUEST', 'VIEWED', function(response) {

		},
		function(error) {

		});
	}

	$scope.createGuest();
	  
}]);
