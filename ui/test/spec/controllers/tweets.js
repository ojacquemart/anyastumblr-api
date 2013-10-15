'use strict';

describe('Controller: TweetsCtrl', function () {

  // load the controller's module
  beforeEach(module('uiApp'));

  var TweetsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TweetsCtrl = $controller('TweetsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
