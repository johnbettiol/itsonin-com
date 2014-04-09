angular.module('itsonin').controller('ViewEventController',
  ['$scope', '$routeParams', 'eventService', 'guestService', 'commentService', 'shareService',
   function ($scope, $routeParams, eventService, guestService, commentService, shareService) {

	$scope.readyToShow = false;
	$scope.event = {};

	$scope.loadEvent = function () {
		eventService.info($routeParams.eventId, function(response) {
			$scope.event = response.event;
			$scope.guests = response.guests;
			$scope.comments = response.comments;
			$scope.readyToShow = true;
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
