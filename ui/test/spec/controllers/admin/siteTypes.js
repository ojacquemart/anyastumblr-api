'use strict';

describe('Controller: AdminSitetypesCtrl', function () {

  // load the controller's module
  beforeEach(module('uiApp'));

  var AdminSitetypesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminSitetypesCtrl = $controller('AdminSitetypesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
