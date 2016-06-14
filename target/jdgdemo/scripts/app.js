var app = angular.module('task', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);
 
app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/nodes.html',
        controller: 'ListCtrl'
    }).when('/', {
        templateUrl: 'views/list.html',
        controller: 'ListCtrl'
    }).when('/create', {
        templateUrl: 'views/create.html',
        controller: 'CreateCtrl'
    }).when('/load', {
        templateUrl: 'views/load.html',
        controller: 'CreateCtrl'
    }).when('/remove', {
        templateUrl: 'views/remove.html',
        controller: 'CreateCtrl'
    }).when('/search', {
        templateUrl: 'views/search.html',
        controller: 'CreateCtrl'
    }).otherwise({
        redirectTo: '/'
    });
});
 
app.controller('ListCtrl', function ($scope, $http) {
    $http.get('/jdgdemo/rest/tasks').success(function (data) {
        $scope.tasks = data;
    }).error(function (data, status) {
        console.log('Error ' + data);
    });
 
  
    

});
 
app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.task = {
        done: false
    };
 
    $scope.createTask = function () {
        console.log($scope.task);
        $http.post('/jdgdemo/rest/tasks', $scope.task).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data);
        });
        
        
    };
    $scope.loadTasks = function () {
        console.log($scope.task);
        $http.post('/jdgdemo/rest/loadtasks', $scope.quantity).success(function (data) {
            $location.path('/');
            console.log('Loading tasks');
        }).error(function (data, status) {
            console.log('Error ' + data);
        });
    };
    
    $scope.removeTasks = function () {
        console.log($scope.task);
        $http.post('/jdgdemo/rest/remove', $scope.task).success(function (data) {
            $location.path('/');
            console.log('Removing tasks');
        }).error(function (data, status) {
            console.log('Error ' + data);
        });
    };
    
    $scope.searchTasks = function () {
        console.log($scope.task);
        var dataObj = {
        	    filter : true,
        	    keyword : $scope.keyword        	   
        	}; 
        
        var res = $http.get('/jdgdemo/rest/search/true/'+$scope.keyword, dataObj);
        res.success(function(data) {
        	 $scope.tasks = data;               
        });
          res.error(function(data) {
           alert( "failure message: " + JSON.stringify({data: data}));
         });  
        
       };

   
          
    
});
