/**
 * Angular app definition.
 */
angular.module('hfrcat', []).
    config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: 'assets/partials/topics-list.html',   controller: TopicListCtrl}).
            otherwise({redirectTo: '/'});
    }])