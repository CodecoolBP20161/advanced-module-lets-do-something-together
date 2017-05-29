'use strict';

const actimate = angular.module('actimate', [
    'ngRoute',
    'ngResource',
    'gm',
    'jcs-autoValidate'
])

    .config(['$httpProvider', '$routeProvider', '$locationProvider',
        function ($httpProvider, $routeProvider, $locationProvider) {
            $httpProvider.defaults.withCredentials = true;
            $httpProvider.defaults.headers['Content-Type'] = 'application/json; charset=UTF-8';
            $httpProvider.defaults.useXDomain = true;
            delete $httpProvider.defaults.headers.common['X-Requested-With'];
            $httpProvider.defaults.headers.post["Content-Type"] = "application/json";

            $locationProvider
                .html5Mode(true)
                .hashPrefix('!');

            $routeProvider
                .when('/', {
                    templateUrl: 'views/main.html',
                    controller: 'mainCtrl'
                })
                .when('/registration', {
                    templateUrl: 'views/registration.html',
                    controller: 'userCtrl'
                })
                .when('/login', {
                    templateUrl: 'views/login_form.html'
                    //controller: 'loginCtrl'
                })
                .when('/u/profile', {
                    templateUrl: 'views/profile.html',
                    controller: 'profileCtrl'
                })
                .when('/u/dashboard', {
                    templateUrl: 'views/dashboard.html',
                    controller: 'dashboardCtrl'
                 })
                .when('/u/edit-profile', {
                    templateUrl: 'views/profile_form.html',
                    controller: 'profileCtrl'
                })
                .when('/u/create_event', {
                    templateUrl: 'views/create_event.html',
                    controller: 'createEventCtrl'
                })
                .when('/u/event/:id', {
                    templateUrl: 'views/event.html',
                    controller: 'eventCtrl'
                })
                .otherwise({
                    redirectTo: '/'
                })
        }])

    .controller('navCtrl', ['$scope', '$location', function($scope, $location) {
        $scope.currentPath = $location.path();
    }]);

