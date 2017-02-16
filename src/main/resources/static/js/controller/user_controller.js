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
                })
                .error(function (response) {
                    alert("failure message: " + angular.toJson($scope.user));
                });
        }
        // Making the fields empty
        $scope.user = null;
    };

    $scope.compareTo = function(){
        return {
            require: "ngModel",
            scope: {
                otherModelValue: "=compareTo"
            },
            link: function(scope, element, attributes, ngModel) {

                ngModel.$validators.compareTo = function(modelValue) {
                    return modelValue == scope.otherModelValue;
                };

                scope.$watch("otherModelValue", function() {
                    ngModel.$validate();
                });
            }
        };
    }
});
