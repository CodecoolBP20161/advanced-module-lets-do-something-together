'use strict';

actimate.controller('mainCtrl', function ($http) {
    $http({
        method: 'GET',
        url: '/',
        withCredentials: true,
        headers: {'Content-Type': 'charset=UTF-8'},
        data: 'templates/main.html'
    })
});
