'use strict';

angular.module('uiApp').directive('faviconSite', function () {
    return {
        restrict: "E",
        scope: {
            site: '=site'
        },
        link: function (scope) {

        },
        template: '<img class="tumblr-site-favicon" ng-src="{{site.favicon}}" />{{site.name}}'
    };
});
