'use strict';

angular.module('AnyAsTumblr', [ "httpInterceptor", "$strap.directives",  "tumblrServices"])
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

