angular.module('itsonin').factory('eventService',
	['$http', '$rootScope', '$cacheFactory', function($http, $rootScope, $cacheFactory) {

	var cache = $cacheFactory('eventsInfoCache');
		
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
		info: function(eventId, queryParams, success, error) {
			var fromCache = cache.get(eventId);			
        	if(!fromCache){
        		$http.get('/api/event/' + eventId + '/info', {params:queryParams}).success(function(info) {
	        		cache.put(eventId, info);
	        	
	        		if(angular.isFunction(success)){
	        			success(info);
	        		}
	        	}).error(error);
        	}else{
        		if(angular.isFunction(success)){
        			success(fromCache);
        		}
        	}
		},
		attend: function(eventId, guestName, success, error) {
			cache.remove(eventId);
			$http.get('/api/event/' + eventId + '/attend/' + encodeURIComponent(guestName))
				.success(success).error(error);
		},
		decline: function(eventId, guestName, success, error) {
			cache.remove(eventId);
			$http.get('/api/event/' + eventId + '/decline/' + encodeURIComponent(guestName))
				.success(success).error(error);
		}
	};
}]);
