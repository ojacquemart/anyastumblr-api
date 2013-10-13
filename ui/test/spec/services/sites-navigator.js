'use strict';

describe('Service: sitesNavigator', function () {

    var _sitesNavigator = null;
    var sites = {
        sitesByType: [],
        sites: [
            { "id": "images-tonnantes" },
            { "id": "joiesducode" },
            { "id": "joiesduscrum" }
        ]
    };

    beforeEach(module('uiApp'));

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
