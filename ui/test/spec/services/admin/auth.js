'use strict';

describe('Service: admin/auth', function () {

  // load the service's module
  beforeEach(module('uiApp'));

  // instantiate service
  var admin/auth;
  beforeEach(inject(function (_admin/auth_) {
    admin/auth = _admin/auth_;
  }));

  it('should do something', function () {
    expect(!!admin/auth).toBe(true);
  });

});
