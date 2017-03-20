'use strict';

actimate.config(['$routeProvider',function($routeProvider) {


    $routeProvider
        .when('/', {
            templateUrl: 'index.html'
        })
        .when('/profile', {
            templateUrl: 'profile.html'
        })
        .when('/login', {
            templateUrl: 'login_form.html'
        })
        .when('/usermain', {
            templateUrl: 'user_main.html'
        })
        .when('/registration', {
            templateUrl: 'registration.html',
            controller: 'UserCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);
