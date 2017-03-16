'use strict';

angular.module('actimate')

    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'index.html'
            })
            .when('/u/profile', {
                templateUrl: 'profile.html'
            })
            .when('/u/dashboard', {
                templateUrl: 'user_main.html'
            })
            .when('/registration', {
                templateUrl: 'registration.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    });