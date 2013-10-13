'use strict';

angular.module('uiApp')
    .service('pageNavigator', function pageNavigator() {
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
