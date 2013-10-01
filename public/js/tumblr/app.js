'use strict';

angular.module('AnyAsTumblr', [ "ngRoute", "commonDirectives", "httpInterceptor",
        "$strap.directives",
        "tumblrDirectives", "tumblrAnimations", "tumblrServices", "tumblrNavigation"])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                templateUrl: "assets/partials/tumblr/main.html",
                controller: TumblrController
            })
            .when('/sites/:siteId', {
                templateUrl: "assets/partials/tumblr/main.html",
                controller: TumblrController
            })
            .when('/sites/:siteId/page/:pageNumber', {
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

