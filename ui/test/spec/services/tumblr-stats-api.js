'use strict';

describe('Service: tumblrStatsApi', function () {

  // load the service's module
  beforeEach(module('uiApp'));

  // instantiate service
  var tumblrStatsApi;
  beforeEach(inject(function (_tumblrStatsApi_) {
    tumblrStatsApi = _tumblrStatsApi_;
  }));

  it('should do something', function () {
    expect(!!tumblrStatsApi).toBe(true);
  });

});
