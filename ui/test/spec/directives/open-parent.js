'use strict';

describe('Directive: openParent', function () {

  // load the directive's module
  beforeEach(module('uiApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<open-parent></open-parent>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the openParent directive');
  }));
});
