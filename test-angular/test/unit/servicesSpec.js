'use strict';

/* jasmine specs for services go here */

describe('services.tumblrNavigation.sitesNavigator', function () {
    var _sitesNavigator = null;
    var sites = {
        sitesByType: [],
        sites: [
            { "id": "images-tonnantes" },
            { "id": "joiesducode" },
            { "id": "joiesduscrum" } ]
        };

    beforeEach(module('tumblrNavigation'));

    beforeEach(inject(function (sitesNavigator) {
        expect(sitesNavigator).toBeDefined();
        _sitesNavigator = sitesNavigator;

        _sitesNavigator.init(sites);
    }));

    it('checks navigation from a middle site position', function () {
        _sitesNavigator.storeSite("joiesducode");

        expect(_sitesNavigator.canNavigateToIndex(-1)).toBe(true);
        expect(_sitesNavigator.canNavigateToIndex(1)).toBe(true)
        expect(_sitesNavigator.getSiteByIndex(-1)).toBe("images-tonnantes");
        expect(_sitesNavigator.getSiteByIndex(1)).toBe("joiesduscrum");
    });
    it('checks navigation from the first site', function () {
        _sitesNavigator.storeSite("images-tonnantes");

        expect(_sitesNavigator.canNavigateToIndex(-1)).toBe(false);
        expect(_sitesNavigator.canNavigateToIndex(1)).toBe(true)
        expect(_sitesNavigator.getSiteByIndex(-1)).toBe(null);
        expect(_sitesNavigator.getSiteByIndex(1)).toBe("joiesducode");
    });
    it('checks navigation from the last site', function () {
        _sitesNavigator.storeSite("joiesduscrum");

        expect(_sitesNavigator.canNavigateToIndex(-1)).toBe(true);
        expect(_sitesNavigator.canNavigateToIndex(1)).toBe(false)
        expect(_sitesNavigator.getSiteByIndex(-1)).toBe("joiesducode");
        expect(_sitesNavigator.getSiteByIndex(1)).toBe(null);
    });
});

describe('services.tumblrNavigation.pagesNavigator', function () {
    var _pagesNavigator = null;

    beforeEach(module('tumblrNavigation'));

    beforeEach(inject(function (pagesNavigator) {
        expect(pagesNavigator).toBeDefined();
        _pagesNavigator = pagesNavigator;
    }));

    it('checks if a page has last page link infos', function () {
        expect(_pagesNavigator.hasLastPageInfos({
            linkLastPage: {
                "url": "http://joiesducode.com"
            }
        })).toBe(true);

        expect(_pagesNavigator.hasLastPageInfos({
            linkLastPage: null
        })).toBe(false);
    });
    it('checks navigation from the first page', function () {
        expect(_pagesNavigator.getPreviousPageNumber({ pageNumber: 1})).toBe(-1);
        expect(_pagesNavigator.getNextPageNumber({ pageNumber: 1})).toBe(2);
    });
    it('checks navigation from a middle page position', function () {
        var pageInfos = {
            pageNumber: 2,
            linkLastPage: {
                pageNumber: 4,
                "url": "http://joiesducode.com"
            }
        };
        expect(_pagesNavigator.getPreviousPageNumber(pageInfos)).toBe(1);
        expect(_pagesNavigator.getNextPageNumber(pageInfos)).toBe(3);
    });
    it('checks navigation from a middle page position', function () {
        var pageInfos = {
            pageNumber: 4,
            linkLastPage: {
                pageNumber: 4,
                "url": "http://joiesducode.com"
            }
        };
        expect(_pagesNavigator.getPreviousPageNumber(pageInfos)).toBe(3);
        expect(_pagesNavigator.getNextPageNumber(pageInfos)).toBe(-1);
    });
});