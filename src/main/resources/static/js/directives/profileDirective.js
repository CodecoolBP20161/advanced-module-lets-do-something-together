'use strict';

var actimate = angular.module('actimate', ['ngResource', 'gm']);
actimate.config(['$httpProvider', '$qProvider', function ($httpProvider, $qProvider) {
    $httpProvider.defaults.useXDomain = true;
    $qProvider.errorOnUnhandledRejections(false);
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

actimate.directive('profileController', function () {
    return {
        controller: function ($scope, $http) {
            $scope.user = {};

            var checkboxes = $("input[type='checkbox']"),
                submitButton = $("input[type='submit']");

            checkboxes.click(function () {
                submitButton.attr("disabled", !checkboxes.is(":checked"));
            });

            $scope.saveProfile = function () {

                $scope.user.interest = checkInterests();

                $http({
                    method: 'POST',
                    url: '/u/edit-profile',
                    headers: {'Content-Type': 'application/json; charset=UTF-8'},
                    data: JSON.stringify($scope.user)
                });
                $scope.user = null;
            };

            $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
                $scope.user.location = $scope.autocomplete.getPlace().formatted_address;
                $scope.$apply();
            });
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
                    $scope.autocomplete = response.data.profile.location;
                })
        }
    };
});
