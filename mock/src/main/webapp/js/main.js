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

function TaskListCtrl(MockRestangular, CommonService, $scope, $http, $routeParams) {
	// init common functions
	$scope.formatDate = CommonService.formatDate;
	
	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	MockRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loading = true;
	});

	MockRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loading = false;
		console.log("error interceptor");
	});
	
	$scope.loadOpenTaskList = function() {
		var resource = MockRestangular.all('task/open');
		resource.getList().then(function(tasks) {
			$scope.tasks = tasks;
			$scope.loading = false;
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
	};
	
	$scope.loadOpenTaskList();
	
}

function TaskCtrl(MockRestangular, CommonService, $scope, $http, $routeParams) {
	// init common functions
	$scope.formatDate = CommonService.formatDate;
	
	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	MockRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loading = true;
	});

	MockRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loading = false;
		console.log("error interceptor");
	});
			
	$scope.loadTask = function(id) {
		MockRestangular.one('task', id).get().then(function(task) {
			$scope.loading = false;
			$scope.task = task;
			console.log(task);
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
	};
	
	$scope.loadTask($routeParams.id);
	
	$scope.submitTask = function() {
		var body = {
				userId: $scope.userId,
				answer: $scope.answer,
				task: {
					id: $scope.task.id,
					url: $scope.task.url
				}
		};
		
		// restangular-post seems broken, doesn't send body
		$http.post("./rest/task/submitTask", body).then(function(job) {
			$scope.job = job.data;
			window.location.href="#";
			// show info message
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
	}
}

