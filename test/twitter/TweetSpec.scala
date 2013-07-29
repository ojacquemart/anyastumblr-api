package twitter

import org.specs2.mutable._

import play.api.test.Helpers._

import twitter._

import utils.SimpleFakeApp

object TweetSpec extends Specification {

  "The Tweet class" should {

    "fetch tweets" in new SimpleFakeApp {
      val tweets = await(Tweet.fetch("#java, #scala"))

      tweets must not be equalTo(Nil)
      tweets.size must be >(1)
    }
  }

}
