'use strict';

describe('Service: httpBroadcaster', function () {

  // load the service's module
  beforeEach(module('uiApp'));

  // instantiate service
  var httpBroadcaster;
  beforeEach(inject(function (_httpBroadcaster_) {
    httpBroadcaster = _httpBroadcaster_;
  }));

  it('should do something', function () {
    expect(!!httpBroadcaster).toBe(true);
  });

});
