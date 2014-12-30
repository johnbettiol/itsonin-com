var EventService = (function() {

	var eventsCache = {};

	return {
		list: function(params, success, error) {console.log(params)
			var id = '';
			for(var prop in params){
				if(params.hasOwnProperty(prop)){
					id += (prop + '_' + params[prop]);
				}
			}

			if(eventsCache[id]) {
				if(success && typeof success == 'function') {
					success(eventsCache[id]);
				}
			} else {
				$.ajax({
					type: 'GET',
					url: '/api/event/list',
					data: params,
					contentType: "application/json",
					dataType: 'json'
				}).done(function(data, textStatus, jqXHR) {
					if(success && typeof success == 'function') {
						eventsCache[id] = data;
						success(eventsCache[id]);
					}
				}).fail(function(jqXHR, textStatus, errorThrown) {
					if(error && typeof error == 'function') {
						error(textStatus);
					}
				});
			}
		},

		save: function(event, guest, success, error) {
			$.ajax({
				type: 'POST',
				url: '/api/event/create',
				data: JSON.stringify({event:event, guest:guest}),
				contentType: "application/json",
				dataType: 'json'
			}).done(function(data) {
				if(success && typeof success == 'function') {
					//TODO: add to cache
					eventsCache = {};
					success(data);
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
				if(error && typeof error == 'function') {
					error(textStatus);
				}
			});
		}
	}

}());