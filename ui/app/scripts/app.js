'use strict';

angular.module('uiApp', [ "ngResource", "$strap.directives", "httpInterceptor" ]).config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/sites', {
            templateUrl: 'views/main.html',
            controller: 'TumblrCtrl'
        })
        .when('/sites/:siteId', {
            templateUrl: 'views/main.html',
            controller: 'TumblrCtrl'
        })
        .when('/sites/:siteId/page/:pageNumber', {
            templateUrl: 'views/main.html',
            controller: 'TumblrCtrl'
        })
        .when('/stats', {
            templateUrl: 'views/stats.html',
            controller: 'TumblrStatsCtrl'
        })
        .when('/tweets', {
            templateUrl: 'views/tweets.html',
            controller: 'TweetsCtrl'
        })
        .otherwise({
            redirectTo: '/sites'
        });

    $httpProvider.responseInterceptors.push('httpBroadcaster');
});
