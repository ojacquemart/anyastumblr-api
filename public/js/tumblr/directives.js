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
    })
    .directive('faviconSite', function () {
        return {
            restrict: "E",
            scope: {
                site: '=site'
            },
            link: function (scope) {

            },
            template: '<img class="tumblr-site-favicon" src="{{site.favicon}}" />{{site.name}}'
        };
    });