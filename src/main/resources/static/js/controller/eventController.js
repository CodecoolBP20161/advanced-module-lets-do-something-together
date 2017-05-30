'use strict';

actimate.controller("eventCtrl", function ($scope, $http) {

    var hideButton = $('.hide-chat');
    var showButton = $('.show-chat');
    var iframe = $('.iframe-chat');


    $scope.showChat = function () {
        showButton.hide();
        iframe.show();
        hideButton.show();
    };

    $scope.hideChat = function () {
        showButton.show();
        hideButton.hide();
        iframe.hide();
    };

    $scope.hideChat();


    $scope.getEventDetails = function () {

        $scope.event = null;
        $scope.user = null;

        const split = window.location.href.split('/');
        const id = split[split.length - 1];

        $http.get('u/event/' + id)
            .then(function (res) {
                $scope.event = res.data;
            });

        $http.get('u/profile_data')
            .then(function (res) {
                $scope.user = res.data;
                let firstname = $scope.user.profile.firstName;
                let lastname = $scope.user.profile.lastName;
                let url = "http://0.0.0.0:9090/?room=" + id + "&firstname=" + firstname + "&lastname=" + lastname;
                iframe.attr("src", url);
            });
    };

    $scope.getEventDetails();

});
