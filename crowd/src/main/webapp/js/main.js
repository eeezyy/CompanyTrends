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
	$routeProvider.when('/quality/list', { templateUrl: 'templates/quality.html', controller: 'QualityCtrl' });
	$routeProvider.when('/info/about', { templateUrl: 'templates/about.html', controller: 'AboutCtrl' });
	$routeProvider.when('/info/contact', { templateUrl: 'templates/contact.html', controller: 'ContactCtrl' });
}]);

app.factory('CommonService', function() {
	var formatDate = function(timeInMillis) {
		var date = new Date(parseInt(timeInMillis, 10));
		return date.toGMTString();
	};
	
	return {
		formatDate: formatDate,
	};
});

// Configuration for Crowd REST-API
app.factory('CrowdRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('rest');
	});
	
});

//Configuration for Crowd REST-API and Quality Resources, because of special Interceptor
app.factory('QualityRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('rest');
	});
	
});

// Configuraiton for DBPedia API
app.factory('DBPediaRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('http://lookup.dbpedia.org/api/search.asmx');
	});
});

function MainCtrl($route, $routeParams, $location) {
	this.$route = $route;
	this.$location = $location;
	this.$routeParams = $routeParams;
}

function SearchCtrl(CrowdRestangular, DBPediaRestangular, CommonService, $scope, $http, $routeParams, $route) {
	// init common functions
	$scope.formatDate = CommonService.formatDate;
	
	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	$scope.showSuggestionLoading = function() {
		return $scope.loadingSuggestion;
	};
	
	DBPediaRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loadingSuggestion = true;
	});

	DBPediaRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loadingSuggestion = false;
	});

	$scope.selectSuggestion = function(index) {
		$scope.name = $scope.suggestions.results[index].label;
		// remove suggestion-list
		$scope.suggestions = false;
	};
	
	$scope.createJob = function() {
		// check if name is empty
		if($scope.name == undefined || $scope.name.replace(/^\s+|\s+$/g, '') == '') {
			alert('Name is empty!');
			$scope.name = undefined;
			return;
		}
		
		// json post-body
		var body = {
				name: $scope.name
		};
		
		// problems with request interceptor, so we put it here
		$scope.loading = true;
		$scope.errorMessageRequest = false;
		
		// restangular-post seems broken, doesn't send body
		$http.post("./rest/job", body).then(function(job) {
			// must be data property, because its not restangular!
			$scope.job = job.data;
			// don't use html5-history, causes endless loop
			window.location.href="#/"+$scope.job.id;
			$scope.loading = false;
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
		
	};
	
	$scope.alertInProgress = function() {
		alert('search is still in progress');
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
				'&QueryString=' + $scope.name
				);
		
		resource.getList().then(function(results) {
			$scope.suggestions = results;
			if (results.results.length > 0) {
				$scope.suggestionError = undefined;
			} else {
				$scope.suggestionError = true;
			}
			$scope.loadingSuggestion = false;
		}, function(response) {
			$scope.loadingSuggestion = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
	};
	
}


function JobCtrl(CrowdRestangular, DBPediaRestangular, CommonService, $scope, $http, $routeParams, $route) {
	// init common functions
	$scope.formatDate = CommonService.formatDate;
	
	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	// Interceptors
	CrowdRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loading = true;
	});

	CrowdRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loading = false;
	});

	$scope.loadJob = function() {
		CrowdRestangular.one('job', $routeParams.id).get().then(function(job) {
			$scope.loading = false;
			$scope.job = job;
			$scope.stateInfo = $scope.setStateInfo();
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
	};
	
	$scope.loadJob();
	
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
	
	$scope.convertToPercent = function(value) {
		return Math.round(value * 100);
	};
	
	$scope.formatTwoDecimalsAndRound = function(value) {
		return Math.round(value * 100)/100;
	};
	
}

function JobListCtrl(CrowdRestangular, CommonService, $scope) {
	// init common functions
	$scope.formatDate = CommonService.formatDate;

	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	// Interceptors
	CrowdRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loading = true;
	});

	CrowdRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loading = false;
	});
	
	$scope.length = function(list) {
		return list.length;
	};
	
	$scope.loadJobList = function() {
		var resource = CrowdRestangular.all('job/list');
		resource.getList().then(function(jobs) {
			$scope.loading = false;
			$scope.jobs = jobs;
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
		
	};
	
	$scope.loadJobList();
}

function QualityCtrl(QualityRestangular, $scope) {
	
	$scope.showLoading = function() {
		return $scope.loading;
	};
	
	// Interceptors
	QualityRestangular.setRequestInterceptor(function(elem, operation, what) {
		// reset error
		$scope.errorMessageRequest = false;
		$scope.loading = true;
	});
	
	QualityRestangular.setResponseExtractor(function(response, operation, what, url) {
		// getting map in separate scope by converting it to list
		var output = [], item;

		for (var type in response) {
		    item = {};
		    item.userId = type;
		    item.value = response[type];
		    output.push(item);
		}
		return output;
	});


	QualityRestangular.setErrorInterceptor(function(elem, operation, what) {
		// show error panel
		$scope.errorMessageRequest = elem.data;
		$scope.loading = false;
	});
	
	$scope.loadDistances = function() {
		var resource = QualityRestangular.all('quality/distance');
		resource.getList().then(function(distances) {
			$scope.loading = false;
			$scope.distances = distances;
		}, function(response) {
			$scope.loading = false;
			$scope.errorMessageRequest = "Error with status code: " + response.status;
		});
		
	};
	
	$scope.loadDistances();
	
	$scope.loadTendencies = function() {
		var resource = QualityRestangular.all('quality/tendency');
		resource.getList().then(function(tendencies) {
			$scope.loading = false;
			$scope.tendencies = tendencies;
		}, function(response) {
			$scope.loading = false;
		});
		
	};
	
	$scope.loadTendencies();
	
	$scope.reload = function() {
		$scope.loadDistances();
		$scope.loadTendencies();
	};
}

