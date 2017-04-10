
describe('Rout.js test', function() {

    var $state, $httpBackend;

    beforeEach(function () {angular.module('actimate')});

    beforeEach(function () {
        angular.mock.inject(function ($injector) {
            $httpBackend = $injector.get('$httpBackend');
        })
    });

    afterEach(inject(function ($httpBackend) {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    }));

    it('should match route with and without trailing slash', function () {
        // $httpBackend.when('/', {templateUrl: 'main.html'}).respond(200);
        /*$httpBackend.when('/registration', {templateUrl: 'registration.html'}).respond(200);
        $httpBackend.when('/u/profile', {templateUrl: 'profile.html'}).respond(200);
        $httpBackend.when('/u/dashboard', {templateUrl: 'user_main.html'}).respond(200);*/
    });

});
