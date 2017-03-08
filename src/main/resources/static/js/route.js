'use strict';

angular.module('actimate')

    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'index.html'
            })
            .when('/profile', {
                templateUrl: 'profile.html'
            })
            .when('/usermain', {
                templateUrl: 'user_main.html'
            })
            .when('/registration', {
                templateUrl: 'registration.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    });