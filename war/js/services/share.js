angular.module('itsonin').factory('shareService',
	['$q', '$modal', 'views', function($q, $modal, views) {

	return {
		shareLink: function (eventId, hostId) {
	        var modalPromise = $modal({
	            template: views.shareLink,
	            persist: false,
	            show: false,
	            keyboard: true,
	            data: {eventId: eventId, hostId: hostId}
	        });

	        $q.when(modalPromise).then(function(modalEl) {
	            modalEl.modal('show');
	        });
	    },
	    shareByEmail: function (eventId, hostId) {
	        var modalPromise = $modal({
	            template: views.shareByEmail,
	            persist: false,
	            show: false,
	            keyboard: true,
	            data: {eventId: eventId, hostId: hostId}
	        });

	        $q.when(modalPromise).then(function(modalEl) {
	            modalEl.modal('show');
	        });
	    }
	};
}]);
