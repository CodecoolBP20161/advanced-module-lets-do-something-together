'use strict';

describe('Unit: App, MainCtrl', function () {

    var $httpBackend, $controller, $rootScope, authRequestHandler, urlEncodedParams;

    beforeEach(module('actimate'));

    beforeEach(inject(function ($injector) {
        // Set up the mock http service responses
        $httpBackend = $injector.get('$httpBackend');
        authRequestHandler = $httpBackend.when('GET', '/')
            .respond({user: 'userX'}, {'A-Token': 'xxx'});

        $rootScope = $injector.get('$rootScope');
        var $controller = $injector.get('$controller');

    }));

    afterEach(inject(function ($httpBackend) {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    }));

    it('httpBackend true', function () {
        $httpBackend = true;
    });

    it('Config part is good', inject(function ($http) {

        $httpBackend.withCredentials = true;

        var fakeResponse = {
            access_token: 'X-Requested-With'
        };

        $httpBackend.when('GET', 'oauth/token', urlEncodedParams, function (headers) {
            return headers['Content-Type'] === 'application/json; charset=UTF-8';
        }).respond(200, fakeResponse);

        $httpBackend.useXDomain = true;

        var loggedUser = {email: 'lorszil@gmail.com', password: 'Lor1234'};

        $httpBackend.when('GET', '/login', loggedUser, function (headers) {
            expect(headers['Content-Type']).toBe('application/json');
            // MUST return boolean
            return headers['Content-Type'] === 'application/json';
        }).respond();
    }));

    it('should match route with and without trailing slash', function () {
        $httpBackend.when('/', {templateUrl: 'index.html'}).respond(200);
        $httpBackend.when('/registration', {templateUrl: 'registration.html'}).respond(200);
    });

});