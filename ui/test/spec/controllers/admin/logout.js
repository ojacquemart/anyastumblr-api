'use strict';

describe('Controller: AdminLogoutCtrl', function () {

  // load the controller's module
  beforeEach(module('uiApp'));

  var AdminLogoutCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminLogoutCtrl = $controller('AdminLogoutCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
