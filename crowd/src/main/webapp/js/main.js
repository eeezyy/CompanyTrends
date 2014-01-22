var rest = angular.module('crowdsourcing', [ 'restangular', 'ngRoute' ]);

// Global configuration
rest.config([ "RestangularProvider", function(RestangularProvider) {
	RestangularProvider.setRestangularFields({
		id : "id"
	});
}]);

rest.config([ "$routeProvider", "$locationProvider", function($routeProvider, $locationProvider) {
	$routeProvider.when('/:id', { templateUrl: 'index.html', controller: 'MainCtrl' });
}]);

// Configuration for Crowd REST-API
rest.factory('CrowdRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer.setBaseUrl('rest');
	});
});

// Configuraiton for DBPedia API
rest.factory('DBPediaRestangular', function(Restangular) {
	return Restangular.withConfig(function(RestangularConfigurer) {
		RestangularConfigurer
				.setBaseUrl('http://lookup.dbpedia.org/api/search.asmx');
	});
});

rest.controller("MainCtrl", ["CrowdRestangular", "DBPediaRestangular", "$scope", "$http", "$routeParams",
		function(CrowdRestangular, DBPediaRestangular, $scope, $http, $routeParams) {
			// preset variables
			$scope.selectedIndex = -1;
			
			if($routeParams.id) {
				$scope.loadJob($routeParams.id);
			}

			// Interceptors
			CrowdRestangular.setRequestInterceptor(function(elem, operation, what) {
				// remove error message if exists
				$scope.errorMessageRequest = false;
				$scope.noArticlesFound = false;
				// to show loading icon
				$scope.requestList = true;
			});

			CrowdRestangular.setErrorInterceptor(function(elem, operation, what) {
				// show error panel
				$scope.errorMessageRequest = elem.data;
				$scope.requestList = false;
			});

			// if response list is empty, transform result so no error will be
			// thrown
//			CrowdRestangular.setResponseExtractor(function(response, operation, what, url) {
//				$scope.requestList = false;
//				if (operation === "getList"
//						&& (response === null || response === "null")) {
//					$scope.noArticlesFound = true;
//					return { results: [] };
//				}
//				return response;
//			});

			// Crowd REST-API
//			$scope.getArticles = function(index) {
//				$scope.searchName = $scope.suggestions.results[index].label;
//				$scope.suggestions = false;
//				var resource = CrowdRestangular.one('article', $scope.searchName);
//				resource.getList().then(function(articles) {
//					$scope.articles = articles;
//				});
//			};
			
			$scope.selectSuggestion = function(index) {
				$scope.job.name = $scope.suggestions.results[index].label;
				$scope.suggestions = false;
//				var resource = CrowdRestangular.one('article', $scope.searchName);
//				resource.getList().then(function(articles) {
//					$scope.articles = articles;
//				});
			};
			
			$scope.createJob = function() {
				var body = {
						name: $scope.job.name
				};
				
				// TODO check if angularjs request is compatible (e.g. interceptors) with restangular
				// restangular-post seems broken, doesn't send body
				$http.post("./rest/job", body).then(function(job) {
					$scope.job = job.data;
					// don't use html5-history, causes endless loop
					window.location.href="#/"+$scope.job.id;
				});
				
			};
			
			$scope.loadJob = function(id) {
				CrowdRestangular.one('job', id).get().then(function(job) {
					$scope.requestList = false;
					console.log(job);
					$scope.job = job;
					if($scope.job.articles.length == 0 && $scope.job.state=='FINISHED') {
						$scope.noArticlesFound = true;
					}
					// to prevent changing view while changing search input
					$scope.name = $scope.job.name; 
				});
			};

			$scope.alertInProgress = function() {
				alert('search is still in progress');
			};

			$scope.showLoading = function(index) {
				return $scope.requestList && (index == $scope.selectedIndex);
			};

			$scope.formatDate = function(timeInMillis) {
				var date = new Date(parseInt(timeInMillis, 10));
				return date.toGMTString();
			};

			// DBPedia Suggestions
			$scope.showSuggestions = function() {
//				if($scope.job == undefined) {
//					return;
//				}
				
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
		} ]);

