'use strict';

angular.module('adminTumblr', ['tumblrServices' ])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/site-types', {
                templateUrl: "/assets/partials/admin/siteTypes.html",
                controller: SiteTypeController
            })
            .when('/sites', {
                templateUrl: "/assets/partials/admin/sites.html",
                controller: SiteController
            })
            .otherwise({redirectTo: '/site-types'});
    }]);

