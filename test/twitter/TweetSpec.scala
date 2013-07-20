package twitter

import org.specs2.mutable._

import play.api.test.Helpers._

import twitter._

import utils.SimpleFakeApp

class TweetSpec extends Specification {

  "The Tweet class" should {

    "fetch tweets" in new SimpleFakeApp {
      val tweets = await(Tweet.fetch("#java, #scala"))

      tweets must not be equalTo(Nil)
      tweets.size must be >(1)
    }
  }

  "The TweetSinceIdCache class" should {

    "generate different cache keys according to query parameter" in new SimpleFakeApp {
      val javaCacheKey = TweetSinceIdCache.getCacheKey("java")
      val scalaCacheKey = TweetSinceIdCache.getCacheKey("scala")
      javaCacheKey must not be equalTo(scalaCacheKey)
    }

    "put in cache the first tweet id, used to query since_id" in new SimpleFakeApp {
      val tweet1 = new Tweet("1", "foo", "bar", "avatar", "source")
      val tweet2 = new Tweet("2", "foo", "bar", "avatar", "source")

      val tweets = List(tweet2, tweet1)
      TweetSinceIdCache.put("java", tweets)
      val sinceId = TweetSinceIdCache.get("java")
      sinceId must be equalTo(tweet2.id)
    }

  }

}
