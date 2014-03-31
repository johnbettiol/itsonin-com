angular.module('itsonin').controller('DeclineInvitationController',
	['$scope', '$routeParams', 'eventService', function ($scope, $routeParams, eventService) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.invitationId, function(response) {//TODO: send eventId
				$scope.event = response;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

}]);
