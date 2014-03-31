angular.module('itsonin').factory('eventService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
		list: function(success, error) {
			$http.get('/api/event/list').success(success).error(error);
		},
		create: function(event, guest, success, error) {
			$http.post('/api/event/create', {event:event, guest:guest}).success(success).error(error);
		},
		update: function(event, success, error) {
			$http.put('/api/' + event.id + '/update', event).success(success).error(error);
		},
		info: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/info', event).success(success).error(error);
		},
		attend: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/attend', event).success(success).error(error);
		},
		decline: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/decline', event).success(success).error(error);
		}
	};
}]);
