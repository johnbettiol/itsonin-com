angular.module('itsonin').controller('AttendEventController',
	['$scope', '$routeParams', 'eventService', 'shareService',
	 function ($scope, $routeParams, eventService, shareService) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.eventId, function(response) {
				$scope.event = response.event;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

	    $scope.shareLink = function () {
	        shareService.shareLink($routeParams.eventId, $routeParams.hostId);
	    }
	    
	    $scope.shareByEmail = function () {
	        shareService.shareByEmail($routeParams.eventId, $routeParams.hostId);
	    }
}]);

