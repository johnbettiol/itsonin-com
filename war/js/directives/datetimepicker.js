angular.module('itsonin').directive('bDatetimepicker', function(){
	  return {
	    require: '?ngModel',
	    link: function(scope, element, attrs, controller) {
	      var updateModel = function(ev) {
	        scope.$apply(function() {
	            controller.$setViewValue(new Date(ev.date.valueOf()));
	            element.datetimepicker('hide');
	            element.blur();
	          });
	      };
	      if (controller != null) {
	        controller.$render = function() {
	          element.datetimepicker().initialDate = controller.$viewValue;
	          element.datetimepicker({autoclose: true}).on('changeDate', updateModel);
	          if(controller.$viewValue){
	        	  element.datetimepicker('update', new Date(controller.$viewValue));
	          }
	          return controller.$viewValue;
	        };
	      }/*else {
	    	  element.datetimepicker({autoclose: true}).on('changeDate', updateModel);
	      }*/
	    }
	  };
});