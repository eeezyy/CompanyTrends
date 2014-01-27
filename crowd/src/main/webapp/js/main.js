var app = angular.module('crowdsourcing', [ 'restangular', 'ngRoute' ]);

// Global configuration
app.config([ "RestangularProvider", function(RestangularProvider) {
	RestangularProvider.setRestangularFields({
		id : "id"
	});
}]);

app.config([ "$routeProvider", "$locationProvider", function($routeProvider, $locationProvider) {
	$routeProvider.when('/', { templateUrl: 'templates/search.html', controller: 'SearchCtrl' });
	$routeProvider.when('/:id', { templateUrl: 'templates/job.html', controller: 'JobCtrl' });
	$routeProvider.when('/job/list', { templateUrl: 'templates/jobList.html', controller: 'JobListCtrl' });
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
app.factory('CrowdRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('rest');
	});
	
});

// Configuraiton for DBPedia API
app.factory('DBPediaRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer
				.setBaseUrl('http://lookup.dbpedia.org/api/search.asmx');
	});
});

function MainCtrl($route, $routeParams, $location) {
	this.$route = $route;
	this.$location = $location;
	this.$routeParams = $routeParams;
}

function SearchCtrl(CrowdRestangular, DBPediaRestangular, CommonService, $scope, $http, $routeParams, $route) {
	// functions
	$scope.formatDate = CommonService.formatDate;
	
	// preset variables
	$scope.selectedIndex = -1;
	
	$scope.errorMessageRequest = false;
	
	if($routeParams.id) {
		$scope.loadJob($routeParams.id);
	} else {
		if($scope.job) {
			$scope.job.name = undefined;
			$scope.job.articles = undefined;
			$scope.job.state = undefined;
			$scope.searchName = undefined;
		}
	}

	// Interceptors
	CrowdRestangular.setRequestInterceptor(function(elem, operation, what) {
		// remove error message if exists
		$scope.errorMessageRequest = false;
//		$scope.noArticlesFound = false;
		// to show loading icon
		$scope.requestList = true;
	});

	CrowdRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.requestList = false;
	});

	$scope.selectSuggestion = function(index) {
		$scope.job.name = $scope.suggestions.results[index].label;
		$scope.suggestions = false;
//		var resource = CrowdRestangular.one('article', $scope.searchName);
//		resource.getList().then(function(articles) {
//			$scope.articles = articles;
//		});
	};
	
	$scope.createJob = function() {
		$scope.requestList = true;
		var body = {
				name: $scope.job.name
		};
		
		// restangular-post seems broken, doesn't send body
		$http.post("./rest/job", body).then(function(job) {
			$scope.job = job.data;
			// don't use html5-history, causes endless loop
			window.location.href="#/"+$scope.job.id;
			$scope.requestList = false;
		}, function(response) {
			$scope.errorMessageRequest = "Error with status code: " + response.status;
			$scope.requestList = false;
		});
		
	};
	
//	$scope.loadJob = function(id) {
//		$scope.requestList = true;
//		$scope.errorMessageRequest = false;
////		$scope.noArticlesFound = false;
//		CrowdRestangular.one('job', id).get().then(function(job) {
//			$scope.requestList = false;
//			$scope.job = job;
//			console.log(job);
//			// to prevent changing view while changing search input
//			$scope.name = $scope.job.name; 
//		}, function(response) {
//			$scope.errorMessageRequest = "Error with status code: " + response.status;
//			$scope.requestList = false;
//		});
//	};
	
	$scope.showState = function(state) {
		return $scope.job && ($scope.job.state == state && !$scope.noArticlesFound());
	};
	
	$scope.noArticlesFound = function() {
		return $scope.job && (($scope.job.articles &&$scope.job.articles.length == 0) && $scope.job.state != 'CREATED');
	};

	$scope.alertInProgress = function() {
		alert('search is still in progress');
	};

	$scope.showLoading = function(index) {
		return $scope.requestList && (index == $scope.selectedIndex);
	};

	// DBPedia Suggestions
	$scope.showSuggestions = function() {

		$scope.selectedIndex = -1;
		var searchItem = 'Thing';
		if ($scope.action == 'Company') {
			searchItem = 'Company';
		} else if ($scope.action == 'Organisation') {
			searchItem = 'Organisation';
		}
		 
		var resource = DBPediaRestangular.all(
				'KeywordSearch?QueryClass=' + searchItem + 
				'&QueryString=' + $scope.job.name
				);
		
		resource.getList().then(function(results) {
			$scope.suggestions = results;
			if (results.results.length > 0) {
				$scope.suggestionError = undefined;
			} else {
				$scope.suggestionError = true;
			}
		});
	};
	
}


function JobCtrl(CrowdRestangular, DBPediaRestangular, CommonService, $scope, $http, $routeParams, $route) {
	// functions
	$scope.formatDate = CommonService.formatDate;
	
	// preset variables
	$scope.selectedIndex = -1;
	
	$scope.errorMessageRequest = false;
	
//	if($routeParams.id) {
//		$scope.loadJob($routeParams.id);
//	} else {
//		if($scope.job) {
//			$scope.job.name = undefined;
//			$scope.job.articles = undefined;
//			$scope.job.state = undefined;
//			$scope.searchName = undefined;
//		}
//	}

	// Interceptors
	CrowdRestangular.setRequestInterceptor(function(elem, operation, what) {
		// remove error message if exists
		$scope.errorMessageRequest = false;
//		$scope.noArticlesFound = false;
		// to show loading icon
		$scope.requestList = true;
	});

	CrowdRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.requestList = false;
	});

	$scope.selectSuggestion = function(index) {
		$scope.job.name = $scope.suggestions.results[index].label;
		$scope.suggestions = false;
//		var resource = CrowdRestangular.one('article', $scope.searchName);
//		resource.getList().then(function(articles) {
//			$scope.articles = articles;
//		});
	};
	
//	$scope.createJob = function() {
//		$scope.requestList = true;
//		var body = {
//				name: $scope.job.name
//		};
//		
//		// restangular-post seems broken, doesn't send body
//		$http.post("./rest/job", body).then(function(job) {
//			$scope.job = job.data;
//			// don't use html5-history, causes endless loop
//			window.location.href="#/"+$scope.job.id;
//			$scope.requestList = false;
//		}, function(response) {
//			$scope.errorMessageRequest = "Error with status code: " + response.status;
//			$scope.requestList = false;
//		});
//		
//	};
	
	$scope.loadJob = function(id) {
		$scope.requestList = true;
		$scope.errorMessageRequest = false;
//		$scope.noArticlesFound = false;
		CrowdRestangular.one('job', id).get().then(function(job) {
			$scope.requestList = false;
			$scope.job = job;
			console.log(job);
			// to prevent changing view while changing search input
			$scope.name = $scope.job.name; 
			$scope.stateInfo = $scope.setStateInfo();
		}, function(response) {
			$scope.errorMessageRequest = "Error with status code: " + response.status;
			$scope.requestList = false;
		});
	};
	
	$scope.loadJob($routeParams.id);
	
	$scope.noArticlesFound = function() {
		return $scope.job && (($scope.job.articles &&$scope.job.articles.length == 0) && $scope.job.state != 'CREATED');
	};
	
	$scope.showState = function() {
		return !$scope.noArticlesFound();
	};
	
	$scope.setStateInfo = function() {
		if (!$scope.job) return;
		var state = $scope.job.state;
		var info = "Job from " + $scope.formatDate($scope.job.date);
		if(state == 'CREATED') {
			return info + " is created!";
		} else if (state == "PREPARED") {
			return info + " is prepared for workers!";
		} else if (state == "ASSIGNED") {
			return info + " is assigned to workers!";
		} else if (state == "FINISHED") {
			return info + " is finished!";
		}
	};
	
	$scope.alertInProgress = function() {
		alert('search is still in progress');
	};

	$scope.showLoading = function(index) {
		return $scope.requestList && (index == $scope.selectedIndex);
	};

	// DBPedia Suggestions
	$scope.showSuggestions = function() {

		$scope.selectedIndex = -1;
		var searchItem = 'Thing';
		if ($scope.action == 'Company') {
			searchItem = 'Company';
		} else if ($scope.action == 'Organisation') {
			searchItem = 'Organisation';
		}
		 
		var resource = DBPediaRestangular.all(
				'KeywordSearch?QueryClass=' + searchItem + 
				'&QueryString=' + $scope.job.name
				);
		
		resource.getList().then(function(results) {
			$scope.suggestions = results;
			if (results.results.length > 0) {
				$scope.suggestionError = undefined;
			} else {
				$scope.suggestionError = true;
			}
		});
	};
	
}

function JobListCtrl(CrowdRestangular, CommonService, $scope) {
	
	$scope.length = function(list) {
		return list.length;
	};
	
	$scope.loadJobList = function() {
		$scope.requestLoading = true;
		var resource = CrowdRestangular.all('job/list');
		resource.getList().then(function(jobs) {
			$scope.jobs = jobs;
			$scope.waitForRequest = false;
		});
		
	};
	
	$scope.loadJobList();
	
	$scope.formatDate = CommonService.formatDate;
}

