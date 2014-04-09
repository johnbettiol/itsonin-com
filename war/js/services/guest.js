angular.module('itsonin').factory('guestService',
	['$http', function($http) {

	return {
	    create: function(eventId, type, status, success, error) {
	        $http.post('/api/event/' + eventId + '/guest/create', {status: status, type: type})
	        .success(success).error(error);
	    },
	    list: function(eventId, success, error) {
	        $http.get('api/event/' + eventId + '/guest/list').success(success).error(error);
	    }
	};
}]);
