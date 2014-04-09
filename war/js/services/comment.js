angular.module('itsonin').factory('commentService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
	    create: function(eventId, comment, success, error) {
	        $http.post('/api/event/' + eventId + '/comment/create', comment).success(success).error(error);
	    },
	    list: function(eventId, success, error) {
	        $http.post('/api/event/' + eventId + '/comment/list').success(success).error(error);
	    }
	};
}]);
