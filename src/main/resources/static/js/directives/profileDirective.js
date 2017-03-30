'use strict';

var actimate = angular.module('actimate',['ngResource']);
actimate.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.directive('profileController', function() {
    return {
        controller: function ($scope, $http) {
            $scope.user = {};

            var checkboxes = $("input[type='checkbox']"),
                submitButton = $("input[type='submit']");

            checkboxes.click(function() {
                submitButton.attr("disabled", !checkboxes.is(":checked"));
            });
            
            $scope.saveProfile = function () {

                $http({
                    method: 'POST',
                    url: '/u/edit-profile',
                    headers: {'Content-Type': 'application/json; charset=UTF-8'},
                    data: JSON.stringify($scope.user)
                })
                    .then($scope.loadUserDetails = function ($scope, $http) {
                        $scope.user = null;
                        $http.get("/u/profile_data")
                            .then(function (response) {
                                $scope.user = response.data;
                            })
                    }, function (error) {
                        console.log('Error: ', error)
                    });
            };
        }
    };
});

actimate.directive('loadUserCtrl', function () {
    return {
        controller: function ($scope, $http) {
            $scope.user = null;

            $scope.listofInterests = null;

            $http.get("/u/profile_data")
                .then(function (response) {
                    $scope.user = response.data.profile;
                    updateInterests(response.data.profile.interestList);
                })
        }
    };
});
