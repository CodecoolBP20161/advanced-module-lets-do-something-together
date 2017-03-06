'use strict';


angular.module('actimate', ['ngRoute']).config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);

