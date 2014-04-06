angular.module('itsonin').factory('guestService',
	['$http', function($http) {

	return {
		create: function(eventId, type, status, success, error) {
			$http.post('/api/event/' + eventId + '/guest/create', {status: status, type: type})
			.success(success).error(error);
		}
	};
}]);
