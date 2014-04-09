angular.module('itsonin').controller('DeclineInvitationController',
	['$scope', '$routeParams', 'eventService', function ($scope, $routeParams, eventService) {

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

}]);
