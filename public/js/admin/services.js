angular.module("tumblrServices", ["ngResource"])
    .factory("SiteType", function ($resource) {
        return $resource("/api/tumblr/admin/site-type/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" }
        });
    })
    .factory("Site", function ($resource) {
        return $resource("/api/tumblr/admin/site/:id", {
            id: "@id"
        }, {
            "update": { method: "PUT" }
        });
    })
;