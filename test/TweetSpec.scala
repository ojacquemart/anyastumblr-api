import org.specs2.mutable._

import concurrent._
import ExecutionContext.Implicits.global

import concurrent.Await
import concurrent.duration.Duration
import java.util.concurrent.TimeUnit

import twitter._

import TestHelpers._

class TweetSpec extends Specification {

  "The Tweet class" should {

    "fetch tweets" in new FakeApp {
      val futureTweets = Tweet.fetch("#java, #scala")
      Await.ready(futureTweets, Duration(5, TimeUnit.SECONDS))

      val tweets = seq[Tweet](futureTweets)
      tweets must not be equalTo(Nil)
      tweets.size must be >(1)
    }

  }

}
