'use strict';

angular.module("tumblrServices", ["ngResource"])
    .factory("Tumblr", function ($resource) {
        return $resource("/api/tumblr/sites/:id/:page/:pageParam", {
                id: "@id", page: "@page", pageParam: "@pageParam"
            }, {
                "query": { method: "GET", isArray: false },
                "getPageByNumber": { method: "GET", params: { page: "page" } }
            }
        );
    })
    .factory("TumblrStats", function ($resource) {
        return $resource("/api/tumblr/stats", {}, { });
    });

angular.module("tumblrNavigation", [])
    .factory("sitesNavigator", function () {
        var currentSiteIndex = -1;
        var sites = null;
        var sitesByType = null;

        return {
            isInitialized: function() {
                return sites != null;
            },
            init: function (data) {
                sitesByType = data.sitesByType;
                sites = data.sites;
            },
            getSitesByType: function() {
                return sitesByType;
            },
            storeSite: function(siteId) {
                if (siteId == null) {
                    siteId = sites[0].id;
                }
                this.setCurrentSiteIndex(siteId);

                return this.getCurrentSite();
            },
            setCurrentSiteIndex: function (siteId) {
                function searchForSiteIdIndex() {
                    var sitesSize = sites.length;
                    for (var i = 0; i < sitesSize; i++) {
                        if (sites[i].id == siteId) {
                            return i;
                        }
                    }

                    return 0;
                }

                currentSiteIndex = searchForSiteIdIndex();
            },
            getCurrentSite: function() {
                return sites[currentSiteIndex];
            },
            getCurrentSiteId: function() {
                return this.getCurrentSite().id;
            },
            getSiteByIndex: function(index) {
                if (this.canNavigateToIndex(index)) {
                    var nextIndex = this.getNextIndex(index);
                    var site = sites[ nextIndex];

                    return site.id;
                }

                return null;
            },
            canNavigateToIndex: function (index) {
                var nextIndex = this.getNextIndex(index);

                return nextIndex >= 0 && nextIndex < sites.length;
            },
            getNextIndex: function(index) {
                return currentSiteIndex + index;
            }
        };
    })
    .factory("pagesNavigator", function () {
        return {
            getPreviousPageNumber: function (page) {
                var previousPageNumber = page.pageNumber - 1;
                if (previousPageNumber !== 0) {
                    return previousPageNumber;
                }

                return -1;
            },
            hasLastPageInfos: function (page) {
                return page != null
                    && page.linkLastPage != null
                    && page.linkLastPage["url"] != null;
            },
            getNextPageNumber: function (page) {
                var nextPageNumber = page.pageNumber + 1;

                if (!this.hasLastPageInfos(page)) {
                    return nextPageNumber;
                }

                if (nextPageNumber <= page.linkLastPage.pageNumber) {
                    return nextPageNumber;
                }

                return -1;
            }
        };
    });

angular.module("tumblrAnimations", [])
    .factory('Animations', function () {
        return {
            toTop: function () {
                $("html, body").animate({ scrollTop: 0 }, "slow");
            }
        };
    });
