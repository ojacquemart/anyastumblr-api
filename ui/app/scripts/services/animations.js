'use strict';

angular.module('uiApp')
  .factory('animations', function () {
    return {
        toTop: function () {
            $("html, body").animate({ scrollTop: 0 }, "slow");
        }
    };
  });
