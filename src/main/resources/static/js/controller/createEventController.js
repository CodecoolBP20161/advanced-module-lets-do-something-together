'use strict';

actimate.run(function (defaultErrorMessageResolver) {
            defaultErrorMessageResolver.getErrorMessages().then(function (errorMessages) {
                errorMessages['wrongNumber'] = 'Please invite at least 1 person!';
                errorMessages['wrongInputType'] = 'Please enter letters only!';
            });
        })

    .controller('eventCtrl', function ($scope, $http) {
        $scope.event = {};

        $('#datetimepicker1').datetimepicker({
            format:'DD/MM/YYYY HH:mm a',
            minDate: moment().add(1, 'h')
        });

        $('#datetimepicker1').on('dp.change', function (data) {
            $scope.event.date = moment(data.date).format("DD/MM/YYYY HH:mm a");
            $scope.$apply();
        });

        $scope.saveEvent = function() {

            $http({
                method: 'POST',
                url: '/u/create_event',
                headers: {'Content-Type': 'application/json; charset=UTF-8'},
                data: JSON.stringify($scope.event)
            })
                .then(function (response) {
                    console.log(response);
                })
        };

        $scope.$on('gmPlacesAutocomplete::placeChanged', function () {
            var location = $scope.autocomplete.getPlace().geometry.location;
            $scope.event.lat = location.lat();
            $scope.event.lng = location.lng();
            $scope.event.location = $scope.autocomplete.getPlace().formatted_address;
            $scope.$apply();
        });


        $scope.getInterest = function() {

            $scope.listOfInterests = null;

            $http.get("/interests")
                .then(function (response) {
                    $scope.listOfInterests = response.data;
                })
        };

        $scope.getInterest();
    });
