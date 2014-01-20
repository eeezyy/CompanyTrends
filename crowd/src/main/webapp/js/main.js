var rest =angular.module('crowdsourcing',["restangular"]);
            
            // Global configuration
            rest.config(["RestangularProvider",function(RestangularProvider){
                RestangularProvider.setRestangularFields({
                        id: "_id"
                });
        	}]);
            
            // Configuration for Cloud REST-API
            rest.factory('CloudRestangular', function(Restangular) {
            	return Restangular.withConfig(function(RestangularConfigurer) {
            		RestangularConfigurer.setBaseUrl('rest')
            	});
            });
            
            // Configuraiton for DBPedia API
			rest.factory('DBPediaRestangular', function(Restangular) {
            	return Restangular.withConfig(function(RestangularConfigurer) {
            		RestangularConfigurer.setBaseUrl('http://lookup.dbpedia.org/api/search.asmx')
            	});
            });
            
			rest.controller("MainCtrl",["CloudRestangular","DBPediaRestangular","$scope",function(CloudRestangular,DBPediaRestangular,$scope){
				// preset variables
				$scope.selectedIndex = -1;
				
	            // Interceptors
	            CloudRestangular.setRequestInterceptor(function(elem, operation, what) {
	            	// remove error message if exists
	            	$scope.errorMessageRequest = false;
	            	$scope.noArticlesFound = false;
	            	// to show loading icon
	            	$scope.requestList = true;	
	            });
	            
	            CloudRestangular.setErrorInterceptor(function(elem, operation, what) {
	            	// show error panel
	            	$scope.errorMessageRequest = elem.data;
	            	$scope.requestList = false;	
// 	            	for(a in elem)console.log(a);
// 	            	console.log("op" + operation);
// 	            	console.log("what" + what);
	            });
	            
	            CloudRestangular.setResponseInterceptor(function(data, operation, what) {
	            	$scope.requestList = false;
	            });
	            
	            // if response list is empty, transform result so no error will be thrown
	            CloudRestangular.setResponseExtractor(function(response, operation, what, url) {
	            	$scope.requestList = false;
	            	if (operation === "getList" && (response === null || response === "null")) {
	            		$scope.noArticlesFound = true;
	            		return "{ results: [] }";
	            	}
	            	return response;
	            });
	            
	         // Cloud REST-API
//				$scope.getArticles = function(index) {
//	            	$scope.selectedIndex = index;
//// 	            	console.log('index: ' + index);
//// 	            	console.log(JSON.stringify($scope.suggestions.results[0]));
//	        	 	var resource = CloudRestangular.all('article/' + $scope.suggestions.results[index].label);
//	                resource.getList().then(function(articles){
//	                        $scope.articles = articles;
//	                        console.log(articles);
//	                });
//	            }
	            
	            $scope.getArticles = function(index) {
	            	$scope.searchName = $scope.suggestions.results[index].label;
	            	$scope.suggestions = false;
	        	 	var resource = CloudRestangular.all('article/' + $scope.searchName);
	                resource.getList().then(function(articles){
	                        $scope.articles = articles;
	                        console.log(articles);
	                });
	                window.history.pushState({searchName:$scope.searchName}, "Searched for " + $scope.searchName, "?id=" + $scope.searchName);
	            };
				
				$scope.alertInProgress = function() {
					alert('search is still in progress');
				};
				
				$scope.showLoading = function(index) {
					return $scope.requestList && (index == $scope.selectedIndex);
				};
				
				$scope.formatDate = function(timeInMillis) {
					var date = new Date(parseInt(timeInMillis,10));
					return date.toGMTString();
				};
	            
	            // DBPedia Suggestions
	            $scope.showSuggestions = function() {
	            	$scope.selectedIndex = -1;
                	var searchItem = 'Thing';
                	if ($scope.action == 'Company') {
                		searchItem = 'Company';
                	} else if ($scope.action == 'Organisation'){
                		searchItem = 'Organisation';
                	}
                	
                	var resource = DBPediaRestangular.all('KeywordSearch?QueryClass=' + searchItem + '&QueryString=' + $scope.searchName);
                    resource.getList().then(function(results){
                            $scope.suggestions = results;
                            if(results.results.length > 0) {
                            	$scope.suggestionError = undefined;
                            } else {
                            	$scope.suggestionError = true;
                            }
                    });
                };
			}]);
