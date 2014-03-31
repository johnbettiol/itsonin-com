angular.module('itsonin').directive('bDatepicker', function(){
	  return {
	    require: '?ngModel',
	    link: function(scope, element, attrs, controller) {
	      var updateModel = function(ev) {
	        scope.$apply(function() {
	            controller.$setViewValue(ev.date);
	            element.datepicker('hide');
	            element.blur();
	          });
	      };
	      if (controller != null) {
	        controller.$render = function() {
	          element.datepicker().data().datepicker.date = controller.$viewValue;
	          element.datepicker('setValue', controller.$viewValue);
	          element.datepicker('update');
	          return controller.$viewValue;
	        };
	      }
	      element.datepicker({autoclose: true}).on('changeDate', updateModel);
	    }
	  };
});