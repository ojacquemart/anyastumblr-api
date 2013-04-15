/**
 * Format method, java like.
 * @return {String}
 */
String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        if (typeof args[number] != 'undefined') {
            return args[number];
        }

        return "";
    });
};

$(function () {
    // FIXME: to change... for tweets streaming.
    var search = function () {
        console.log(Router.controllers.Tweets.stream().url)
        var stream = new EventSource(Router.controllers.Tweets.stream().url)

        $(stream).on('message', function (e) {
            var tweet = JSON.parse(e.originalEvent.data)
            console.log(e.originalEvent)
            console.log(tweet)
        })
    }
    search();

})