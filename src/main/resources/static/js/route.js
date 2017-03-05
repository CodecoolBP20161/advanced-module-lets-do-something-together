'use strict';

angular.module('actimate')

    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'main.html'
            })
            .when('/profile', {
                templateUrl: 'profile.html'
            })
            .when('/main', {
                templateUrl: 'user_main.html'
            })
            .when('/registration', {
                templateUrl: 'registration.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    });