angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', 'eventService', function ($scope, $routeParams, eventService) {
	  
	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response;
		},
		function(error) {
			
		});
	}
	
	$scope.loadEvent();
	  
}]);
