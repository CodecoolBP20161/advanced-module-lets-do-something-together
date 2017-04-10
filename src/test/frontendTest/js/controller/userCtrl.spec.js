
describe('New UserCtrl', function () {
   var $controller, userController, mockUserResource;
   var user, $httpBackend, $httpProvider, $http, Provider, $provide;

    var $scope ={};
    $scope.user = {
        email: 'lorszil@gmail.com',
        password: 'Lor1234',
        passwordAgain: 'Lor1234'
    };

   beforeEach(angular.mock.module('actimate'));
   beforeEach(angular.mock.module('userCtrl'));

   beforeEach(function () {
        angular.mock.inject(function ($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $httpProvider = $injector.get('$httpProvider');
            $scope = $injector.get('$scope');
            $provide = $injector.get('$provide');
            mockUserResource = $injector.get($scope.user);
        })
   });

   beforeEach(inject(function ($controller) {
        UserCtrl = $controller('UserCtrl');
   }));

   beforeEach(module(function ($provide) {
        user = {};
        $provide.value('user', user);
   }));

   it('should be defined', function () {

       expect(userController).toBeDefined();

   })

});
