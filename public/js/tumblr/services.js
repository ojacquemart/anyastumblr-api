'use strict';

angular.module("tumblrServices", ["ngResource"])
    .factory("Tumblr", function ($resource) {
        return $resource("/api/tumblr/sites/:id/:page/:pageParam", {
                id: "@id", page: "@page", pageParam: "@pageParam"
            }, {
                "getTotalPage": { method: "GET", params: { page: "page", pageParam: "total" } },
                "getPageByNumber": { method: "GET", params: { page: "page" } }
            }
        );
    })
    .factory("TumblrStats", function ($resource) {
        return $resource("/api/tumblr/stats", {}, { });
    })
;

angular.module("tumblrAnimations", [])
    .factory('Animations', function() {
        return {
            toTop : function() {
                $("html, body").animate({ scrollTop: 0 }, "slow");
            }
        };
    })
;
