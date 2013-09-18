'use strict';

var directives = angular.module('tumblrDirectives', []);
directives
    .directive('openParent', function () {
        return {
            restrict: "A",
            link: function (scope, element) {
                element.bind('mouseover', function() {
                    element.parent().addClass("open");
                });
            }
        };
    });