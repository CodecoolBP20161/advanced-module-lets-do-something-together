'use strict';

actimate.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'index.html'
        })
        .when('/u/profile', {
            templateUrl: 'profile.html'
        })
        .when('/u/dashboard', {
            templateUrl: 'user_main.html',
            controller: "loadEventCtrl"
        })
        .when('/registration', {
            templateUrl: 'registration.html'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);