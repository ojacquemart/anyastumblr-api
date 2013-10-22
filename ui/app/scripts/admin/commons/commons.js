"use strict";

angular.module("admin.commons", [])
    .factory("SlugChecker", function ($resource) {
        return $resource("/api/tumblr/admin/:type/slug/exists/:value", {
        }, {
            "notExistsSlug": { method: "GET" }
        });
    })
;