$(function() {

    // Search
    var search = function() {
        console.log(Router.controllers.Tweets.stream().url)
        var stream = new EventSource(Router.controllers.Tweets.stream().url)

        $(stream).on('message', function(e) {
            var tweet = JSON.parse(e.originalEvent.data)
            console.log(e.originalEvent)
            console.log(tweet)
        })
    }
    search();

})