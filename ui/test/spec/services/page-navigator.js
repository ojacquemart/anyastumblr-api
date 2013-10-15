'use strict';

describe('Service: pageNavigator', function () {

    var _pageNavigator = null;

    beforeEach(module('uiApp'));

    beforeEach(inject(function (pageNavigator) {
        expect(pageNavigator).toBeDefined();
        _pageNavigator = pageNavigator;
    }));

    it('checks if a page has last page link infos', function () {
        expect(_pageNavigator.hasLastPageInfos({
            linkLastPage: {
                "url": "http://joiesducode.com"
            }
        })).toBe(true);

        expect(_pageNavigator.hasLastPageInfos({
            linkLastPage: null
        })).toBe(false);
    });
    it('checks navigation from the first page', function () {
        expect(_pageNavigator.getPreviousPageNumber({ pageNumber: 1})).toBe(-1);
        expect(_pageNavigator.getNextPageNumber({ pageNumber: 1})).toBe(2);
    });
    it('checks navigation from a middle page position', function () {
        var pageInfos = {
            pageNumber: 2,
            linkLastPage: {
                pageNumber: 4,
                "url": "http://joiesducode.com"
            }
        };
        expect(_pageNavigator.getPreviousPageNumber(pageInfos)).toBe(1);
        expect(_pageNavigator.getNextPageNumber(pageInfos)).toBe(3);
    });
    it('checks navigation from a middle page position', function () {
        var pageInfos = {
            pageNumber: 4,
            linkLastPage: {
                pageNumber: 4,
                "url": "http://joiesducode.com"
            }
        };
        expect(_pageNavigator.getPreviousPageNumber(pageInfos)).toBe(3);
        expect(_pageNavigator.getNextPageNumber(pageInfos)).toBe(-1);
    });

});
