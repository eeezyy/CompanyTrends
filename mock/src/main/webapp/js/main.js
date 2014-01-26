var app = angular.module('mock', [ 'restangular', 'ngRoute' ]);

// Global configuration
app.config([ "RestangularProvider", function(RestangularProvider) {
	RestangularProvider.setRestangularFields({
		id : "id"
	});
}]);

app.config([ "$routeProvider", "$locationProvider", function($routeProvider, $locationProvider) {
	$routeProvider.when('/', { templateUrl: 'templates/taskList.html', controller: 'TaskListCtrl' });
	$routeProvider.when('/:id', { templateUrl: 'templates/task.html', controller: 'TaskCtrl' });
	$routeProvider.otherwise({redirectTo: '/'});
}]);

app.factory('CommonService', function() {
	var formatDate = function(timeInMillis) {
		var date = new Date(parseInt(timeInMillis, 10));
		return date.toGMTString();
	};
	
	return {
		formatDate: formatDate
	};
});

// Configuration for Crowd REST-API
app.factory('MockRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('rest');
	});
});

function MainCtrl($route, $routeParams, $location) {
	this.$route = $route;
	this.$location = $location;
	this.$routeParams = $routeParams;
}

function TaskListCtrl(MockRestangular, $scope, $http, $routeParams) {
	MockRestangular.setRequestInterceptor(function(elem, operation, what) {
		// remove error message if exists
		$scope.errorMessageRequest = false;
		
		// to show loading icon
		$scope.waitForRequest = true;
	});

	MockRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.waitForRequest = false;
	});
	
	$scope.loadOpenTaskList = function() {
		$scope.requestLoading = true;
		var resource = MockRestangular.all('task/open');
		resource.getList().then(function(tasks) {
			console.log(tasks);
			$scope.waitForRequest = false;
		});
		
	};
	
	$scope.loadOpenTaskList();

	$scope.formatDate = function(timeInMillis) {
		var date = new Date(parseInt(timeInMillis, 10));
		return date.toGMTString();
	};
}

function TaskCtrl(MockRestangular, CommonService, $scope, $http, $routeParams) {
	// functions
	$scope.formatDate = CommonService.formatDate;
			
	$scope.loadTask = function(id) {
		$scope.requestList = true;
		
		MockRestangular.one('task', id).get().then(function(task) {
			$scope.requestList = false;
			$scope.task = task;
			
		});
	};
}

