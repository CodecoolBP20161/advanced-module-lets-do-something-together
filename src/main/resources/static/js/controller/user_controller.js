'use strict';

var actimate = angular.module("actimate", ['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);
actimate.controller("UserCtrl", function($scope, $http) {
    $scope.user = {}
    ;
    console.log($scope.user);
    $scope.addRow = function(){
        /*$scope.users.push({ 'email':$scope.email, 'password': $scope.password, 'password_again':$scope.password_again });
        $scope.email='';
        $scope.password='';
        $scope.password_again='';
*/
        $http.post('/registration', $scope.user)
            .success(function(data, status, headers, config) {
                $scope.message = data;
            })
            .error(function(data, status, headers, config) {
                alert( "failure message: " + $scope.user);
            });
        // Making the fields empty
        //
        $scope.email='';
        $scope.password='';
        $scope.password_again='';
    };

    });