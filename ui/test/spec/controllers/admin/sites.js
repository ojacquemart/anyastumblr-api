'use strict';

describe('Controller: AdminSitesCtrl', function () {

  // load the controller's module
  beforeEach(module('uiApp'));

  var AdminSitesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminSitesCtrl = $controller('AdminSitesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
