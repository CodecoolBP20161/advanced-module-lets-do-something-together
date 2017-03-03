'use strict';

var mainApp = angular.module("actimate", ['ngRoute']);

mainApp.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'main.html'
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
            controller: 'viewProfileController',
            templateUrl: 'profile.html'

        })
        .otherwise({
            redirectTo: '/'
        });
});


mainApp.controller('profileController', function($scope) {
});

mainApp.controller('registerController', function ($scope) {

});

mainApp.controller('loginController', function ($scope) {

});