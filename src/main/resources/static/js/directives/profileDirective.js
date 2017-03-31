'use strict';

actimate.run(function(defaultErrorMessageResolver){
        defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
            errorMessages['wrongInputType'] = 'Please enter letters only!';
        });
    }
);

actimate.directive('profileController', function() {
    return {
        controller: function ($scope, $http) {
            $scope.user = {};
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