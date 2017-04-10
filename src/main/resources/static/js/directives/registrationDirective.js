'use strict';

actimate.directive('registrationDirective', function () {
        return {
            restrict: 'E',
            templateUrl: 'registration.html',
            replace : true,
            controller: 'userCtrl'
        }
    });