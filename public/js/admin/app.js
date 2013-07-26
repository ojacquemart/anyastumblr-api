'use strict';

angular.module('adminTumblr', [ "ui", "slugifier", 'tumblrDirectives', 'tumblrServices' ])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/site-types', {
                controller: "SiteTypeController",
                templateUrl: "/assets/partials/admin/site-types.html"
            })
            .when('/sites', {
                controller: "SiteController",
                templateUrl: "/assets/partials/admin/sites.html"
            })
            .when('/sites/new', {
                controller: "NewSiteController",
                templateUrl: "/assets/partials/admin/sites-form.html"
            })
            .when('/sites/edit/:id', {
                controller: "EditSiteController",
                resolve: {
                    site: function(SiteLoader) {
                        return SiteLoader();
                    }
                },
                templateUrl: "/assets/partials/admin/sites-form.html"
            })
            .when('/caches', {
                controller: "CachesController",
                templateUrl: "/assets/partials/admin/caches.html"
            })
            .otherwise({redirectTo: '/site-types'});
    }]);
