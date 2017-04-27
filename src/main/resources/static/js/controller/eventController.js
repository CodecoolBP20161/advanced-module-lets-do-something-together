'use strict';

actimate.run(function (defaultErrorMessageResolver) {
            defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
                errorMessages['wrongNumber'] = 'Please invite at least 1 person!';
                errorMessages['wrongInputType'] = 'Please enter letters only!';
            });
        })

    .controller('saveEventCtrl', function ($scope, $http) {
        $scope.event = {};

            $('#datetimepicker1').on('dp.change', function (data) {
                $scope.event.date = moment(data.date).format("DD/MM/YYYY HH:mm a");
                $scope.$apply();
            });

            $scope.saveEvent = function () {

                $http({
                    method: 'POST',
                    url: '/u/create_event',
                    headers: {'Content-Type': 'application/json; charset=UTF-8'},
                    data: JSON.stringify($scope.event)
                })
                    .then(function (response) {
                        console.log($scope.event);
                    })
            };

            $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
                var location = $scope.autocomplete.getPlace().geometry.location;
                $scope.event.lat = location.lat();
                $scope.event.lng = location.lng();
                $scope.event.location = $scope.autocomplete.getPlace().formatted_address;
                $scope.$apply();
            });
    });
