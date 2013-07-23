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
    .directive('ngUniqueSlug', function(Slug, SlugChecker) {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                elem.on('blur', function (evt) {
                    scope.$apply(function () {
                        var slug = Slug.slugify(elem.val());
                        if (slug.length == 0) {
                            return false;
                        }

                        var json = angular.fromJson(attrs.ngUniqueSlug);

                        function getSlugs() {
                            var objects = scope.$parent[json.objectsVarName];

                            var slugs = []  ;
                            angular.forEach(objects, function(obj, key) {
                                slugs.push(obj.slug);
                            });

                            return slugs;
                        }

                        function hasSlugInList() {
                            return getSlugs()
                                    .filter(function(value) { return value == slug })
                                    .length > 1;
                        }

                        if (hasSlugInList()) {
                            ctrl.$setValidity('unique', false);

                            return false;
                        }

                        SlugChecker.notExistsSlug({ "type": json.type, "value": slug }, function (data) {
                            var exists = data.exists;

                            var hasJsonId = json.id != undefined &&  json.id.length > 0;

                            // Add mode, the slug exists and is equal to the slug typed.
                            if (hasJsonId && exists && data.id == json.id) {
                                ctrl.$setValidity('unique', true);
                            } else {
                                ctrl.$setValidity('unique', !exists);
                            }

                        }, function() {
                            ctrl.$setValidity('unique', false);
                        });
                    });
                });
            }
        }
    });