'use strict';

var mainApp = angular.module("mainApp", ['ngRoute']);

mainApp.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'index.html'
        })
        .when('/registration', {
             templateUrl: 'registration.html',
             controller: 'registerController'
        })
        .when('/login', {
            templateUrl: 'login_form.html',
            controller: 'loginController'
        })
        .when('/profile', {
            resolve: {
                "check": function ($location, $rootScope) {
                    if(!$rootScope.loggedIn){
                        $location.path('/');
                    }
                }
            },
            templateUrl: 'profile.html'

        })
        .when('/mission', {
            templateUrl: 'mission.html',
            controller: 'missionController'
        })
        .otherwise({
            redirectTo: '/'
        });
});

mainApp.controller('missionController', function($scope) {
    $scope.message = "No Mission Yet."
});

mainApp.controller('profileController', function($scope) {
});

mainApp.controller('registerController', function ($scope) {

});