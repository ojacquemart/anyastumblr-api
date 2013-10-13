'use strict';

describe('Service: animations', function () {

  // load the service's module
  beforeEach(module('uiApp'));

  // instantiate service
  var animations;
  beforeEach(inject(function (_animations_) {
    animations = _animations_;
  }));

  it('should do something', function () {
    expect(!!animations).toBe(true);
  });

});
