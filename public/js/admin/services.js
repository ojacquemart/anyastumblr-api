angular.module("tumblrServices", ["ngResource"])
    .factory("SiteType", function ($resource) {
        return $resource("/api/tumblr/admin/site-type/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" },
            "test": {

            }
        });
    })
    .factory("Site", function ($resource) {
        return $resource("/api/tumblr/admin/site/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" }
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
