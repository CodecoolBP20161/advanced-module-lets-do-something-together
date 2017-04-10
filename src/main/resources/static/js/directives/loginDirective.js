'use strict';

actimate.directive('loginDirective', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/login_form.html',
            replace : true,
            controller: 'loginCtrl'
        }
    });