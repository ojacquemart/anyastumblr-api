"use strict";

angular.module("admin.commons", [])
    .factory("SlugChecker", function (secureResource) {
        return secureResource("/api/tumblr/admin/:type/slug/exists/:value", {
        }, {
            "notExistsSlug": { method: "GET" }
        });
    })
;