angular.module('itsonin').controller('AttendInvitationController',
	['$scope', '$routeParams', '$modal', 'eventService', 'views', '$q',
	 function ($scope, $routeParams, $modal, eventService, views, $q) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.invitationId, function(response) {//TODO: send eventId
				$scope.event = response;
			},
			function(error) {
			});
		}

		$scope.loadEvent();

		$scope.shareLink = function () {
		    var modalPromise = $modal({
		        template: views.shareLink,
		        persist: false,
		        show: false,
		        keyboard: true,
		        data: {}
		    });

		    $q.when(modalPromise).then(function(modalEl) {
		        modalEl.modal('show');
		    });
		}
}]);
