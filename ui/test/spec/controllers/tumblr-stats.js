'use strict';

describe('Controller: TumblrStatsCtrl', function () {

  // load the controller's module
  beforeEach(module('uiApp'));

  var TumblrStatsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TumblrStatsCtrl = $controller('TumblrStatsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
