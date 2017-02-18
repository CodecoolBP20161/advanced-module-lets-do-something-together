'use strict';

var actimate = angular.module("actimate", ['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);
actimate.controller("UserCtrl", function($scope, $http) {
    $scope.user = {};
    $scope.errorConfirm = false;

    $scope.addUser = function(valid){
        if(valid) {
            $http({
                method: 'POST',
                url: '/registration',
                //withCredentials:true,
                headers: {'Content-Type': 'application/json; charset=UTF-8'},
                data: angular.toJson($scope.user)
            })
                .success(function (response) {
                    if (response == "fail") {
                        $('.email-error-message').text("Email address is already in use").fadeIn();
                    } else {
                        $('.success-message').text("You have successfully registered").fadeIn();
                    }
                    console.log(response);
                })
                .error(function (response) {
                    alert("failure message: " + angular.toJson($scope.user));
                });
        }
        // Making the fields empty
        $scope.user = null;
    };
});
