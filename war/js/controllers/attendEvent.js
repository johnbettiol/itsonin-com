angular.module('itsonin').controller('AttendEventController',
	['$scope', '$routeParams', '$modal', 'eventService', 'views', '$q',
	 function ($scope, $routeParams, $modal, eventService, views, $q) {

		$scope.loadEvent = function () {
			eventService.info($routeParams.eventId, function(response) {
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
		        data: {eventId: $routeParams.eventId, hostId: $routeParams.hostId}
		    });

		    $q.when(modalPromise).then(function(modalEl) {
		        modalEl.modal('show');
		    });
		}
}]);

