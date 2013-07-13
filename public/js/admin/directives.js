'use strict';

var directives = angular.module('tumblrDirectives', []);

directives
    .directive('selectYesNo', function () {
        return {
            restrict: "E",
            scope: {
                value: '=value'
            },
            link: function (scope) {
                scope.trueFalseOptions = [
                    { value: true, label: "Yes"},
                    { value: false, "label": "No"}
                ];
            },
            template: '<select ng-model="value" ' +
                'ng-options="option.value as option.label for option in trueFalseOptions" ' +
                'ng-required="!(value == true || value == false)"> ' +
                '</select>'
        };
    })
    // TODO: use http://mgcrea.github.io/angular-strap/
    .directive('bsNavbar', function ($location) {
        'use strict';

        return {
            restrict: 'A',
            link: function postLink(scope, element, attrs, controller) {
                // Watch for the $location
                scope.$watch(function () {
                    return $location.path();
                }, function (newValue, oldValue) {

                    $('li[data-match-route]', element).each(function (k, li) {
                        var $li = angular.element(li),
                        // data('match-rout') does not work with dynamic attributes
                            pattern = $li.attr('data-match-route'),
                            regexp = new RegExp('^' + pattern + '$', ['i']);

                        if (regexp.test(newValue)) {
                            $li.addClass('active');
                        } else {
                            $li.removeClass('active');
                        }

                    });
                });
            }
        };
    })
;