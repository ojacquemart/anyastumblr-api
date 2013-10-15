'use strict';

angular.module('uiApp')
    .factory("tumblrApi", function ($resource) {
        return $resource("/api/tumblr/sites/:id/:page/:pageParam", {
                id: "@id", page: "@page", pageParam: "@pageParam"
            }, {
                "query": { method: "GET", isArray: false },
                "getPageByNumber": { method: "GET", params: { page: "page" } }
            }
        );
    })
