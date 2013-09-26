package services

import org.specs2.mutable._

import play.api.test.Helpers.defaultAwaitTimeout

import utils.SimpleFakeApp

object TweetFetcherSpec extends Specification {

  "The Tweet class" should {

    "fetch tweets" in new SimpleFakeApp {
      val tweets = play.api.test.Helpers.await(TweetFetcher.fetch("#java, #scala"))

      tweets must not be equalTo(Nil)
      tweets.size must be >(1)
    }
  }

}
