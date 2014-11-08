angular.module("itsonin").factory('loading', function() {
  var service = {
      requestCount: 0,
      message: $('<div id="loading"/>'),
      show: function() {
        this.requestCount++;
        var width = $(window).width();

        if ($("#loading").length === 0) {
          this.message.appendTo($(document.body));
        }

        this.message
            .css({
            	'font': 'normal 16px Verdana, Tahoma',
	        	'background-color': '#f9edbe',
	        	'border': '1px solid #F0C36D',
	        	'-moz-border-radius': '2px',
	        	'-webkit-border-radius': '2px',
	        	'-webkit-box-shadow': '0 2px 4px rgba(0, 0, 0, 0.2)',
	        	'border-radius': '2px',
	        	'box-shadow': '0 2px 4px rgba(0, 0, 0, 0.2)',
	        	'line-height': '18px',
	        	'padding-top': '2px',
	        	'padding-bottom': '2px',
	        	'padding-left': '5px',
	        	'padding-right': '5px',
	        	'text-align': 'center',
	        	'vertical-align': 'middle',
	        	'position': 'absolute',
	        	'top': '20px',
	        	'z-index': '100001'
            })
            .addClass('inf')
            .css('left', width / 2 - this.message.width() / 2)
            .text('Loading ...')
            .show();
      },
      hide: function() {
        this.requestCount--;
        if (this.requestCount === 0) {
          this.message.hide();
        }
      },
      isLoading: function() {
        return this.requestCount > 0;
      }
  };
  return service;
});
