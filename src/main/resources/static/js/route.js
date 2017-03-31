'use strict';

actimate.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('', {
            templateUrl: 'main.html',
            controller: 'mainCtrl'
        })
        .when('/u/profile', {
            templateUrl: 'profile.html',
            controller: "loadUserCtrl"
        })
        .when('/u/dashboard', {
            template: 'user_main.html',
            controller: "loadEventCtrl"
        })
        .when('/u/edit-profile', {
            templateUrl: 'profile_form.html',
            controller: 'profileController'
        })
        .when('/u/create_event', {
            templateUrl: 'create_event.html',
            controller: "saveEventCtrl"
        })
        .when('/registration', {
            template: 'registration.html',
            controller: "userCtrl"
        })
        .when('/login', {
            templateUrl: 'login_form.html'
            //controller: "userCtrl"
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

//actimate.run();