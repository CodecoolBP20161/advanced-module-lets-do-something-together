
var helloApp = angular.module("helloApp", ['ngResource']);
helloApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);
helloApp.controller("UserCtrl", function($scope, $http) {
    $scope.users = [
        { 'email':'Infosys Technologies',
            'password': 125000,
            'password_again': 'Bangalore'},
        { 'email':'Cognizant Technologies',
            'password': 100000,
            'password_again': 'Bangalore'},
        { 'email':'Wipro',
            'password': 115000,
            'password_again': 'Bangalore'},
        { 'email':'Tata Consultancy Services (TCS)',
            'password': 150000,
            'password_again': 'Bangalore'},
        { 'email':'HCL Technologies',
            'password': 90000,
            'password_again': 'Noida'},
    ];
    $scope.addRow = function(){
        $scope.users.push({ 'email':$scope.email, 'password': $scope.password, 'password_again':$scope.password_again });
        $scope.email='';
        $scope.password='';
        $scope.password_again='';

        var data = 'email=' + $scope.email + '&password=' + $scope.password + '&password_again=' + $scope.password_again;
        $http.post('/saveuser', data )
            .success(function(data, status, headers, config) {
                $scope.message = data;
            })
            .error(function(data, status, headers, config) {
                alert( "failure message: " + JSON.stringify({data: data}));
            });
        // Making the fields empty
        //
        $scope.email='';
        $scope.password='';
        $scope.password_again='';
    };
    });