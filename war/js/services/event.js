angular.module('itsonin').factory('eventService',
	['$http', '$q', '$rootScope', '$cacheFactory', function($http, $q, $rootScope, $cacheFactory) {

	return {
		list: function(queryParams, success, error) {
			$http.get('/api/event/list', {params:queryParams}).success(success).error(error);
		},
		create: function(event, guest, success, error) {
			$http.post('/api/event/create', {event:event, guest:guest}).success(success).error(error);
		},
		update: function(event, success, error) {
			$http.put('/api/event/' + event.eventId + '/update', event).success(success).error(error);
		},
		info: function(eventId, success, error) {
			$http.get('/api/event/' + eventId + '/info').success(success).error(error);
		},
		attend: function(eventId, guestName, success, error) {
			$http.get('/api/event/' + eventId + '/attend/' + encodeURIComponent(guestName))
				.success(success).error(error);
		},
		decline: function(eventId, guestName, success, error) {
			$http.get('/api/event/' + eventId + '/decline/' + encodeURIComponent(guestName))
				.success(success).error(error);
		}
	};
}]);
