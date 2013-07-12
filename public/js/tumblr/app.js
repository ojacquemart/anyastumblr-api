'use strict';

angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('loadingHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            // todo start the spinner here
            $('#loading').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    // register the interceptor as a service, intercepts ALL angular ajax http calls
    .factory('loadingHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                // todo hide the spinner
                $('#loading').hide();
                $('#error').hide();
                return response;

            }, function (response) {
                // do something on error
                // todo hide the spinner
                $('#error').show();
                $('#loading').hide();
                return $q.reject(response);
            });
        };
    });

angular.module('AnyAsTumblr', ['SharedServices'])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.
            when('/', {
                templateUrl: "assets/partials/tumblr/main.html",
                controller: TumblrController
            })
            .when('/stats', {
                templateUrl: "assets/partials/tumblr/stats.html",
                controller: TumblrStatsController
            })
            .when('/tweets', {
                templateUrl: 'assets/partials/tumblr/tweets.html',
                controller: TweetsController
            })
            .otherwise({redirectTo: '/'});
    }]);

