'use strict';

angular.module('uiApp', [
        "ngResource",
        "ngCookies",
        "httpInterceptor",
        "admin.directives",
        "admin.services",
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
            .when('/admin/login', {
                templateUrl: 'views/admin/login.html',
                controller: 'AdminLoginCtrl',
                access:  "admin",
                login: true
            })
            .when('/admin/index', {
                templateUrl: 'views/admin/sites.html',
                controller: 'AdminSitesCtrl',
                access: "admin"
            })
            .when('/admin/sites', {
                templateUrl: 'views/admin/sites.html',
                controller: 'AdminSitesCtrl',
                access: "admin"
            })
            .when('/admin/sites/new', {
                templateUrl: "views/admin/sites-form.html",
                controller: "AdminNewSiteCtrl",
                access: "admin"
            })
            .when('/admin/sites/edit/:id', {
                controller: "AdminEditSiteCtrl",
                resolve: {
                    site: function(SiteLoader) {
                        return SiteLoader();
                    }
                },
                templateUrl: "views/admin/sites-form.html",
                access: "admin"
            })
            .when('/admin/site-types', {
                templateUrl: 'views/admin/site-types.html',
                controller: 'AdminSiteTypesCtrl',
                access: "admin"
            })
            .when('/admin/caches', {
                controller: "AdminCachesCtrl",
                templateUrl: "views/admin/caches.html",
                access: "admin"
            })
            .otherwise({
                redirectTo: '/sites'
            });

        $httpProvider.responseInterceptors.push('httpBroadcaster');
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
