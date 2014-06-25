var EventListModule = {

	filter: {
			types: [],
			startTime: moment().set('hour', 0).set('minute', 0).set('second', 0).set('millisecond', 0)
	},
	
    init: function() {
    	var self = this;
        $('#searchButton').on('click', function() {

        });
        
        $('#filter-now-button').on('click', function() {

        });
        
        $('#filter-tomorrow-button').on('click', function() {

        });
        
        $('.event-category').on('click', function() {
        	$('.event-category').each(function (i) {
        		$(this).parent().removeClass('active');
        		$('#event-subcategories-' + $(this).attr('id')).hide();
        	});
        	$(this).parent().addClass('active');
        	$('#event-subcategories-' + $(this).attr('id')).show();
        });
        
        $('.event-subcategory').on('click', function() {
        	$('.event-subcategory').each(function (i) {
        		$(this).parent().removeClass('active');
        	});
        	$(this).parent().addClass('active');
        });
        
        $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii', autoclose: true});
    },
    
    loadEvents: function(params, callback) {
	    $.get('/api/event/list', params, callback);
    }
};