/**
 * AngularJs - app.js
 */
angular.module('SharedServices', [])
    .config(function ($httpProvider) {
        $httpProvider.responseInterceptors.push('myHttpInterceptor');
        var spinnerFunction = function (data, headersGetter) {
            // todo start the spinner here
            $('#loading').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    })
    // register the interceptor as a service, intercepts ALL angular ajax http calls
    .factory('myHttpInterceptor', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                // todo hide the spinner
                $('#loading').hide();
                $('#error').hide();
                return response;

            }, function (response) {
                // do something on error
                // todo hide the spinner
                $('#error').show();
                $('#loading').hide();
                return $q.reject(response);
            });
        };
    })

angular.module('hfrGifs', ['SharedServices']).directive('hoverable', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            elm.bind('mouseover', function () {
                scope.$apply(function () {
                    var imageSrc = elm.attr("src");
                    // Id to glue is the image...
                    var clip = new ZeroClipboard.Client();
                    clip.glue(imageSrc);
                    clip.setText(imageSrc);
                    clip.addEventListener('complete', function (client, text) {
                    });
                });

            });
        }
    };
});
//angular.module('hfrGifs', ['SharedServices']).directive('whenScrolled', function () {
//    return function (scope, elm, attr) {
//        var raw = elm[0];
//        elm.bind('scroll', function () {
//            // Need to scroll... call funciton in directive..
//            console.log("jquery way " + raw.scrollTop + "+" + raw.offsetHeight + ">=" + $(window).height());
//            console.log("angular way " + raw.scrollTop + "+" + raw.offsetHeight + ">=" + raw.scrollHeight);
//            if (raw.scrollTop + raw.offsetHeight >= $(window).height()) {
//            if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
//                scope.$apply(attr.whenScrolled);
//            }
//        });
//
//    };
//
//});