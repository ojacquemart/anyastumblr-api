'use strict';

angular.module('uiApp', [
        "ngRoute",
        "ngResource",
        "ngCookies",
        "httpInterceptor",
        "slugifier",
        "admin.auth",
        "admin.caches",
        "admin.commons",
        "admin.directives",
        "admin.navbar",
        "admin.sites",
        "admin.siteTypes",
        "$strap.directives" ])
    .config(function ($routeProvider, $httpProvider) {
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
    }).run(['$rootScope', '$location', 'Auth', function ($rootScope, $location, Auth) {

        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            $rootScope.error = null;
            if (next.access === "admin") {
                if (!Auth.isLoggedIn()) {
                    $location.path("/admin/login");
                } else if (next.login) {
                    $location.path("/admin/index");
                }

            }
        });

    }]);
