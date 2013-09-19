'use strict';

/* jasmine specs for services go here */

describe('tumblr.services.Pagination', function () {
    // Load your module.
    beforeEach(module('tumblrServices'));

    // Setup the mock service in an anonymous module.
    beforeEach(module(function ($provide) {
    }));

    it('can get an instance of my factory', inject(function(TumblrPagination) {
        expect(TumblrPagination).toBeDefined();
        expect(TumblrPagination.getSites()).toBeDefined();
        expect(TumblrPagination.getSites().length).toBe(1);

    }));
});