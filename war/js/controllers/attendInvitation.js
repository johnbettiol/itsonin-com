angular.module('itsonin').controller('AttendInvitationController',
	['$scope', '$routeParams', 'eventService', 'shareService',
	 function ($scope, $routeParams, eventService, shareService) {

		$scope.eventId = $routeParams.invitationId.split('.')[0];
		$scope.parentGuestId = $routeParams.invitationId.split('.')[1];
		
		$scope.loadEvent = function () {
			eventService.info($scope.eventId, function(response) {
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
