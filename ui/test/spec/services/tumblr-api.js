'use strict';

describe('Service: tumblrApi', function () {

  // load the service's module
  beforeEach(module('uiApp'));

  // instantiate service
  var tumblrApi;
  beforeEach(inject(function (_tumblrApi_) {
    tumblrApi = _tumblrApi_;
  }));

  it('should do something', function () {
    expect(!!tumblrApi).toBe(true);
  });

});
