angular.module("tumblrServices", ["ngResource"])
    .factory("SiteType", function ($resource) {
        return $resource("/api/tumblr/admin/site-types/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" }
        });
    })
    .factory("Site", function ($resource) {
        return $resource("/api/tumblr/admin/sites/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" }
        });
    })
    .factory("Caches", function ($resource) {
        return $resource("/api/tumblr/admin/caches/:key", {
            key: "@key"
        }, {
            'removeAll':    { method: 'DELETE' },
            'remove':       { method: 'DELETE' }
        });
    })
    .factory("SlugChecker", function ($resource) {
        return $resource("/api/tumblr/admin/:type/slug/exists/:value", {
        }, {
            "notExistsSlug": { method: "GET" }
        });
    })
    .factory("SiteLoader", ["Site", "$route", "$q",
        function (Site, $route, $q) {
            return function () {
                var delay = $q.defer();
                var siteId = $route.current.params.id;
                Site.get({id: siteId}, function (recipe) {
                    delay.resolve(recipe);
                }, function () {
                    delay.reject("Unable to fetch site " + siteId);
                });
                return delay.promise;
            };
    }])
;

