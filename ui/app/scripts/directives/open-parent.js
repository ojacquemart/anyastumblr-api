'use strict';

angular.module('uiApp').directive('openParent', function () {
    return {
        restrict: "A",
        link: function (scope, element) {
            element.bind('mouseover', function () {
                element.parent().addClass("open");
            });
        }
    };
})