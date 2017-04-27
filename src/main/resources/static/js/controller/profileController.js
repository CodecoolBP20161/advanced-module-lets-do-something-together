'use strict';



actimate.controller("profileCtrl", function ($scope, $http) {

    $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
        $scope.user.location = $scope.autocomplete.getPlace().formatted_address;
        $scope.$apply();
    });

    var checkboxes = $("input[type='checkbox']"),
        submitButton = $("input[type='submit']");

    checkboxes.click(function () {
        submitButton.attr("disabled", !checkboxes.is(":checked"));
    });


    $scope.saveProfile = function () {
        
        // $scope.user.interest = checkInterests();
        console.log( $scope.user.interest);

        $http({
            method: 'POST',
            url: '/u/edit-profile',
            headers: {'Content-Type': 'application/json; charset=UTF-8'},
            data: JSON.stringify($scope.user)
        });
        console.log($scope.user);
        $scope.user = null;
    };

    $scope.loadProfile = function () {

        $scope.user = null;

        $scope.listofInterests = null;

        $http.get("/u/profile_data")
            .then(function (response) {
                $scope.user = response.data.profile;
                updateInterests(response.data.profile.interestList);
                $scope.autocomplete = response.data.profile.location;
            })
    };

    $scope.locateUser = function () {

    };

    $scope.loadProfile();

});